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
public class EmailValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private final String regex = "[a-z0-9._-]{3,32}@[a-z0-9._-]{2,20}\\.[a-z]{2,3}";

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }
  
  @Test
  public void whenValidateEmail_thenSuccess() {
    Assert.assertTrue("testmail@dummy.com".matches(regex));
    Assert.assertTrue("testail123@dummy.com".matches(regex));
    Assert.assertTrue("test_mail@dummy.com".matches(regex));
    Assert.assertTrue("t35tm0il@dummy.com".matches(regex));
    Assert.assertTrue("testmail@dummy.co".matches(regex));
    Assert.assertTrue("testmail@dummy.vmi.com".matches(regex));
    Assert.assertTrue("testmail@dummy.da.das.com".matches(regex));
    Assert.assertTrue("testmail@dummy_d.com".matches(regex));
    Assert.assertTrue("test.mail@dummy_d.com".matches(regex));
    Assert.assertTrue("test.mail.vmi@dummy_d.com".matches(regex));
  }

  @Test
  public void whenValidationFail_thenException() {
    List<String> wrongAddresses = Arrays.asList(
        "testm@il@dummy.com",
        "TestMail@dummy.com",
        "TestMail@d@mmy.com",
        "testmail@d",
        "testmail@dummy.c",
        "testmail@dummy.",
        "testmail@dummy",
        "testmail@tobb_mint_husz_karakter.com",
        "t@dummy.com",
        "te@dummy.com",
        "test mail@dummy.com",
        "34_tobb_mint_harmincketto_karakter@dummy.com");

    for (String address : wrongAddresses) {
      Assert.assertThrows(ValidationException.class,
          () -> validationService.emailValidation(address));
    }
  }
}
