package com.example;

import com.example.dto.MemberFormDto;
import com.example.entity.ShoppingMemberEntity;
import com.example.service.ShoppingMemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@ActiveProfiles("test") // application-test.yml을 선택함
public class ShoppingMemberServiceTest {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    @Autowired
    private ShoppingMemberService shoppingMemberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    public ShoppingMemberEntity createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return ShoppingMemberEntity.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        ShoppingMemberEntity member = createMember();
        ShoppingMemberEntity savedMember = shoppingMemberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        ShoppingMemberEntity member1 = createMember();
        ShoppingMemberEntity member2 = createMember();
        shoppingMemberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            shoppingMemberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
