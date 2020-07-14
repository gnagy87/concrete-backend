package com.concrete.poletime.exceptions;

public class CannotLoadDataFromDbException extends Exception {
  public CannotLoadDataFromDbException(String message) {
    super(message);
  }
  public CannotLoadDataFromDbException(String message, Throwable cause) {
    super(message, cause);
  }
}
