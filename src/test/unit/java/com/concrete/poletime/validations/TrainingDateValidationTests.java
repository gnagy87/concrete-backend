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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class TrainingDateValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private String date = new Date(new Timestamp(System.currentTimeMillis()).getTime()).toString() + " 15:15";
  private final String regex = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):(00|15|30|45)";

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }

  @Test
  public void whenDateIsValid_thenSuccess() throws ValidationException {
    Assert.assertTrue(date.matches(regex));
    validationService.trainingDateValidator(date);
  }

  @Test
  public void whenDateValidationFails_thenException() {
    List<String> dates = Arrays.asList(
        "2020-08-07 15:16",
        "2020-08-07 15:31",
        "2020-08-07 15:46",
        "2020-08-07 16:01",
        "2020-08-07",
        "2020-08-07 15_16",
        "2020.08.07 15:16",
        "2020.08.07 15.16",
        "20200807 15:16"
    );
    for (String date : dates) {
      Exception e = Assert.assertThrows(ValidationException.class,
          () -> validationService.trainingDateValidator(date));
      Assert.assertTrue(e.getMessage().contains("Date is not accepted"));
    }
  }



}
