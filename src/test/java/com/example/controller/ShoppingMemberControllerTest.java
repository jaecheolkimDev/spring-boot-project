package com.example.controller;

import com.example.dto.MemberFormDto;
import com.example.entity.ShoppingMemberEntity;
import com.example.service.ShoppingMemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@ActiveProfiles("test") // application-test.yml을 선택함
@AutoConfigureMockMvc   // MockMvc 테스트 설정
public class ShoppingMemberControllerTest {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    @Autowired
    private ShoppingMemberService shoppingMemberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    @Test
    @DisplayName("createMember 테스트")
    public ShoppingMemberEntity createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        ShoppingMemberEntity member = ShoppingMemberEntity.createMember(memberFormDto, passwordEncoder);
        return shoppingMemberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        // 이 방식은 Security가 제공하는 기본 설정을 그대로 테스트할 때 편리합니다
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/shoppingMembers/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());  // 인증되었는지 확인
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        // 이 방식은 Security가 제공하는 기본 설정을 그대로 테스트할 때 편리합니다
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/shoppingMembers/login")
                        .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());  // 인증되었는지 확인
    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
