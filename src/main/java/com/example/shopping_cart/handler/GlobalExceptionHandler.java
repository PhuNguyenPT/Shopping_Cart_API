package com.example.shopping_cart.handler;

import com.example.shopping_cart.file.FileProperties;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final FileProperties fileProperties;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(
            @NotNull MethodArgumentTypeMismatchException e
    ) {
        // Create a custom error message
        String errorMessage = "Method argument type mismatch: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleException(
            @NotNull LockedException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ExceptionResponse.BusinessErrorCode.ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ExceptionResponse.BusinessErrorCode.ACCOUNT_LOCKED.getDescription())
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleException(
            @NotNull DisabledException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ExceptionResponse.BusinessErrorCode.ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ExceptionResponse.BusinessErrorCode.ACCOUNT_DISABLED.getDescription())
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(
            @NotNull BadCredentialsException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ExceptionResponse.BusinessErrorCode.BAD_CREDENTIAL.getCode())
                        .businessErrorDescription(ExceptionResponse.BusinessErrorCode.BAD_CREDENTIAL.getDescription())
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handleException(
            @NotNull MessagingException e
    ) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.error("An unexpected error occurred:", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            @NotNull HttpMessageNotReadableException e
    ) {
        // Create a custom error message
        String errorMessage = "Request body is invalid: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleException(
//            @NotNull MethodArgumentNotValidException e
//    ) {
//        Set<String> errors = new HashSet<>();
//        e.getBindingResult().getAllErrors()
//                .forEach(error -> {
//                    var errorMessage = error.getDefaultMessage();
//                    errors.add(errorMessage);
//                });
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(ExceptionResponse.builder()
//                        .validationErrors(errors)
//                        .build()
//                );
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            @NotNull MethodArgumentNotValidException e
    ) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.error("An unexpected error occurred:", e);
        // Create a map to store error messages for each field
        Map<String, String> errorMessages = new HashMap<>();

        // Add error messages for each field
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.put(error.getField(), error.getDefaultMessage()));

        // Return a ResponseEntity with the error messages and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .errors(errorMessages)
                        .build()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleException(
            @NotNull NoResourceFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                        .businessErrorDescription("Page not found")
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            @NotNull Exception e
    ) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.error("An unexpected error occurred:", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                        .businessErrorDescription("Internal errors, please contact admin")
                        .error(e.getMessage())
                        .build()
                );
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(
            @NotNull MissingServletRequestParameterException e
    ) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.error("An unexpected error occurred:", e);

        // Create a custom error message
        Map<String, String> errorMessages = new HashMap<>();

        // Add error messages for each field
        Arrays.stream(Objects.requireNonNull(e.getDetailMessageArguments())).forEach(o ->
                errorMessages.put(o.toString(), e.getMessage())
        );

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .errors(errorMessages)
                        .build()
        );
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handleMissingPathVariableException(
            @NotNull MissingPathVariableException e
    ) {
        // Create a custom error message
        String errorMessage = "Missing path variable: " + e.getVariableName();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(
            @NotNull MissingServletRequestPartException e
    ) {
        // Create a custom error message
        String errorMessage = "Missing request part: " + e.getRequestPartName();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<?> handleServletRequestBindingException(
            @NotNull ServletRequestBindingException e
    ) {
        // Create a custom error message
        String errorMessage = "Request binding error: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<?> handleConversionNotSupportedException(
            @NotNull ConversionNotSupportedException e
    ) {
        // Create a custom error message
        String errorMessage = "Data conversion error: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(
            @NotNull TypeMismatchException e
    ) {
        // Create a custom error message
        String errorMessage = "Type mismatch error: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(
            @NotNull MaxUploadSizeExceededException e
    ) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(
//                        e.getMessage()
                        ExceptionResponse.builder()
                                .businessErrorDescription("File must not exceed " + fileProperties.getMaxFileSize())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerMethodValidationException(
            @NotNull HandlerMethodValidationException e
    ) {
        Map<String, String> errorMessages = new HashMap<>();

        e.getAllValidationResults().forEach(parameterValidationResult ->
            errorMessages.put(parameterValidationResult.getMethodParameter().getParameterName(),
                    parameterValidationResult.getResolvableErrors().get(0).getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .errors(errorMessages)
                        .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(
            @NotNull DataIntegrityViolationException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Data integrity violation")
                                .error(String.valueOf(e.getMostSpecificCause()))
                                .build()
        //                        e.getMessage()
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(@NotNull RuntimeException e) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.error("An unexpected error occurred:", e);

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(
            @NotNull MultipartException e
    ) {
        // Create a custom error message
        String errorMessage = "Error processing the uploaded file: " + e.getMessage();

        // Return a ResponseEntity with the custom error message and HTTP status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription(errorMessage)
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(
            @NotNull EntityNotFoundException e
    ) {
        String errorMessage = "The requested entity was not found: " + e.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription(errorMessage)
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(
            @NotNull EntityExistsException e
    ) {
        String errorMessage = "The requested entity exists: " + e.getMessage();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription(errorMessage)
                        .error(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(
            @NotNull AccessDeniedException e
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            @NotNull IllegalArgumentException e
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            @NotNull HttpRequestMethodNotSupportedException e
    ) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription(e.getMessage())
                                .error(e.getMessage())
                                .build()
                );
    }
}
