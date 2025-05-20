package com.example.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLNonTransientConnectionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLNonTransientConnectionException.class)
    public ResponseEntity<String> handleDatabaseConnectionException(SQLNonTransientConnectionException ex) {
        // 可以记录日志、发送告警等
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("数据库连接失败，请检查网络或配置: " + ex.getMessage());
    }

    // 可扩展其他异常处理...
}
