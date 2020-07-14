package com.concrete.poletime.exceptions;

public class TrainingTypeException extends Exception {
  public TrainingTypeException(String message) {
    super(message);
  }

  public TrainingTypeException(String message, Throwable cause) {
    super(message, cause);
  }
}