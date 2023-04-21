package com.skyapi.weatherforescast;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(request.getServletPath());

        LOGGER.error(ex.getMessage(), ex);

        return error;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        LOGGER.error(ex.getMessage(), ex);

        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        List<ObjectError> fieldErrors = ex.getAllErrors();

        fieldErrors.forEach(fieldError -> {
            error.addError(fieldError.getDefaultMessage());
        });

        return new ResponseEntity<>(error, headers, status);
    }


}
