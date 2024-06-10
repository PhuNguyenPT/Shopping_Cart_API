package com.example.shopping_cart.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse
{
    private Integer businessErrorCode;
    private String businessErrorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    @Getter
    @AllArgsConstructor
    public enum BusinessErrorCode {
        NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
        INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
        NEW_PASSWORD_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "New password does not match"),
        ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
        ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
        BAD_CREDENTIAL(304, HttpStatus.FORBIDDEN, "Login username and/ or password is incorrect");

        private final int code;
        private final HttpStatus httpStatus;
        private final String description;
    }
}
