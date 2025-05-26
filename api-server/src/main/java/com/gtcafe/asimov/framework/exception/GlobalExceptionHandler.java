package com.gtcafe.asimov.framework.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        log.error("400", ex);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    // ------------------------------------------------------------------------
    // 400
    // - **用途**: 當 `@Valid` 注解用於方法參數時，Spring 發現參數無效。
    // - **處理**: 提供詳細的錯誤訊息，如欄位名稱與錯誤內容。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            details.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .details(details)
                .build();
        log.error("400", ex);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 400
    // - **用途**: 當 `@Validated` 注解驗證類別層級的參數違規。
    // - **處理**: 提供詳細的違規資訊。
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });

        log.error("400", ex);
        return ResponseEntity.badRequest().body(errors);
    }

    // 400
    // - **用途**: 當參數不合法時拋出的例外。
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("400", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // ------------------------------------------------------------------------
    // 401
    // - **用途**: 驗證失敗時拋出的基礎例外類別。
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthentication(AuthenticationException ex) {
        log.error("401", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

    // 403
    // - **用途**: 當用戶嘗試存取無權限的資源。
    // - **處理**: 回應 403 狀態碼。
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        log.error("403", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    // 404
    // - **用途**: 當使用 `Optional#get()` 而發現值為空。
    // - **處理**: 回應 404 狀態碼。
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException ex) {
        log.error("404", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    // 404
    // - **用途**: 資源未找到，通常用於查詢操作。
    // - **處理**: 回應 404 狀態碼與自定義訊息。
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("404", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 405: 不支援 HTTP Method
    // - **用途**: 當用戶使用不支援的 HTTP 方法。
    // - **處理**: 回應 405 狀態碼。
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error("405", ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP method not supported");
    }

    // 409
    // - **用途**: 資料庫操作違反唯一性或其他約束。
    // - **處理**: 回應 400 或 409 狀態碼。
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("409", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Database constraint violation");
    }


    // 415: 
    // - **用途**: 當用戶提交不支援的 Content-Type。
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.error("415", ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Media type not supported");
    }

    // ------------------------------------------------------------------------
    // 500
    // - **用途**: 捕獲所有未處理的例外，作為預設的回應策略。
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("500", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}