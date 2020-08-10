package com.concrete.poletime.validations;

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
public class TrainingHallValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private List<String> halls;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.halls = Arrays.asList("A", "B", "c");
  }

  @Test
  public void whenTrainingHallIsValid_thenSuccess() {
    for (String hall : halls) {
      Assert.assertTrue(validationService.trainingHallValidator(hall));
    }
  }

  @Test
  public void whenHallIsNotValid_thenFail() {
    halls = Arrays.asList("E", "G", "vmi");
    for (String hall : halls) {
      Assert.assertFalse(validationService.trainingHallValidator(hall));
    }
  }

}
