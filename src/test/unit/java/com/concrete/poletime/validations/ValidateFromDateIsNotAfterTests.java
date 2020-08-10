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

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class ValidateFromDateIsNotAfterTests {

  private ValidationService validationService;
  private DateService dateService;

  private Date fromDate;
  private Date toDate;
  private Long oneDay;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.fromDate = dateService.trainingDateParser(new java.sql.Date(
        new Timestamp(System.currentTimeMillis()).getTime() + 2 * oneDay).toString() + " 15:00");
    this.toDate = dateService.trainingDateParser(new java.sql.Date(
        new Timestamp(System.currentTimeMillis()).getTime() + 2 * oneDay).toString() + " 16:00");
  }

  @Test
  public void whenValidateFromDateIsNotAfter_thenSuccess() throws ValidationException {
    validationService.validateFromDateIsNotAfter(fromDate, toDate);
  }

  @Test
  public void whenValidationFailsOnFromDate_thenException() throws DateConversionException {
    this.fromDate = dateService.trainingDateParser(new java.sql.Date(
        new Timestamp(System.currentTimeMillis()).getTime() + 2 * oneDay).toString() + " 17:00");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateFromDateIsNotAfter(fromDate, toDate));
    Assert.assertTrue(e.getMessage().contains("Invalid interval of dates"));
  }
}
