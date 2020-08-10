package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.TrainingIsHeldUnsettableException;
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
public class TrainingIsHeldTests {

  private ValidationService validationService;
  private DateService dateService;

  private Long oneDay;
  Long trainingTo;
  Long now;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.trainingTo = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() - oneDay).toString() + " 15:00").getTime();
    this.now = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime()).toString() + " 15:00").getTime();
  }

  @Test
  public void whenValidateIsHeld_thenSuccess() throws TrainingIsHeldUnsettableException {
    validationService.doesTrainingIsHeldSettable(trainingTo, now);
  }

  @Test
  public void whenValidationFails_thenException() throws DateConversionException {
    trainingTo = dateService.trainingDateParser(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() + 2*oneDay).toString() + " 15:00").getTime();
    Exception e = Assert.assertThrows(TrainingIsHeldUnsettableException.class,
        () -> validationService.doesTrainingIsHeldSettable(trainingTo, now));
    Assert.assertTrue(e.getMessage().contains("Cannot set training isHeld before training"));
  }


}
