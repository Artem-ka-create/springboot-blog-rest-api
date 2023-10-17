package com.springboot.blog.exception;

import com.springboot.blog.payload.ErrorDeatils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //handle specific constructions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDeatils> handleResourceFoundFromException(ResourceNotFoundException exception,
                                                                         WebRequest webRequest){

        ErrorDeatils errorDeatils = new ErrorDeatils(
                new Date(),exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDeatils, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BloxAPIException.class)
    public ResponseEntity<ErrorDeatils> handleBloxAPIException(BloxAPIException exception,
                                                                         WebRequest webRequest){

        ErrorDeatils errorDeatils = new ErrorDeatils(
                new Date(),exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDeatils, HttpStatus.BAD_REQUEST);
    }
    //global exceptions

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDeatils> handleGlobalException(Exception exception,
                                                               WebRequest webRequest){

        ErrorDeatils errorDeatils = new ErrorDeatils(
                new Date(),exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDeatils, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName  = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
//                                                                         WebRequest webRequest){
//        Map<String,String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error)-> {
//            String fieldName  = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            errors.put(fieldName,message);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDeatils> handleAccessDeniedException(AccessDeniedException exception,
                                                                         WebRequest webRequest){

        ErrorDeatils errorDeatils = new ErrorDeatils(
                new Date(),exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDeatils, HttpStatus.UNAUTHORIZED);
    }
}
