package com.probodia.userservice.api.exception;

import com.probodia.userservice.api.annotation.BasicError;
import com.probodia.userservice.api.annotation.ValidationError;
import com.probodia.userservice.common.ErrorResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExControllerAdvice implements ErrorController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    @BasicError
    public ErrorResult userNotFoundException(UsernameNotFoundException e) {
        return new ErrorResult("Not found User", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenAuthException.class)
    @BasicError
    public ErrorResult tokenAuthException(TokenAuthException e){
        return new ErrorResult("Token Authentication Failed..",e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    @BasicError
    public ErrorResult notFoundException(NoSuchElementException e) {
        return new ErrorResult("Not found Element", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    @BasicError
    public ErrorResult unAuthorizedTokenception(UnAuthorizedException e){
        return new ErrorResult("Unauthorized", e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ValidationError
    public ErrorResult handleValidationExceptions(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(c -> errors.put(((FieldError)c).getField() , c.getDefaultMessage()));

        return new ErrorResult("Request is not valid", errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @BasicError
    public ErrorResult handleIllegalArgsException(IllegalArgumentException e){
        return new ErrorResult("Illegal Argument. Try again.", e.getMessage());
    }

}
