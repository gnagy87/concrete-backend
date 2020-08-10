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

import java.text.ParseException;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class IsTrainingLimitExceededTests {

  private ValidationService validationService;
  private DateService dateService;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }

  @Test
  public void whenValidateTrainingLimit_thenSuccess() throws ValidationException {
    validationService.isTrainingLimitExceeded(10,2);
  }

  @Test
  public void whenTrainingLimitValidationFails_thenException() throws ValidationException {
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.isTrainingLimitExceeded(9,9));
    Assert.assertTrue(e.getMessage().contains("Limit has already been reached"));
  }

}
