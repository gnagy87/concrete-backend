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
public class NameValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private final String regex = "[a-zA-Z]{2,32}";

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }

  @Test
  public void whenValidateName_thenSuccess() {
    Assert.assertTrue("Daniel".matches(regex));
    Assert.assertTrue("DanieL".matches(regex));
    Assert.assertTrue("daniel".matches(regex));
    Assert.assertTrue("bd".matches(regex));
    Assert.assertTrue("danielmichaelbagyonn".matches(regex));
  }

  @Test
  public void whenNameValidationFails_thenException() {
    List<String> names = Arrays.asList(
        "d", "d1", "123", "d_d", "moredthanthirtytwoanielmichaelbagyon", "Daniel_", "_", "daniel bagyon"
    );

    for (String name : names) {
      Assert.assertThrows(ValidationException.class,
          () -> validationService.nameValidation(name));
    }
  }

}
