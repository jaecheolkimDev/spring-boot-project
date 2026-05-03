package com.example.config.jwt;

import com.example.entity.ShoppingMemberEntity;
import com.example.repository.ShoppingMemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@AutoConfigureMockMvc   // MockMvc 테스트 설정
public class TokenProviderTest {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ShoppingMemberRepository shoppingMemberRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    @Test
    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    public void generateToken() {
        // given
        ShoppingMemberEntity testUser = shoppingMemberRepository.save(
                ShoppingMemberEntity.builder().email("user@gmail.com").password("test").build()
        );

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // 0.12.x 버전에서는 SecretKey 객체를 생성해서 넣어줘야 안전합니다.
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        // then
        Long userId = Jwts.parser()
                .verifyWith(key)            // 서명 검증 키 설정 - setSigningKey 대신 verifyWith 사용
                .build()                    // 파서 빌드(파싱할 준비가 된 파서 객체를 만들어야 합니다.)
                .parseSignedClaims(token)   // 토큰 파싱 - parseClaimsJws 대신 parseSignedClaims 사용
                .getPayload()               // 페이로드(Claims) 획득 - 기존 getBody() 대신 getPayload() 사용
                .get("id", Long.class);  // "id" 클레임을 Long 타입으로 가져오기

        assertEquals(userId, testUser.getId());
    }

    @Test
    @DisplayName("validToken() : 만료된 토큰인 때에 유효성 검증에 실패한다.")
    public void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertEquals(result, false);
    }

    @Test
    @DisplayName("validToken() : 유효한 토큰인 때에 유효성 검증에 성공한다.")
    public void validToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertEquals(result, true);
    }

    @Test
    @DisplayName("getAuthentication() : 토큰 기반으로 인증 정보를 가져올 수 있다.")
    public void getAuthentication() {
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertEquals(((UserDetails)authentication.getPrincipal()).getUsername(), userEmail);
    }

    @Test
    @DisplayName("getUserId() : 토큰으로 유저 ID를 가져올 수 있다.")
    public void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertEquals(userIdByToken, userId);
    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
