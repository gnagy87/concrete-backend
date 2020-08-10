package com.concrete.poletime.utils.dateservice;

import com.concrete.poletime.exceptions.DateConversionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class TicketDateParserTests {

  private DateService dateService;
  private String date;

  @Before
  public void setup() {
    this.dateService = new DateServiceImpl();
    this.date = "2020-01-01";
  }

  @Test
  public void whenTicketDateParsing_thenSuccess() throws DateConversionException {
    LocalDate dateResult = dateService.ticketDateParser(date);
    Assert.assertEquals(10, dateResult.toString().length());
  }

  @Test
  public void whenDateParsingFails_thenException() {
    this.date = null;
    Exception e = Assert.assertThrows(DateConversionException.class,
        () -> dateService.ticketDateParser(date));
    Assert.assertTrue(e.getMessage().contains("Could not parse given ticket"));
  }

}
