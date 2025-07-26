package com.gtcafe.asimov.system.hello.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Hello 訊息驗證註解
 * 驗證 Hello 訊息的格式和內容是否符合業務規則
 */
@Documented
@Constraint(validatedBy = HelloMessageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHelloMessage {

    String message() default "Invalid hello message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 最小長度 (預設 1)
     */
    int minLength() default 1;

    /**
     * 最大長度 (預設 500)
     */
    int maxLength() default 500;

    /**
     * 是否允許特殊字符 (預設允許)
     */
    boolean allowSpecialChars() default true;

    /**
     * 是否允許數字 (預設允許)
     */
    boolean allowNumbers() default true;

    /**
     * 禁用的關鍵字
     */
    String[] forbiddenWords() default {};
}