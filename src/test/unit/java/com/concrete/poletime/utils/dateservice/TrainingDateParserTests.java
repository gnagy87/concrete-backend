package com.concrete.poletime.utils.dateservice;

import com.concrete.poletime.exceptions.DateConversionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class TrainingDateParserTests {

  private DateService dateService;
  private String dateToParse;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.dateToParse = "2020-01-01 10:00";
  }

  @Test
  public void whenParsingTrainingDate_thenSuccess() throws DateConversionException {
    Date result = dateService.trainingDateParser(dateToParse);
    Assert.assertTrue(result.toString().contains(dateToParse));
  }

  @Test
  public void whenParsingFails_thenException() {
    this.dateToParse = "2020-01-01";
    Exception e = Assert.assertThrows(DateConversionException.class,
        () -> dateService.trainingDateParser(dateToParse));
    Assert.assertTrue(e.getMessage().contains("Could not parse training date"));
  }

}
