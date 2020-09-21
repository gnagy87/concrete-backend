package com.concrete.poletime.exceptions;

public class NoBlogPostRepresentedException extends Exception {
  public NoBlogPostRepresentedException(String message) {
    super(message);
  }

  public NoBlogPostRepresentedException(String message, Throwable cause) {
    super(message, cause);
  }
}
