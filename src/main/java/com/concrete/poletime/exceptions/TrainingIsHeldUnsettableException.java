package com.concrete.poletime.exceptions;

public class TrainingIsHeldUnsettableException extends Exception {
  public TrainingIsHeldUnsettableException(String message) {
    super(message);
  }

  public TrainingIsHeldUnsettableException(String message, Throwable cause) {
    super(message, cause);
  }
}
