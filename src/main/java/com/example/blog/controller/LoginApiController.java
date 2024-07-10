package com.example.blog.controller;

import com.example.blog.domain.RefreshToken;
import com.example.blog.domain.User;
import com.example.blog.domain.Role;
import com.example.blog.dto.UserLoginDto;
import com.example.blog.dto.UserLoginResponseDto;
import com.example.blog.jwt.token.JwtTokenizer;
import com.example.blog.service.RefreshTokenService;
import com.example.blog.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

// 로그인 검증 + 토큰 생성 등등
@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    // 로그인 처리
    // BindingResult - 웹 폼을 제출할 때, 서버 측에서 폼 데이터가 올바른지, 필요한 값이 빠지지 않았는지,
    // 형식등을 검증하고, 검증 결과를 저장하고 처리하는데 사용되는 객체
    // @RequestBody - HTTP 요청 본문에 포함된 JSON의 데이터를 자바 객체로 변환
    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto,
                        BindingResult bindingResult, HttpServletResponse response) {
        // 1. 폼 데이터가 오류가 있는 경우 거르기 ex) null
        if (bindingResult.hasErrors()) {
            // 에러 메시지 반환(상태코드 : 400)
            log.info("error : 폼 데이터에 오류 발생");
            return new ResponseEntity("비밀번호 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. DB 사용자 조회 해서 비밀번호 매칭하여 거르기
        User user = userService.findByUsername(userLoginDto.getUsername());
        if(user == null){
            log.info("error : 사용자가 존재하지 않습니다.");
            return new ResponseEntity("사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            // 비밀번호 미 일치 시 에러 메시지 반환(상태 코드 : 401)
            log.info("error : 비밀번호가 올바르지 않습니다.");
            return new ResponseEntity("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 3. 유저의 권한 꺼내오기
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        // 4. 토큰 발급하기
        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);

        // 5. 리프레시 토큰 db에 저장
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setId(user.getId());
        refreshTokenEntity.setUserId(user.getUsername());
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenService.addrefreshToken(refreshTokenEntity);

        // 6. 응답으로 보낼 정보 설정(토큰 + 유저 정보)
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .name(user.getName())
                .build();

        // 7. 쿠키에 토큰 저장 저장
        // 엑세스 토큰 쿠키
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true); // 쿠키값을 js등에서 접근 불가능
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));
        response.addCookie(accessTokenCookie);
        // 리프레시 토큰 쿠키
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT/1000)); //7일
        response.addCookie(refreshTokenCookie);

        // 응답 보내기
        return new ResponseEntity(userLoginDto,HttpStatus.OK);

    }


    // 리프레시 토큰 -> acc토큰 생성
    @PostMapping("/api/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // 1. 쿠키안에 있는 refresh Token 꺼내기
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if(cookies !=null){ // 쿠키들이 존재한다면
            for(Cookie cookie : cookies)
                // 쿠키의 키 이름이 refreshToken인 것 찾기
                if(cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                    break;
                }
        }
        // 만약 쿠키가 없거나, 쿠키들을 살펴봣는데 refresh Token이 없다면
        if(refreshToken == null){
            // 에러 메시지 반환(상태코드 : 400)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // 2. 쿠키에 refresh Token이 있다면, 정보 얻어오기
        // 리프레시 토큰을 파싱하여 클레임 객체를 반환
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        // 클레임 객체를 통해 주키 id 찾기
        Long userId = Long.valueOf((Integer)claims.get("userId"));
        //id를 이용하여 user 사용자 객체 찾기 -> 없을 시 예외 발생
        User user = userService.findUser(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        // 클래임 객체를 통해 해당 사용자 권한들 뽑기
        List roles = (List)claims.get("roles");

        // 3. accessToken 생성
        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getName(), user.getUsername(), roles);

        // 4. 쿠키 생성 후 accessToken 담아서 응답으로 보낸다
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));
        response.addCookie(accessTokenCookie);

        // 5. api 응답 결과
        UserLoginResponseDto responseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .userId(user.getId())
                .build();
        return new ResponseEntity(responseDto,HttpStatus.OK);
    }

}
