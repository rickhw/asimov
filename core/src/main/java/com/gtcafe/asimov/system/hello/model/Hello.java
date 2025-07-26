package com.gtcafe.asimov.system.hello.model;

import com.gtcafe.asimov.system.hello.validation.ValidHelloMessage;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Hello {

    @NotBlank(message = "Message cannot be blank")
    @ValidHelloMessage(
        minLength = 1,
        maxLength = 500,
        allowSpecialChars = true,
        allowNumbers = true,
        forbiddenWords = {"spam", "test123", "admin"}
    )
    private String message;

}
