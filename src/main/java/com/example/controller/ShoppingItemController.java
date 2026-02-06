package com.example.controller;

import com.example.dto.MemberFormDto;
import com.example.entity.ShoppingMemberEntity;
import com.example.service.ShoppingMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Shpping Item API", description = "쇼핑 상품 관리 API")     // Swagger 컨트롤러 단위 설명
@Controller
public class ShoppingItemController {
    @Operation(summary = "로그인", description = "로그인 이동")     // Swagger API 메서드 단위 설명
    @Parameter(name = "", description = "void")
    @GetMapping("/admin/shoppingItem/new")
    public String itemForm() {
        return "/shoppingItem/itemForm";
    }

}