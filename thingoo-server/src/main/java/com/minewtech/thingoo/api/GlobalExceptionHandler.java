package com.minewtech.thingoo.api;

import com.minewtech.thingoo.model.response.OperationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/*
@ControllerAdvice tells your spring application that this class will do the exception handling for your application.
@RestController will make it a controller and let this class render the response.
Use @ExceptionHandler annotation to define the class of Exception it will catch. (A Base class will catch all the Inherited and extended classes)
*/
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public OperationResponse handleBaseException(DataIntegrityViolationException e){
        OperationResponse resp = new OperationResponse();
        resp.setStatus(400);
        resp.setMessage(e.getRootCause().getMessage());
        return resp;
    }

}
