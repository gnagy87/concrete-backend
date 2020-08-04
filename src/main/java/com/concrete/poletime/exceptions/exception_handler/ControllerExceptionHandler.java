package com.concrete.poletime.exceptions.exception_handler;

import com.concrete.poletime.error.StatusMessageDTO;
import com.concrete.poletime.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

import javax.security.auth.login.LoginException;
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
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
    //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
    //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
    //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex);
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

  @ExceptionHandler({TrainingIsHeldUnsettableException.class})
  public ResponseEntity<Object> handleTrainingIsHeldUnsettableException(TrainingIsHeldUnsettableException ex,
                                                                        WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.PRECONDITION_FAILED,
        request
    );
  }

  @ExceptionHandler({CannotLoadDataFromDbException.class})
  public ResponseEntity<Object> handleCannotLoadDataFromDbException(CannotLoadDataFromDbException ex,
                                                                    WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request
    );
  }

  @ExceptionHandler({TrainingTypeException.class})
  public ResponseEntity<Object> handleTrainingTpeException(TrainingTypeException ex,
                                                           WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request
    );
  }

  @ExceptionHandler({RegistrationException.class})
  public ResponseEntity<Object> handleRegistrationException(RegistrationException ex,
                                                           WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }


  @ExceptionHandler({ConfirmationException.class})
  public ResponseEntity<Object> handleConfirmationException(ConfirmationException ex,
                                                            WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }

  @ExceptionHandler({RecordNotFoundException.class})
  public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex,
                                                            WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request
    );
  }

  @ExceptionHandler({LoginException.class})
  public ResponseEntity<Object> handleLoginException(LoginException ex,
                                                              WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex,
                                                     WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.FORBIDDEN,
        request
    );
  }

  @ExceptionHandler({TrainingException.class})
  public ResponseEntity<Object> handleTrainingException(TrainingException ex,
                                                            WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }

  @ExceptionHandler({SeasonTicketException.class})
  public ResponseEntity<Object> handleSeasonTicketException(SeasonTicketException ex,
                                                        WebRequest request) {
    return handleExceptionInternal(
        ex,
        new StatusMessageDTO(ex.getLocalizedMessage(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }
}
