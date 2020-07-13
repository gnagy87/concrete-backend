package com.concrete.poletime.exceptions;

public class DateConversionException extends Exception {
  public DateConversionException(String message) {
    super(message);
  }

  public DateConversionException(String message, Throwable throwable) {
    super(message);
  }
}
