package com.concrete.poletime.validations;

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

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class ValidityDateValidatorTests {

  private ValidationService validationService;
  private DateService dateService;

  private String validityDate;
  private String regex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.validityDate = "2020-08-07";
  }

  @Test
  public void whenDateIsValid_thenSuccess() throws ValidationException {
    Assert.assertTrue(validityDate.matches(regex));
    validationService.validityDateValidator(validityDate);
  }

  @Test
  public void whenDateValidityFails_thenException() {
    validityDate = "20200807";
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validityDateValidator(validityDate));

    Assert.assertTrue(e.getMessage().contains("Validity date is not accepted"));

    validityDate = "2020-08-07 20:20";
    Assert.assertThrows(ValidationException.class, () -> validationService.validityDateValidator(validityDate));

    validityDate = "2020.08.07.";
    Assert.assertThrows(ValidationException.class, () -> validationService.validityDateValidator(validityDate));
  }

}
