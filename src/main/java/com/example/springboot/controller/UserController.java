package com.example.springboot.controller;

import com.example.springboot.entity.MemberEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "User API", description = "사용자 관리 API")     // Swagger 컨트롤러 단위 설명
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "유저 정보 저장", description = "유저 정보를 저장합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "request", description = "Map<String, Object>")
    @PostMapping("/saveUser")
    public ResponseEntity<MemberEntity> saveUser(@RequestBody Map<String, Object> request) {
        String name = (String)request.get("name");
        String password = (String)request.get("password");
        MemberEntity userEntity = userService.saveUser(name, password);
        // 정상일 때만 200 OK
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @Operation(summary = "유저 정보 삭제", description = "유저 정보를 삭제합니다.")
    @PostMapping("/deleteAll")
    public void deleteAll() {
        userService.deleteAll();
    }

    @Operation(summary = "유저 정보 Count", description = "유저들의 수를 조회합니다.")
    @PostMapping("/getCount")
    public ResponseEntity<Long> getCount() {
        return new ResponseEntity<>(userService.getCount(), HttpStatus.OK);
    }

    @Operation(summary = "유저 Level Up", description = "유저의 등급을 상승시킵니다.")
    @PostMapping("/upgradeLevels")
    public void upgradeLevels() {
        userService.upgradeLevels();
    }
}
