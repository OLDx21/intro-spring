package com.mdemydovych.intro.introspring.api.handler;

import com.mdemydovych.intro.introspring.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerExceptionHandler {

  @ExceptionHandler(value = ProductNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<String> userNotFoundException(ProductNotFoundException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }
}
