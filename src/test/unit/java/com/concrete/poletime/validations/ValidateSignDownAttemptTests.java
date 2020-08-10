package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.training.Training;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
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
import java.text.ParseException;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class ValidateSignDownAttemptTests {

  private ValidationService validationService;
  private DateService dateService;

  private Training training;
  private PoleUser user;

  private Long oneDay;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.training = new Training(
        dateService.trainingDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime() + 2 * oneDay).toString() + " 15:00"),
        dateService.trainingDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime() + 2 * oneDay).toString() + " 16:00"),
        TrainingHall.A, 10, TrainingType.GROUP, TrainingLevel.EXOTIC, 1L
    );
    this.user = new PoleUser(
        "bagyondaniel@gmail.com",
        "danel",
        "bagyon",
        "aA@123456"
    );
    this.user.setEnabled(true);
  }

  @Test
  public void whenValidateSignDownAttempt_thenSuccess() throws ValidationException {
    this.training.getPoleUsers().add(this.user);
    validationService.validateSignDownAttempt(training, user, 1);
    this.training.getPoleUsers().remove(this.user);
  }

  @Test
  public void whenValidationFailsOnParticipation_thenException() throws ValidationException {
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateSignDownAttempt(training, user, 1));
    Assert.assertTrue(e.getMessage().contains("User is not participate"));
  }

  @Test
  public void whenValidationFailsOn24HrsLimit_thenException() throws DateConversionException {
    this.training.getPoleUsers().add(this.user);
    this.training.setTrainingFrom(
        dateService.trainingDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime()).toString() + " 15:00")
    );
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateSignDownAttempt(training, user, 1));
    Assert.assertTrue(e.getMessage().contains("24hrs before training"));
  }
}
