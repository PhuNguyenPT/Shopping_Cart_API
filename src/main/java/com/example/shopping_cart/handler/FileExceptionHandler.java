package com.example.shopping_cart.handler;

import org.apache.juli.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class FileExceptionHandler {

//    public ResponseStatusException handleUploadFileException(RuntimeException e, String originalFilename) {
//        LogFactory.getLog(e.getClass()).error(e.getMessage(),e.getCause());
//        return new ResponseStatusException(
//                HttpStatus.BAD_REQUEST,
//                e.getMessage()
//        );
//    }
//
//
//
//    public ResponseStatusException handleFindByNameException(String fileName) {
//        return new ResponseStatusException(
//                HttpStatus.NOT_FOUND,
//                "File with name " + fileName + " not found."
//        );
//    }
//
//    public ResponseStatusException handleDeleteDataException(String fileName) {
//        return new ResponseStatusException(
//                HttpStatus.NOT_FOUND,
//                "Cannot delete file " + fileName
//        );
//    }
//
//    public ResponseStatusException handleBadCredentialsException() {
//        return new ResponseStatusException(
//                HttpStatus.UNAUTHORIZED,
//                "Username or password is invalid"
//        );
//    }
}
