package com.gtcafe.asimov.system.hello.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.service.HelloMetricsService;
import com.gtcafe.asimov.system.hello.service.HelloValidationService.HelloValidationException;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.mockito.Mock;

/**
 * HelloValidationService 單元測試
 * 測試驗證服務的各種業務邏輯情境
 */
class HelloValidationServiceTest {

    @Mock
    private HelloMetricsService metricsService;

    private HelloValidationService validationService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validationService = new HelloValidationService(validator, metricsService);
    }

    @Test
    void validateHello_ValidObject_ShouldPass() {
        Hello hello = new Hello();
        hello.setMessage("Hello, World!");

        assertDoesNotThrow(() -> validationService.validateHello(hello));
    }

    @Test
    void validateHello_NullObject_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(null);
        });
    }

    @Test
    void validateHello_EmptyMessage_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("");

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateHello_NullMessage_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage(null);

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateHello_MessageWithHtmlTags_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("Hello <script>alert('xss')</script>");

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateHello_MessageWithSqlInjection_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("Hello'; DROP TABLE users; --");

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateHello_MessageWithExcessiveRepeatedChars_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("Hellooooooo World!");

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateHello_MessageWithForbiddenWord_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("This is spam content");

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }

    @Test
    void validateMessage_ValidMessage_ShouldPass() {
        String message = "Hello, World!";

        assertDoesNotThrow(() -> validationService.validateMessage(message));
    }

    @Test
    void validateMessage_NullMessage_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateMessage(null);
        });
    }

    @Test
    void validateMessage_EmptyMessage_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateMessage("");
        });
    }

    @Test
    void validateEventId_ValidUuid_ShouldPass() {
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";

        assertDoesNotThrow(() -> validationService.validateEventId(validUuid));
    }

    @Test
    void validateEventId_NullEventId_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateEventId(null);
        });
    }

    @Test
    void validateEventId_EmptyEventId_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateEventId("");
        });
    }

    @Test
    void validateEventId_TooShortEventId_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateEventId("short");
        });
    }

    @Test
    void validateEventId_InvalidUuidFormat_ShouldThrowException() {
        assertThrows(HelloValidationException.class, () -> {
            validationService.validateEventId("not-a-valid-uuid-format");
        });
    }

    @Test
    void validateHello_ValidMessageWithSpecialChars_ShouldPass() {
        Hello hello = new Hello();
        hello.setMessage("Hello! How are you? 😊");

        assertDoesNotThrow(() -> validationService.validateHello(hello));
    }

    @Test
    void validateHello_ValidMessageWithNumbers_ShouldPass() {
        Hello hello = new Hello();
        hello.setMessage("Hello 2024!");

        assertDoesNotThrow(() -> validationService.validateHello(hello));
    }

    @Test
    void validateHello_MessageWithControlCharacters_ShouldThrowException() {
        Hello hello = new Hello();
        hello.setMessage("Hello\u0001World"); // 包含控制字符

        assertThrows(HelloValidationException.class, () -> {
            validationService.validateHello(hello);
        });
    }
}