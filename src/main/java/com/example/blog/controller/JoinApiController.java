package com.example.blog.controller;

import com.example.blog.domain.DuplicateChecker;
import com.example.blog.domain.User;
import com.example.blog.dto.JoinUserDto;
import com.example.blog.dto.UserLoginDto;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JoinApiController {
    private final UserService userService;

    // 회원 가입 정보를 받아 들여 회원 등록을 수행하고, 성공시 환영 메시지 보여주는 화면으로 리다이렉트
    @PostMapping("/api/userreg")
    public ResponseEntity<JoinUserDto> userReg(@RequestBody @Valid JoinUserDto joinUserDto,
                                               BindingResult bindingResult) {
        log.info("api실행됨!");
        // 1. 폼 데이터(vaild)가 유효한지 체크
        if (bindingResult.hasErrors()) {
            log.info("error : 폼 데이터에 오류 발생");
            return new ResponseEntity("비밀번호 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. 비밀번호와 확인 비밀번호가 일치하는지 체크
        if (!joinUserDto.getPassword().equals(joinUserDto.getAgainPassword())) {
            log.info("error : 비밀번호 미 일치 ");
            return new ResponseEntity("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 3. 사용자 추가하기
        // String username, String password, String email, String name
        User user = new User(joinUserDto.getUsername(), joinUserDto.getPassword(), joinUserDto.getEmail(), joinUserDto.getName());
        userService.addUser(user);
        return new ResponseEntity(joinUserDto, HttpStatus.OK);

    }

    // 사용자 아이디가 중복되는지 확인
    @GetMapping("/api/users/check-username")
    // 중복이 없다면 json 객체로 내보내고, 중복이 있다면 에러인 String 타입으로 내놓을것이기에 Object로 선언
    public ResponseEntity<Object> checkName(@RequestParam("username") String username) {

        boolean nameCheck = userService.isUserExists(username); // id 중복 여부
        DuplicateChecker duplicateChecker = new DuplicateChecker(nameCheck);

        // 중복이 아닌경우
        if (!nameCheck) {
            // 빈 값일 시 400 Bad Request 보내기
            if (username.trim().isEmpty()) {
                log.info("error : 아이디 미입력");
                return new ResponseEntity("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(duplicateChecker,HttpStatus.OK);
        } else {
            // 중복값일 시 400 Bad Request 보내기
            log.info("error : 아이디 중복");
            return new ResponseEntity("해당 아이디가 이미 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 사용자의 이메일이 중복되는지 확인
    @GetMapping("/api/users/check-email")
    public ResponseEntity<Object> checkEmail(@RequestParam("email") String email) {

        boolean emailCheck = userService.isEmailExists(email); // eamil 중복 여부
        DuplicateChecker duplicateChecker = new DuplicateChecker(emailCheck);

        // 중복이 아닌경우
        if (!emailCheck) {
            // 빈 값일 시 400 Bad Request 보내기
            if (email.trim().isEmpty()) {// 빈 값일 시 400 Bad Request 보내기
                log.info("error : 이메일 미입력");
                return new ResponseEntity("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(duplicateChecker,HttpStatus.OK);
        } else { // 중복값일 시 400 Bad Request 보내기
            log.info("error : 이메일 중복");
            return new ResponseEntity("해당 이메일이 이미 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }

}
