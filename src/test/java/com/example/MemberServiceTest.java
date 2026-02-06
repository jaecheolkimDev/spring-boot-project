package com.example;

import com.example.entity.MemberEntity;
import com.example.repository.MemberRepository;
import com.example.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // 스프링 부트 테스트 설정, @SpringBootApplication이 있는 패키지에서 컴포넌트를 스캔합니다.
public class MemberServiceTest {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    @ParameterizedTest  /** 파라미터화된 테스트 */
    @ValueSource(ints = {1, 2, 3, 4, 5}) // {1, 2, 3, 4, 5} 값을 순서대로 input 변수에 전달
    void testIsPositive(int input) {
        assertTrue(input > 0);
    }
    @Test
    public void saveUserTest() {
        // 테스트 코드 작성
        MemberEntity user1 = memberService.saveUser("testUser", "testPassword");

        Optional<MemberEntity> user2 = memberRepository.findById(user1.getId());
        Assertions.assertEquals(user2.get().getName(), "testUser");
        Assertions.assertEquals(user2.get().getPassword(), "testPassword");

    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
