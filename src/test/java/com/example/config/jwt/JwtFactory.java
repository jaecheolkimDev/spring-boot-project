package com.example.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@AutoConfigureMockMvc   // MockMvc 테스트 설정
@Getter
public class JwtFactory {
    /** 테스트 패키지 경로 일치 */
    /** 파일/리소스 테스트: src/test/resources 폴더에 파일을 넣고 테스트합니다. */
    /** 반복 테스트 (@ParameterizedTest): 여러 입력값으로 동일한 테스트를 반복할 때 유용합니다. */

    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    // 빌더 패턴을 사용해 설정이 필요한 데이터만 선택 설정
    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    // jjwt 라이브러리를 사용해 JWT 토큰 생성
    public String createToken(JwtProperties jwtProperties) {
        // 0.12.x 버전에서는 SecretKey 객체를 생성해서 넣어줘야 안전합니다.
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .header().add("typ", "JWT").and()   // 헤더 type : JWT
                // 내용 iss : ajufresh@gmail.com(properties 파일에서 설정한 값)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(issuedAt)   // 내용 iat : 현재 시간
                .expiration(expiration)  // 내용 exp : expiry 멤버 변숫값
                .subject(subject)    // 내용 sub : 유저의 이메일
                .claim("id", claims)  // 클레임 id : 유저 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(key)
                .compact();
    }

    @BeforeEach
    void setup() {
        System.out.println("테스트 시작 전 준비");
    }

    @AfterEach
    void cleanup() {
        System.out.println("테스트 종료 후 정리");
    }
}
