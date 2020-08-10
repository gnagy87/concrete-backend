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
public class TrainingLevelValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private List<String> levels;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.levels = Arrays.asList(
        "POLE_MIX", "POLE_BEGINNER", "POLE_BEGINNER_INTERMEDIATE", "POLE_INTERMEDIATE", "POLE_ADVANCED",
        "STRETCHING", "FULL_BODY_TRAINING", "EXOTIC", "FLOORWERK", "TWERK", "FLOW", "workshop"
    );
  }

  @Test
  public void whenTrainingLevelIsValid_thenSuccess() {
    for (String level : levels) {
      Assert.assertTrue(validationService.trainingLevelValidator(level));
    }
  }

  @Test
  public void whenTrainingLevelIsNotValid_thenFail() {
    levels = Arrays.asList("vmi", "barmi", "barmi mas", "A");
    for (String level : levels) {
      Assert.assertFalse(validationService.trainingLevelValidator(level));
    }
  }

}
