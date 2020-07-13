package com.concrete.poletime.seasonticket;

import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SeasonTicketDTO;
import com.concrete.poletime.dto.SeasonTicketParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.SeasonTicketException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.utils.dateservice.DateService;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SeasonTicketServiceImpl implements SeasonTicketService {

  private SeasonTicketRepository seasonTicketRepo;
  private ValidationService validationService;
  private DateService dateService;

  @Autowired
  public SeasonTicketServiceImpl(SeasonTicketRepository seasonTicketRepo, ValidationService validationService,
                                 DateService dateService) {
    this.seasonTicketRepo = seasonTicketRepo;
    this.validationService = validationService;
    this.dateService = dateService;
  }

  @Override
  public PoleUserDTO createSeasonTicket(SeasonTicketParamsDTO seasonTicketParams, Long sellerId, PoleUser poleUser)
      throws SeasonTicketException, ValidationException, DateConversionException {
    userFilter(poleUser);
    validationService.validityDateValidator(seasonTicketParams.getValidFrom());
    validationService.amountValidator(seasonTicketParams.getAmount());
    LocalDate validFrom = dateService.ticketDateParser(seasonTicketParams.getValidFrom());
    validationService.ticketDateFilter(validFrom);
    LocalDate validTo = validToCalculator(validFrom, seasonTicketParams.getAmount());
    seasonTicketFilter(poleUser.getId(), validFrom);
    SeasonTicket seasonTicket = new SeasonTicket(validFrom, validTo, seasonTicketParams.getAmount(), sellerId, poleUser);
    seasonTicketRepo.save(seasonTicket);
    return new PoleUserDTO(poleUser);
  }

  private void userFilter(PoleUser poleUser) throws SeasonTicketException {
    if (!poleUser.isEnabled() || poleUser.getRole().equals(Role.ADMIN) || poleUser.getRole().equals(Role.TRAINER)) {
      throw new SeasonTicketException("User is not acceptable to set Season Ticket! User is not enabled/ADMIN/TRAINER");
    }
  }

  private void seasonTicketFilter(Long userId, LocalDate validFrom) throws SeasonTicketException {
    Optional<SeasonTicket> lastSeasonTicket = seasonTicketRepo.findLastSeasonTicket(userId);
    if (!(lastSeasonTicket.isEmpty() || lastSeasonTicket.get().getValidTo().isBefore(validFrom))) {
     throw new SeasonTicketException("User already has a valid season ticket");
    }
  }

  private LocalDate validToCalculator(LocalDate validFrom, int amount) {
    return validFrom.plusDays(SeasonTicketProperties.AMOUNT_AND_WEEK.get(amount) * 7);
  }

  @Override
  public SeasonTicketDTO updateSeasonTicket(Long seasonTicketId, SeasonTicketParamsDTO seasonTicketParams)
      throws SeasonTicketException, ValidationException, RecordNotFoundException, DateConversionException {
    SeasonTicket seasonTicket = loadSeasonTicketById(seasonTicketId);
    LocalDate validFrom = seasonTicket.getValidFrom();
    if (seasonTicketParams.getValidFrom() != null) {
      validationService.validityDateValidator(seasonTicketParams.getValidFrom());
      validFrom = dateService.ticketDateParser(seasonTicketParams.getValidFrom());
      updateDateFilter(validFrom);
      if (seasonTicketRepo.findValidSeasonTicket(seasonTicket.getPoleUser().getId(), seasonTicketId, validFrom).isPresent())
        throw new SeasonTicketException("User has a valid season ticket");
      seasonTicket.setValidFrom(validFrom);
    }
    if (seasonTicketParams.getAmount() != 0) {
      validationService.amountValidator(seasonTicket.getAmount());
      LocalDate validTo = validToCalculator(validFrom, seasonTicketParams.getAmount());
      seasonTicket.setValidTo(validTo);
      seasonTicket.setAmount(seasonTicketParams.getAmount());
    }
    seasonTicketRepo.save(seasonTicket);
    return new SeasonTicketDTO(seasonTicket);
  }

  private void updateDateFilter(LocalDate date) throws SeasonTicketException {
    if (!(date.isAfter(LocalDate.now().minusDays(6)) && date.isBefore(LocalDate.now().plusDays(6)))) {
      throw new SeasonTicketException("Valid from not in range. -5 and +5 day is allowed from today");
    }
  }

  @Override
  public SeasonTicket loadSeasonTicketById(Long seasonTicketId) throws RecordNotFoundException {
    return seasonTicketRepo.findById(seasonTicketId)
        .orElseThrow(() -> new RecordNotFoundException("SeasonTicket was not found!"));
  }
}
