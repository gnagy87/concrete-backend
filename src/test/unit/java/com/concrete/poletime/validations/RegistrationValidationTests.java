package com.concrete.poletime.validations;

import com.concrete.poletime.dto.SetUserParamsDTO;
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
public class RegistrationValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private SetUserParamsDTO params;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.params = new SetUserParamsDTO(
        "bagyondaniel@gmail.com",
        "daniel",
        "bagyon",
        "aA@123456"
    );
  }

  @Test
  public void whenValidateParams_thenSuccess() throws ValidationException {
    Assert.assertTrue(params.getEmail().matches("[a-z0-9._-]{3,32}@[a-z0-9._-]{2,20}\\.[a-z]{2,3}"));
    Assert.assertTrue(params.getFirstName().matches("[a-zA-Z]{2,32}"));
    Assert.assertTrue(params.getLastName().matches("[a-zA-Z]{2,32}"));
    Assert.assertTrue(params.getPassword().matches("^(?=.*[0-9]{2})"
                                                        + "(?=.*[a-z])(?=.*[A-Z])"
                                                        + "(?=.*[@#$%^&+=])"
                                                        + "(?=\\S+$).{8,20}$"));

    validationService.userRegistrationValidator(params);
  }

  @Test
  public void whenParamsValidationFailsOnEmail_thenException() {
    params.setEmail("");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userRegistrationValidator(params));

    String message = e.getMessage();
    Assert.assertTrue(message.contains("Email format is not valid"));
  }

  @Test
  public void whenParamsValidationFailsOnFirstName_thenException() {
    params.setEmail("bagyondaniel@gmail.com");
    params.setFirstName("d@n1el");

    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userRegistrationValidator(params));

    String message = e.getMessage();
    Assert.assertTrue(message.contains("Name is not valid"));
  }

  @Test
  public void whenParamsValidationFailsOnLastName_thenException() {
    params.setFirstName("daniel");
    params.setLastName("b@gy0_");

    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userRegistrationValidator(params));

    String message = e.getMessage();
    Assert.assertTrue(message.contains("Name is not valid"));
  }

  @Test
  public void whenParamsValidationFailsOnPassword_thenException() {
    params.setLastName("bagyon");
    params.setPassword("123456");

    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userRegistrationValidator(params));

    String message = e.getMessage();
    Assert.assertTrue(message.contains("Password is not valid"));
  }

}
