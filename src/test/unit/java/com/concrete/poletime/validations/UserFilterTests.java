package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
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
public class UserFilterTests {

  private ValidationService validationService;
  private DateService dateService;

  private PoleUser user;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.user = new PoleUser(
        "bagyondaniel@gmail.com",
        "danel",
        "bagyon",
        "aA@123456"
    );
  }

  @Test
  public void whenFailsOnUserIsNotEnabled_thenException() {
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userFilter(user));
    Assert.assertTrue(e.getMessage().contains("User is not acceptable"));
  }

  @Test
  public void whenUserIsEnabled_thenSuccess() throws ValidationException {
    user.setEnabled(true);
    validationService.userFilter(user);
  }

  @Test
  public void whenFailsOnUserIsAdmin_thenException() {
    user.setRole(Role.ADMIN);
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userFilter(user));
    Assert.assertTrue(e.getMessage().contains("User is not acceptable"));
  }

}
