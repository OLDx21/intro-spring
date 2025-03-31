package com.mdemydovych.intro.introspring.exception;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(String message) {
    super(message);
  }

}
