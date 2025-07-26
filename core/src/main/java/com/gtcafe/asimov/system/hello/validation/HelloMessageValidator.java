package com.gtcafe.asimov.system.hello.validation;

import java.util.Arrays;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 訊息驗證器
 * 實作 Hello 訊息的業務驗證邏輯
 */
@Slf4j
public class HelloMessageValidator implements ConstraintValidator<ValidHelloMessage, String> {

    private int minLength;
    private int maxLength;
    private boolean allowSpecialChars;
    private boolean allowNumbers;
    private String[] forbiddenWords;

    // 特殊字符的正則表達式
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s]");
    
    // 數字的正則表達式
    private static final Pattern NUMBERS_PATTERN = Pattern.compile("\\d");

    @Override
    public void initialize(ValidHelloMessage constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.allowSpecialChars = constraintAnnotation.allowSpecialChars();
        this.allowNumbers = constraintAnnotation.allowNumbers();
        this.forbiddenWords = constraintAnnotation.forbiddenWords();
        
        log.debug("Initialized HelloMessageValidator with minLength={}, maxLength={}, " +
                "allowSpecialChars={}, allowNumbers={}, forbiddenWords={}", 
                minLength, maxLength, allowSpecialChars, allowNumbers, Arrays.toString(forbiddenWords));
    }

    @Override
    public boolean isValid(String message, ConstraintValidatorContext context) {
        if (message == null) {
            addConstraintViolation(context, "Message cannot be null");
            return false;
        }

        // 去除前後空白後檢查
        String trimmedMessage = message.trim();
        
        // 檢查長度
        if (!isLengthValid(trimmedMessage, context)) {
            return false;
        }

        // 檢查是否為空白字符串
        if (trimmedMessage.isEmpty()) {
            addConstraintViolation(context, "Message cannot be empty or contain only whitespace");
            return false;
        }

        // 檢查特殊字符
        if (!allowSpecialChars && containsSpecialChars(trimmedMessage)) {
            addConstraintViolation(context, "Message cannot contain special characters");
            return false;
        }

        // 檢查數字
        if (!allowNumbers && containsNumbers(trimmedMessage)) {
            addConstraintViolation(context, "Message cannot contain numbers");
            return false;
        }

        // 檢查禁用關鍵字
        if (!isForbiddenWordsValid(trimmedMessage, context)) {
            return false;
        }

        // 檢查業務規則
        if (!isBusinessRuleValid(trimmedMessage, context)) {
            return false;
        }

        log.debug("Message validation passed for: {}", message);
        return true;
    }

    /**
     * 檢查長度是否有效
     */
    private boolean isLengthValid(String message, ConstraintValidatorContext context) {
        if (message.length() < minLength) {
            addConstraintViolation(context, 
                String.format("Message length must be at least %d characters", minLength));
            return false;
        }

        if (message.length() > maxLength) {
            addConstraintViolation(context, 
                String.format("Message length must not exceed %d characters", maxLength));
            return false;
        }

        return true;
    }

    /**
     * 檢查是否包含特殊字符
     */
    private boolean containsSpecialChars(String message) {
        return SPECIAL_CHARS_PATTERN.matcher(message).find();
    }

    /**
     * 檢查是否包含數字
     */
    private boolean containsNumbers(String message) {
        return NUMBERS_PATTERN.matcher(message).find();
    }

    /**
     * 檢查禁用關鍵字
     */
    private boolean isForbiddenWordsValid(String message, ConstraintValidatorContext context) {
        if (forbiddenWords == null || forbiddenWords.length == 0) {
            return true;
        }

        String lowerCaseMessage = message.toLowerCase();
        for (String forbiddenWord : forbiddenWords) {
            if (lowerCaseMessage.contains(forbiddenWord.toLowerCase())) {
                addConstraintViolation(context, 
                    String.format("Message cannot contain forbidden word: %s", forbiddenWord));
                return false;
            }
        }

        return true;
    }

    /**
     * 檢查業務規則
     */
    private boolean isBusinessRuleValid(String message, ConstraintValidatorContext context) {
        // 不能全部是相同字符
        if (isAllSameCharacter(message)) {
            addConstraintViolation(context, "Message cannot consist of the same character repeated");
            return false;
        }

        // 不能全部是大寫 (超過一定長度時)
        if (message.length() > 10 && message.equals(message.toUpperCase()) && 
            !message.equals(message.toLowerCase())) {
            addConstraintViolation(context, "Long messages should not be all uppercase");
            return false;
        }

        return true;
    }

    /**
     * 檢查是否全部是相同字符
     */
    private boolean isAllSameCharacter(String message) {
        if (message.length() <= 1) {
            return false;
        }

        char firstChar = message.charAt(0);
        for (int i = 1; i < message.length(); i++) {
            if (message.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 添加約束違反訊息
     */
    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        log.debug("Validation failed: {}", message);
    }
}