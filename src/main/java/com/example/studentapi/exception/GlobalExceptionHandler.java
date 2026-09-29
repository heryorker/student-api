package com.example.studentapi.exception;

import com.example.studentapi.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)//针对所有 Controller 的全局处理类，返回值会转成 JSON。
    public ResponseEntity<ApiResponse<Map<String,String>>> handleValidationException (MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        ApiResponse<Map<String,String>> apiResponse=new ApiResponse<>(400,"参数校验失败",errors);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)//指定某个方法负责处理哪一种异常。
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception){
        String message=exception.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        ApiResponse<Void> apiResponse=new ApiResponse<>(400,message,null);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
