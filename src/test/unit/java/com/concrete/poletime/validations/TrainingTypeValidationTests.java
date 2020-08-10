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
public class TrainingTypeValidationTests {

  private ValidationService validationService;
  private DateService dateService;

  private List<String> types;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.types = Arrays.asList("GROUP", "PRIVATE", "PRACTICE_TRAINER", "PRACTICE_GUEST", "party");
  }

  @Test
  public void whenTypeIsValid_thenSuccess() {
    for (String type : types) {
      Assert.assertTrue(validationService.trainingTypeValidator(type));
    }
  }

  @Test
  public void whenTypeIsNotValid_thenFail() {
    types = Arrays.asList("vmi", "MÉG_VMI", "vmi_Más");
    for (String type : types) {
      Assert.assertFalse(validationService.trainingTypeValidator(type));
    }
  }



}
