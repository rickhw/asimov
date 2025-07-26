package com.gtcafe.asimov.system.hello.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * HelloMessageValidator 單元測試
 * 測試自訂驗證註解的各種情境
 */
class HelloMessageValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validMessage_ShouldPass() {
        TestObject obj = new TestObject();
        obj.message = "Hello, World!";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = null;

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void emptyMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whitespaceOnlyMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "   ";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void tooShortMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = ""; // 長度為 0，小於最小長度 1

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void tooLongMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "a".repeat(501); // 超過最大長度 500

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void messageWithForbiddenWord_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "This is spam content";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void messageWithAllSameCharacter_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "aaaaaaaaaa";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void messageWithSpecialChars_ShouldPassByDefault() {
        TestObject obj = new TestObject();
        obj.message = "Hello! @#$%";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    @Test
    void messageWithNumbers_ShouldPassByDefault() {
        TestObject obj = new TestObject();
        obj.message = "Hello 123";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    @Test
    void longUppercaseMessage_ShouldFail() {
        TestObject obj = new TestObject();
        obj.message = "THIS IS A VERY LONG UPPERCASE MESSAGE";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
    }

    @Test
    void normalMixedCaseMessage_ShouldPass() {
        TestObject obj = new TestObject();
        obj.message = "This is a Normal Message";

        Set<ConstraintViolation<TestObject>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    // 測試用的內部類別
    private static class TestObject {
        @ValidHelloMessage(
            minLength = 1,
            maxLength = 500,
            allowSpecialChars = true,
            allowNumbers = true,
            forbiddenWords = {"spam", "test123", "admin"}
        )
        public String message;
    }
}