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

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class UserHasValidSeasonTicketAndAmountToUseTests {

  private ValidationService validationService;
  private DateService dateService;

  private Date trainingFrom;
  private PoleUser user;
  private Set<SeasonTicket> seasonTickets;
  private SeasonTicket st;
  private Long oneDay;

  @Before
  public void setup() throws DateConversionException, ParseException {
    this.dateService = new DateServiceImpl();
    this.validationService = new ValidationServiceImpl(this.dateService);
    this.oneDay = 24 * 60 * 60 * 1000L;
    this.trainingFrom = dateService.trainingDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() - oneDay).toString() + " 15:00");
    this.user = new PoleUser(
        "bagyondaniel@gmail.com",
        "danel",
        "bagyon",
        "aA@123456"
    );
    this.st = new SeasonTicket(
        dateService.ticketDateParser(new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() - 10*oneDay).toString()),
        dateService.ticketDateParser(new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() + 10*oneDay).toString()),
        20,
        1L,
        this.user
    );
    this.seasonTickets = new HashSet<SeasonTicket>(Collections.singletonList(st));
    this.user.setSeasonTickets(this.seasonTickets);
  }

  @Test
  public void whenValidateUserHasValidTicket_thenSuccess() throws ValidationException, DateConversionException, ParseException {
    SeasonTicket returnedTicket = validationService.userHasValidSeasonTicket(
        user.getSeasonTickets(), trainingFrom
    );
    Assert.assertNotNull(returnedTicket);
  }

  @Test
  public void whenValidateUserHasValidTicketFails_thenException() throws DateConversionException {
    this.st.setValidFrom(dateService.ticketDateParser(
        new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime() + 30*oneDay).toString()
    ));
    seasonTickets.add(st);
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userHasValidSeasonTicket(
            user.getSeasonTickets(), trainingFrom
        ));
    Assert.assertTrue(e.getMessage().contains("User does not have valid season ticket"));
  }

  @Test
  public void whenValidateUserHasAmount_thenSuccess() throws ValidationException {
    validationService.userHasAmountToUse(st);
  }

  @Test
  public void whenUserHasAmountValidationFails_thenException() {
    st.setUsed(st.getAmount());
    Exception e = Assert.assertThrows(ValidationException.class,
        () -> validationService.userHasAmountToUse(st));

    Assert.assertTrue(e.getMessage().contains("No amount to be used"));
  }

}
