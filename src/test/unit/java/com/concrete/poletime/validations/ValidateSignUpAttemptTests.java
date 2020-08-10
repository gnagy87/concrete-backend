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
public class ValidateSignUpAttemptTests {

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
  public void whenValidateSignUpAttempt_thenSuccess() throws ValidationException {
    validationService.validateSignUpAttempt(training, user, 1);
  }

  @Test
  public void whenValidationFailsOnTrainingLimit_thenException() {
    training.setPersonLimit(0);
    user.setId(2L);
    user.setEmail("bd@gmail.com");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateSignUpAttempt(training, user, 1));
    Assert.assertTrue(e.getMessage().contains("Limit has already been reached"));
  }

  @Test
  public void whenValidationFailsOnUserAlreadySignedUp_thenException() {
    training.setPersonLimit(10);
    training.getPoleUsers().add(this.user);
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateSignUpAttempt(training, user, 1));
    Assert.assertTrue(e.getMessage().contains("User already participate"));
  }

}
