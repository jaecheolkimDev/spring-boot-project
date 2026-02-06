package com.example.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import javax.naming.ServiceUnavailableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 잘못된 요청일 때 (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // 2. 서버 내부 오류 혹은 외부 서비스 불가능일 때 (503 Service Unavailable)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleServiceUnavailable(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("서비스 점검 중입니다.");
    }

//    // 3. 그 외 모든 예외 (500 Internal Server Error)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleAll(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 에러가 발생했습니다.");
//    }
}