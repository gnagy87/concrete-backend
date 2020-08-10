package com.concrete.poletime.utils.timeservice;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.utils.dateservice.DateService;
import com.concrete.poletime.utils.dateservice.DateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class TrainingTimeCalculatorTests {

  private TimeService timeService;
  private DateService dateService;

  private Long oneDay;

  private Date trainingFrom;
  private Date trainingTo;

  @Before
  public void setup() throws DateConversionException {
    this.timeService = new TimeServiceImpl();
    this.dateService = new DateServiceImpl();
    this.oneDay = this.oneDay = 24 * 60 * 60 * 1000L;
    this.trainingFrom = dateService.trainingDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 15:00");
    this.trainingTo = dateService.trainingDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 16:00");
  }

  @Test
  public void whenCalculateTrainingTime_thenSuccess() throws TrainingException {
    long result = timeService.trainingTimeCalculator(trainingFrom,trainingTo);
    Assert.assertTrue(result > 0);
  }

  @Test
  public void whenCalculationFailsOnTrainingTo_thenException() throws DateConversionException {
    this.trainingTo = dateService.trainingDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 14:00");
    Exception e = Assert.assertThrows(TrainingException.class,
        () -> timeService.trainingTimeCalculator(trainingFrom, trainingTo));
    Assert.assertTrue(e.getMessage().contains("trainingTo can not be smaller"));
  }

}
