package com.jhuguet.sb_taskv1.app.handler;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.ExceptionDetails;
import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingUserFields;
import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.exceptions.NoTagInOrder;
import com.jhuguet.sb_taskv1.app.exceptions.OrderNotRelated;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
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

    @ExceptionHandler(OrderNotRelated.class)
    public ResponseEntity<?> orderNotRelated(OrderNotRelated exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IdAlreadyInUse.class)
    public ResponseEntity<?> idAlreadyInUse(IdAlreadyInUse exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoExistingOrders.class)
    public ResponseEntity<?> noExistingOrders(NoExistingOrders exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoTagInOrder.class)
    public ResponseEntity<?> noTagInOrder(NoTagInOrder exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageNotFound.class)
    public ResponseEntity<?> pageNotFound(PageNotFound exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingUserFields.class)
    public ResponseEntity<?> missingUserFields(MissingUserFields exception) {
        ExceptionDetails details = new ExceptionDetails(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

}
