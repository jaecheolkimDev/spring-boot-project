package com.example.controller;

import com.example.dto.ItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Tag(name = "Thymeleaf Ex API", description = "Thymeleaf Ex 관리 API")     // Swagger 컨트롤러 단위 설명
@Controller
public class ThymeleafExController {

    @Operation(summary = "http://localhost:8080/thmeleaf/ex01", description = "타임리프 예제 입니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "Model model", description = "org.springframework.ui.Model")
    @GetMapping(value = "/thmeleaf/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @Operation(summary = "http://localhost:8080/thmeleaf/ex02", description = "hello페이지로 이동합니다.")     // Swagger API 메서드 단위 설명
    @Parameter(name = "Model model", description = "org.springframework.ui.Model ")
    @GetMapping(value = "/thmeleaf/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }
}
