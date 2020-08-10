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
public class PasswordValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private final String regex = "^(?=.*[0-9]{2})"
                              + "(?=.*[a-z])(?=.*[A-Z])"
                              + "(?=.*[@#$%^&+=])"
                              + "(?=\\S+$).{8,20}$";

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }

  @Test
  public void whenValidatePassword_thenSuccess() {
    Assert.assertTrue("aA@123456".matches(regex));
  }

  @Test
  public void whenValidationFail_thenException() {
    List<String> wrongPasswords = Arrays.asList(
        "a", "1", "12345678", "abcdefgh", "ABCDEFGH", "@&#$><€[",
        "aA@1234", "aA@bcdefgh", "1_a_A@bcdefgh"
    );

    for (String address : wrongPasswords) {
      Assert.assertThrows(ValidationException.class,
          () -> validationService.passwordValidation(address));
    }
  }

}
