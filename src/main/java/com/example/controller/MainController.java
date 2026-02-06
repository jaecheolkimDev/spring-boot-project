package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Main API", description = "Main 관리 API")     // Swagger 컨트롤러 단위 설명
@Controller
public class MainController {

    @Operation(summary = "http://localhost:8080/ 이동", description = "hello페이지로 이동합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "void", description = "void")
    @GetMapping("/")
    public String main() {
        return "hello";
    }

    @Operation(summary = "http://localhost:8080/main 이동", description = "main페이지로 이동합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "void", description = "void")
    @GetMapping("/main")
    public String main2() {
        return "main";
    }


}
