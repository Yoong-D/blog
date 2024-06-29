package com.example.blog.controller;

import com.example.blog.domain.DuplicateChecker;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // 사용자 아이디가 중복되는지 확인
    @GetMapping("/api/users/check-username")
    // 중복이 없다면 json 객체로 내보내고, 중복이 있다면 에러인 String 타입으로 내놓을것이기에 Object로 선언
    public ResponseEntity<Object> checkName(@RequestParam("username") String username) {
        boolean nameCheck = userService.isUserExists(username); // 해당 값에 중복값이 존재하냐?
        DuplicateChecker duplicateChecker = new DuplicateChecker(nameCheck);
        if (!nameCheck) { // 중복값이 존재하지 않는다면 200.ok보내기
            return ResponseEntity.ok(duplicateChecker);
        } else{
            return ResponseEntity
                    .badRequest()
                    .body("Username is already taken");
        }
    }

    // 사용자의 이메일이 중복되는지 확인
    @GetMapping("/api/users/check-email")
    public ResponseEntity<Object> checkEmail(@RequestParam("email") String email) {
        boolean emailCheck = userService.isUserExists(email);
        DuplicateChecker duplicateChecker = new DuplicateChecker(emailCheck);
        if(!emailCheck){
            return ResponseEntity.ok(duplicateChecker);
        }else{
            return ResponseEntity
                    .badRequest()
                    .body("email is already taken");
        }
    }

}
