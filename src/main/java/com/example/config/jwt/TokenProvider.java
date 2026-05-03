package com.example.config.jwt;

import com.example.entity.ShoppingMemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(ShoppingMemberEntity user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, ShoppingMemberEntity user) {
        // 0.12.x 버전에서는 SecretKey 객체를 생성해서 넣어줘야 안전합니다.
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();

        return Jwts.builder()
                .header().add("typ", "JWT").and()   // 헤더 type : JWT
                // 내용 iss : ajufresh@gmail.com(properties 파일에서 설정한 값)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)   // 내용 iat : 현재 시간
                .expiration(expiry)  // 내용 exp : expiry 멤버 변숫값
                .subject(user.getEmail())    // 내용 sub : 유저의 이메일
                .claim("id", user.getId())  // 클레임 id : 유저 ID
        // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(key)
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        // 1. String 비밀값을 SecretKey 객체로 변환
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        // 2. parserBuilder()가 아닌 parser()를 사용 (0.12.x 기준)
        try {
            Jwts.parser()
                    .verifyWith(key)    // setSigningKey 대신 verifyWith 사용
                    .build()    // 빌더를 완성
                    .parseSignedClaims(token);  // parseClaimsJws 대신 parseSignedClaims 사용
            return true;
        } catch(Exception e) {  // 복호화 과정에서 에러(만료, 서명 불일치 등)가 나면 유효하지 않은 토큰
            return false;
        }
    }

    // 3. 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject()
                        , ""
                        , authorities
                ),
                token,
                authorities
        );
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        // 1. 비밀값을 SecretKey 객체로 변환 (UTF-8 인코딩 권장)
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        // 2. 최신 빌더 패턴 적용
        return Jwts.parser()
                .verifyWith(key)        // 서명 검증을 위한 키 설정
                .build()                // 파서 빌드
                .parseSignedClaims(token) // 토큰 파싱 및 검증
                .getPayload();          // 기존 getBody() 대신 getPayload() 사용
    }


























}
