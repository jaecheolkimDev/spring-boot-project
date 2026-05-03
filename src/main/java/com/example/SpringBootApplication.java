package com.example;

import com.example.config.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplication.class, args);
	}

}
