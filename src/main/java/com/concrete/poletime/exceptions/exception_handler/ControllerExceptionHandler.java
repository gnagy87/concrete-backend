package com.concrete.poletime.exceptions.exception_handler;

import com.concrete.poletime.error.StatusMessageDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.NoTrainingRepresentedException;
import com.concrete.poletime.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    String message = "Missing parameter(s): " + ex.getBindingResult().getAllErrors().stream()
        .map((error) -> ((FieldError) error).getField())
        .collect(Collectors.joining(", "));
    return ResponseEntity.status(409).body(new StatusMessageDTO(message, ex.getClass().getSimpleName()));
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex,
                                                       HttpHeaders headers,
                                                       HttpStatus status,
                                                       WebRequest request) {

    String message = "Missing parameter(s): " + ex.getBindingResult().getAllErrors().stream()
        .map((error) -> ((FieldError) error).getField())
        .collect(Collectors.joining(", "));
    return ResponseEntity.status(409).body(new StatusMessageDTO(message, ex.getClass().getSimpleName()));
  }

  @ExceptionHandler({DateConversionException.class})
  public ResponseEntity<Object> handleDateConversionException(DateConversionException ex,
                                                              WebRequest request) {
    return handleExceptionInternal(
      ex,
      new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
      new HttpHeaders(),
      HttpStatus.INTERNAL_SERVER_ERROR,
      request
    );
  }

  @ExceptionHandler({NoTrainingRepresentedException.class})
  public ResponseEntity<Object> handleNoTrainingRepresentedException(NoTrainingRepresentedException ex,
                                                                     WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request
    );
  }

  @ExceptionHandler({ValidationException.class})
  public ResponseEntity<Object> handleValidationException(ValidationException ex,
                                                          WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request
    );
  }
}
