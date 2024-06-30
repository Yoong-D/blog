package com.example.blog.controller;

import com.example.blog.domain.DuplicateChecker;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    private final UserService userService;

    // 사용자 아이디가 중복되는지 확인
    @GetMapping("/api/users/check-username")
    // 중복이 없다면 json 객체로 내보내고, 중복이 있다면 에러인 String 타입으로 내놓을것이기에 Object로 선언
    public ResponseEntity<Object> checkName(@RequestParam("username") String username) {

        boolean nameCheck = userService.isUserExists(username); // id 중복 여부
        DuplicateChecker duplicateChecker = new DuplicateChecker(nameCheck);

        if (!nameCheck) { // 빈 값이 아니고 + 중복값이 존재하지 않는다면 200.ok보내기
            if(username.trim().isEmpty()){// 빈 값일 시 400 Bad Request 보내기
                return ResponseEntity
                        .badRequest()
                        .body("아이디를 입력해주세요."); // 응답에 대한 본문
            }
            return ResponseEntity.ok(duplicateChecker);
        }
        else{
            return ResponseEntity // 중복값일 시 400 Bad Request 보내기
                    .badRequest()
                    .body("해당 아이디가 이미 존재합니다."); // 응답에 대한 본문
        }
    }

    // 사용자의 이메일이 중복되는지 확인
    @GetMapping("/api/users/check-email")
    public ResponseEntity<Object> checkEmail(@RequestParam("email") String email) {

        boolean emailCheck = userService.isEmailExists(email); // eamil 중복 여부
        DuplicateChecker duplicateChecker = new DuplicateChecker(emailCheck);

        if(!emailCheck){// 빈 값이 아니고 + 중복값이 존재하지 않는다면 200.ok보내기
            if(email.trim().isEmpty()){// 빈 값일 시 400 Bad Request 보내기
                return ResponseEntity
                        .badRequest()
                        .body("이메일을 입력해주세요."); // 응답에 대한 본문
            }
            return ResponseEntity.ok(duplicateChecker);
        }else{
            return ResponseEntity // 중복값일 시 400 Bad Request 보내기
                    .badRequest()
                    .body("해당 이메일이 이미 존재합니다."); // 응답에 대한 본문
        }
    }

}
