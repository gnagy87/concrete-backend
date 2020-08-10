package com.concrete.poletime.validations;

import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
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
import java.text.ParseException;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class ValidateTrainingParamsTests {

  private ValidationService validationService;
  private DateService dateService;
  private Long oneDay;
  private TrainingParamsDTO paramsDTO;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.paramsDTO = new TrainingParamsDTO(
        new Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 15:00",
        new Date(new Timestamp(System.currentTimeMillis()).getTime() + oneDay).toString() + " 16:15",
        "A", 10, "GROUP", "EXOTIC"
    );
  }

  @Test
  public void whenValidateTrainingParams_thenSuccess() throws ValidationException {
    validationService.validateTrainingParams(paramsDTO);
  }

  @Test
  public void whenValidationFailsOnTrainingHall_thenException() {
    paramsDTO.setHall("E");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateTrainingParams(paramsDTO));
    Assert.assertTrue(e.getMessage().contains("Hall is not acceptable"));
  }

  @Test
  public void whenValidationFailsOnType_thenException() {
    paramsDTO.setType("valami");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateTrainingParams(paramsDTO));
    Assert.assertTrue(e.getMessage().contains("Type is not acceptable"));
  }

  @Test
  public void whenValidationFailsOnDate() {
    paramsDTO.setTrainingFrom(new Date(new Timestamp(System.currentTimeMillis()).getTime() + 3*oneDay).toString() + " 15:11");
    paramsDTO.setTrainingTo(new Date(new Timestamp(System.currentTimeMillis()).getTime() + 3*oneDay).toString() + " 16:30:");
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.validateTrainingParams(paramsDTO));
    Assert.assertTrue(e.getMessage().contains("Date is not accepted"));
  }

}
