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

import java.text.ParseException;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class TicketDateFilterTests {

  private ValidationService validationService;
  private DateService dateService;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
  }

  @Test
  public void whenValidTicketDate_thenSuccess() throws ValidationException {
    LocalDate date = LocalDate.now().plusDays(1);
    validationService.ticketDateFilter(date);
    date = LocalDate.now().plusDays(1);
    validationService.ticketDateFilter(date);
    date = LocalDate.now().plusDays(4);
    validationService.ticketDateFilter(date);
  }

  @Test
  public void whenInvalidTicketDate_thenException() throws ValidationException {
    LocalDate date = LocalDate.now().plusDays(5);
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.ticketDateFilter(date));

  }
}
