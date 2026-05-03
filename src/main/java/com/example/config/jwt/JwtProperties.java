package com.example.config.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")    // 자바 클래스에 프로퍼티값을 가져와서 사용하는 애너테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
