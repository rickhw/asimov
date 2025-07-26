package com.gtcafe.asimov.system.hello.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gtcafe.asimov.system.hello.model.Hello;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 驗證服務
 * 提供 Hello 相關的業務驗證邏輯
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HelloValidationService {

    private final Validator validator;

    /**
     * 驗證 Hello 物件
     * 
     * @param hello 要驗證的 Hello 物件
     * @throws HelloValidationException 當驗證失敗時拋出
     */
    public void validateHello(Hello hello) {
        log.debug("Validating hello object: {}", hello);

        if (hello == null) {
            throw new HelloValidationException("Hello object cannot be null");
        }

        // 使用 Bean Validation 進行基本驗證
        Set<ConstraintViolation<Hello>> violations = validator.validate(hello);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<Hello> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new HelloValidationException(errorMessage.toString());
        }

        // 額外的業務驗證
        validateBusinessRules(hello);

        log.debug("Hello object validation passed");
    }

    /**
     * 驗證 Hello 訊息
     * 
     * @param message 要驗證的訊息
     * @throws HelloValidationException 當驗證失敗時拋出
     */
    public void validateMessage(String message) {
        log.debug("Validating hello message: {}", message);

        if (!StringUtils.hasText(message)) {
            throw new HelloValidationException("Message cannot be null or empty");
        }

        // 建立臨時 Hello 物件進行驗證
        Hello hello = new Hello();
        hello.setMessage(message);
        validateHello(hello);
    }

    /**
     * 驗證事件 ID
     * 
     * @param eventId 要驗證的事件 ID
     * @throws HelloValidationException 當驗證失敗時拋出
     */
    public void validateEventId(String eventId) {
        log.debug("Validating event ID: {}", eventId);

        if (!StringUtils.hasText(eventId)) {
            throw new HelloValidationException("Event ID cannot be null or empty");
        }

        if (eventId.trim().length() < 10) {
            throw new HelloValidationException("Event ID must be at least 10 characters long");
        }

        // 檢查 UUID 格式 (如果使用 UUID)
        if (!isValidUuidFormat(eventId)) {
            throw new HelloValidationException("Event ID must be a valid UUID format");
        }

        log.debug("Event ID validation passed");
    }

    /**
     * 檢查是否為有效的 UUID 格式
     */
    private boolean isValidUuidFormat(String eventId) {
        try {
            // 簡單的 UUID 格式檢查
            String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
            return eventId.matches(uuidPattern);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 驗證業務規則
     */
    private void validateBusinessRules(Hello hello) {
        String message = hello.getMessage();

        // 檢查訊息內容的業務規則
        if (message != null) {
            // 不能包含 HTML 標籤
            if (containsHtmlTags(message)) {
                throw new HelloValidationException("Message cannot contain HTML tags");
            }

            // 不能包含 SQL 注入關鍵字
            if (containsSqlInjectionKeywords(message)) {
                throw new HelloValidationException("Message contains potentially dangerous content");
            }

            // 檢查字符編碼
            if (!isValidEncoding(message)) {
                throw new HelloValidationException("Message contains invalid characters");
            }

            // 檢查重複字符
            if (hasExcessiveRepeatedCharacters(message)) {
                throw new HelloValidationException("Message contains excessive repeated characters");
            }
        }
    }

    /**
     * 檢查是否包含 HTML 標籤
     */
    private boolean containsHtmlTags(String message) {
        return message.matches(".*<[^>]+>.*");
    }

    /**
     * 檢查是否包含 SQL 注入關鍵字
     */
    private boolean containsSqlInjectionKeywords(String message) {
        String lowerCase = message.toLowerCase();
        String[] sqlKeywords = {"select", "insert", "update", "delete", "drop", "union", "script"};
        
        for (String keyword : sqlKeywords) {
            if (lowerCase.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 檢查字符編碼是否有效
     */
    private boolean isValidEncoding(String message) {
        try {
            // 檢查是否包含控制字符 (除了常見的空白字符)
            for (char c : message.toCharArray()) {
                if (Character.isISOControl(c) && c != '\n' && c != '\r' && c != '\t') {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 檢查是否有過多的重複字符
     */
    private boolean hasExcessiveRepeatedCharacters(String message) {
        if (message.length() < 4) {
            return false;
        }

        int maxRepeatedCount = 5; // 允許最多連續 5 個相同字符
        int currentCount = 1;
        char previousChar = message.charAt(0);

        for (int i = 1; i < message.length(); i++) {
            char currentChar = message.charAt(i);
            if (currentChar == previousChar) {
                currentCount++;
                if (currentCount > maxRepeatedCount) {
                    return true;
                }
            } else {
                currentCount = 1;
                previousChar = currentChar;
            }
        }

        return false;
    }

    /**
     * Hello 驗證異常
     */
    public static class HelloValidationException extends RuntimeException {
        public HelloValidationException(String message) {
            super(message);
        }

        public HelloValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}