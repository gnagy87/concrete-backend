package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.user.PoleUser;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class HasNoOverlappingTicketsTests {

  private ValidationService validationService;
  private DateService dateService;

  private Long oneDay;
  private LocalDate newTicketValidFrom;

  private Set<SeasonTicket> seasonTickets;
  private PoleUser user;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.user = new PoleUser(
        "bagyondaniel@gmail.com",
        "danel",
        "bagyon",
        "aA@123456"
    );
    this.newTicketValidFrom = dateService.ticketDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime() +  6*oneDay).toString());
    this.seasonTickets = new HashSet<>(
        Collections.singletonList(
            new SeasonTicket(
                dateService.ticketDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime() - 5 * oneDay).toString()),
                dateService.ticketDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime() + 5 * oneDay).toString()),
                5,
                1L,
                this.user
            )
        )
    );

  }

  @Test
  public void whenValidateOverlappingTickets_thenSuccess() throws ValidationException {
    validationService.hasNoOverlappingTickets(newTicketValidFrom, seasonTickets);
  }

  @Test
  public void whenValidationFailsOnOverlapping_thenException() throws DateConversionException, ValidationException {
    this.newTicketValidFrom = dateService.ticketDateParser(new Date(new Timestamp(System.currentTimeMillis()).getTime()).toString());
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.hasNoOverlappingTickets(newTicketValidFrom, seasonTickets)
    );
    Assert.assertTrue(e.getMessage().contains("User already has got valid season ticket"));
  }


}
