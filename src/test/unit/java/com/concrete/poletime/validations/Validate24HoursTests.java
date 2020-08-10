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

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class Validate24HoursTests {

  private ValidationService validationService;
  private DateService dateService;

  private long oneDay;
  private Long trainingFrom;
  private Long now;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.trainingFrom = new Timestamp(System.currentTimeMillis()).getTime() + oneDay;
    this.now = new Timestamp(System.currentTimeMillis()).getTime();
  }

  @Test
  public void whenDateIsAboveThan24Hrs_thenSuccess() throws ValidationException {
    validationService.validate24hours(trainingFrom, now);
  }

  @Test
  public void whenDateFailsOnNotAboveThan24Hrs_thenException() throws ValidationException {
    trainingFrom = new Timestamp(System.currentTimeMillis()).getTime() + oneDay/2;
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validate24hours(trainingFrom, now));
    Assert.assertTrue(e.getMessage().contains("Unable to sign down"));
  }


}
