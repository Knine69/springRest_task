package com.jhuguet.sb_taskv1.app.handler;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.ExceptionDetails;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidInputInformation.class)
    public ResponseEntity<?> invalidInputInformation(InvalidInputInformation exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<?> idNotFound(IdNotFound exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CertificateAssociatedException.class)
    public ResponseEntity<?> certificateAssociated(CertificateAssociatedException exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingEntity.class)
    public ResponseEntity<?> missingEntity(MissingEntity exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

}
