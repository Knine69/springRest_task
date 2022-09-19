package com.jhuguet.sb_taskv1.app.handler;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.TagsAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidIdInputInformation.class)
    public ResponseEntity<?> InvalidIdInputInformation(InvalidIdInputInformation exception, WebRequest request) {
        return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<?> idNotFound(IdNotFound exception, WebRequest request) {
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagsAssociatedException.class)
    public ResponseEntity<?> tagsAssociated(TagsAssociatedException exception, WebRequest request) {
        return new ResponseEntity<>(exception, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
