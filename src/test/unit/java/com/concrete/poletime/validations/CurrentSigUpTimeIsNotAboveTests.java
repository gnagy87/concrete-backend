package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.utils.dateservice.DateService;
import com.concrete.poletime.utils.dateservice.DateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class CurrentSigUpTimeIsNotAboveTests {

  private ValidationService validationService;
  private DateService dateService;

  private Long trainingFrom;
  private Long signUpAttempt;

  private Long oneDay;

  @Before
  public void setup() throws DateConversionException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.trainingFrom = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() + 2*oneDay).toString() + " 15:00").getTime();
    this.signUpAttempt = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 15:00").getTime();
  }

  @Test
  public void whenSignUpTimeIsNotAbove_thenSuccess() throws ValidationException, DateConversionException {
    validationService.currentSigUpTimeIsNotAbove(trainingFrom, signUpAttempt);
    signUpAttempt = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() - 4*oneDay).toString() + " 15:00"
    ).getTime();
    validationService.currentSigUpTimeIsNotAbove(trainingFrom, signUpAttempt);
  }

  @Test
  public void whenSignUpValidationFails_thenException() throws DateConversionException {
    signUpAttempt = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() - 7*oneDay).toString() + " 14:59").getTime();
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.currentSigUpTimeIsNotAbove(trainingFrom, signUpAttempt));
    Assert.assertTrue(e.getMessage().contains("Invalid sign up attempt"));
  }
}
