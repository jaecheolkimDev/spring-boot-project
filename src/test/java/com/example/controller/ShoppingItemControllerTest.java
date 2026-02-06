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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@ActiveProfiles("test") // application-test.yml을 선택함
@AutoConfigureMockMvc   // MockMvc 테스트 설정
public class ShoppingItemControllerTest {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    // 현재 회원의 이름이 admin 이고, role이 ADMIN인 유저가 로그인된 상태로 테스트를 할 수 있도록 해주는 어노테이션입니다.
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shoppingItemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/shoppingItem/new"))  // get요청을 보냅니다.
                .andDo(print())                 // 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력해줍니다.
                .andExpect(status().isOk());    // 응답 상태 코드가 정상인지 확인합니다.
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "user", roles = "USER")    // 현재 인증된 사용자의 Role을 USER로 세팅합니다.
    public void shoppingItemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/shoppingItem/new"))  // get요청을 보냅니다.
                .andDo(print())                     // 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력해줍니다.
                .andExpect(status().isForbidden()); // 상품 등록 페이지 진입 요청 시 Forbidden 예외가 발생하면 테스트가 성공적으로 통과합니다.
    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
