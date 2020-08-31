package com.concrete.poletime.user;

import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.utils.dateservice.DateService;
import com.concrete.poletime.utils.dateservice.DateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DataJpaTest
public class PoleUserRepositoryTests {

  private PoleUser user;
  private SeasonTicket st;
  private DateService dateService;
  private Long oneDay;
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private PoleUserRepository userRepository;

  @Before
  public void setup() throws DateConversionException {
    this.dateService = new DateServiceImpl();
    this.oneDay = 24 * 60 * 60 * 1000L;
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
    this.user.getSeasonTickets().add(st);
    this.entityManager.persist(user);
    this.entityManager.flush();
  }

  @Test
  public void whenFindPoleUsersWithValidSeasonTicket_thenSuccess() {
    Iterable<PoleUser> iterableUsers = this.userRepository.findPoleUsersWithValidSeasonTicket();
    List<PoleUser> result = StreamSupport.stream(iterableUsers.spliterator(), false)
        .collect(Collectors.toList());
    Assert.assertTrue(result.size() > 0);
  }

}

