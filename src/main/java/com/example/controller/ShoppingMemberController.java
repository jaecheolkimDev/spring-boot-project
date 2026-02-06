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

@Tag(name = "Shpping Member API", description = "쇼핑 사용자 관리 API")     // Swagger 컨트롤러 단위 설명
@Controller
public class ShoppingMemberController {
    @Autowired
    private ShoppingMemberService shoppingMemberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "GET 멤버 정보 생성", description = "GET 멤버 정보를 생성합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "Model", description = "org.springframework.ui.Model")
    @GetMapping("/shoppingMembers/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "shoppingMember/memberForm";
    }

    @Operation(summary = "POST 회원가입", description = "POST 멤버 정보를 생성합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "Model", description = "org.springframework.ui.Model")
    @PostMapping("/shoppingMembers/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "shoppingMember/memberForm";
        }

        try {
            ShoppingMemberEntity member = ShoppingMemberEntity.createMember(memberFormDto, passwordEncoder);
            shoppingMemberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "shoppingMember/memberForm";
        }
        return "redirect:/shoppingMembers/login";
//        return "redirect:/main";
    }

    @Operation(summary = "로그인", description = "메뉴 > 로그인 버튼 눌렀을때")     // Swagger API 메서드 단위 설명
    @Parameter(name = "", description = "void")
    @GetMapping("/shoppingMembers/login")
    public String loginMember() {
        return "shoppingMember/memberLoginForm";
    }

    @Operation(summary = "로그인 에러", description = "로그인 에러")     // Swagger API 메서드 단위 설명
    @Parameter(name = "Model", description = "Model")
    @GetMapping("/shoppingMembers/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "shoppingMember/memberLoginForm";
    }
}