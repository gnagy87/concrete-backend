package com.concrete.poletime.seasonticket;

import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SeasonTicketParamsDTO;
import com.concrete.poletime.exceptions.SeasonTicketException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SeasonTicketServiceImpl implements SeasonTicketService {

    private SeasonTicketRepository seasonTicketRepo;
    private ValidationService validationService;

    @Autowired
    public SeasonTicketServiceImpl(SeasonTicketRepository seasonTicketRepo, ValidationService validationService) {
        this.seasonTicketRepo = seasonTicketRepo;
        this.validationService = validationService;
    }

    @Override
    public PoleUserDTO createSeasonTicket(SeasonTicketParamsDTO seasonTicketParams, Long sellerId, PoleUser poleUser) throws SeasonTicketException, ValidationException {
        userFilter(poleUser);
        validationService.validityDateValidator(seasonTicketParams.getValidFrom());
        LocalDate validFrom = dateParser(seasonTicketParams.getValidFrom());
        if (!dateFilter(validFrom)) throw new SeasonTicketException("validFrom can not be before today!");
        LocalDate validTo = validFrom.plusDays(seasonTicketParams.getValidityDays());
        if (!seasonTicketFilter(poleUser.getId(), validFrom)) throw new SeasonTicketException("User has a valid season ticket");
        SeasonTicket seasonTicket = new SeasonTicket(validFrom, validTo, seasonTicketParams.getAmount(), sellerId, poleUser);
        seasonTicketRepo.save(seasonTicket);
        return new PoleUserDTO(poleUser);
    }

    private LocalDate dateParser(String date) throws SeasonTicketException {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new SeasonTicketException(e.getMessage());
        }
    }

    private boolean dateFilter(LocalDate date) {
        return LocalDate.now().isEqual(date) || LocalDate.now().isBefore(date);
    }

    private void userFilter(PoleUser poleUser) throws SeasonTicketException {
        if (!poleUser.isEnabled() || poleUser.getRole().equals(Role.ADMIN) || poleUser.getRole().equals(Role.TRAINER)) {
            throw new SeasonTicketException("User is not acceptable to set Season Ticket! User is not enabled/ADMIN/TRAINER");
        }
    }

    private boolean seasonTicketFilter(Long userId, LocalDate validFrom) {
        Optional<SeasonTicket> lastSeasonTicket = seasonTicketRepo.findLastSeasonTicket(userId);
        return (!lastSeasonTicket.isPresent() || lastSeasonTicket.get().getValidTo().isBefore(validFrom));
    }
}
