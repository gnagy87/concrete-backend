package com.concrete.poletime.utils.dateservice;

import com.concrete.poletime.exceptions.DateConversionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class ConvertTrainingDateToLocalDateTests {

  private DateService dateService;
  private Date trainingFrom;

  @Before
  public void setup() throws DateConversionException {
    this.dateService = new DateServiceImpl();
    this.trainingFrom = dateService.trainingDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime()).toString() + " 15:00");
  }

  @Test
  public void whenConvertTrainingDate_thenSuccess() throws DateConversionException {
    LocalDate convertedDate = dateService.convertTrainingDateToLocalDate(trainingFrom);
    Assert.assertEquals(10, convertedDate.toString().length());
  }

  @Test
  public void whenConvertFails_thenException() {
    this.trainingFrom = null;
    Exception e = Assert.assertThrows(DateConversionException.class,
        () -> dateService.convertTrainingDateToLocalDate(trainingFrom));
    Assert.assertTrue(e.getMessage().contains("Could not parse"));
  }
}
