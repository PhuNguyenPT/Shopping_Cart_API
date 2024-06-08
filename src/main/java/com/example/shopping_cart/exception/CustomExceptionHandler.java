package com.example.shopping_cart.exception;

import org.apache.juli.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class CustomExceptionHandler {

    public ResponseStatusException handleUploadFileException(RuntimeException e, String originalFilename) {
        LogFactory.getLog(e.getClass()).error(e.getMessage(),e.getCause());
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Cannot upload file " + originalFilename + " because it has a duplicated name or already exists."
        );
    }

    public ResponseStatusException handleFindByNameException(String fileName) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "File with name " + fileName + " not found."
        );
    }

    public ResponseStatusException handleDeleteDataException(String fileName) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cannot delete file " + fileName
        );
    }

    public ResponseStatusException handleBadCredentialsException() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Username or password is invalid"
        );
    }

}
