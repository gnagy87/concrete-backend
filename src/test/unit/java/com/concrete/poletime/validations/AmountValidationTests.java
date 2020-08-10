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

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class AmountValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  List<Integer> amounts;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.amounts = Arrays.asList(5, 10, 15, 20);
  }

  @Test
  public void whenAmountIsValid_thenSuccess() throws ValidationException {
    for (Integer amount : amounts) {
      validationService.amountValidator(amount);
    }
  }

  @Test
  public void whenAmountValidationFails_thenException() {
    amounts = Arrays.asList(1, 2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14, 16, 17, 18, 19, 21);
    for (Integer amount : amounts) {
      Assert.assertThrows(ValidationException.class, () -> validationService.amountValidator(amount));
    }
  }



}
