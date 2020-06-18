package com.concrete.poletime.dto;

import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.user.PoleUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SeasonTicketDTO {
    private Long id;
    private LocalDate validFrom;
    private LocalDate validTo;
    private int amount;
    private Long userId;
    private Long sellerId;

    public SeasonTicketDTO(PoleUser poleUser) {
        Optional<SeasonTicket> currentSeasonTicket = validSeasonTicket(poleUser.getSeasonTickets());
        if (currentSeasonTicket.isPresent()) {
            this.id = currentSeasonTicket.get().getId();
            this.validFrom = currentSeasonTicket.get().getValidFrom();
            this.validTo = currentSeasonTicket.get().getValidTo();
            this.amount = currentSeasonTicket.get().getAmount();
            this.userId = currentSeasonTicket.get().getPoleUser().getId();
            this.sellerId = currentSeasonTicket.get().getSellerId();
        }
    }

    public SeasonTicketDTO(SeasonTicket seasonTicket) {
        this.id = seasonTicket.getId();
        this.validFrom = seasonTicket.getValidFrom();
        this.validTo = seasonTicket.getValidTo();
        this.amount = seasonTicket.getAmount();
        this.userId = seasonTicket.getPoleUser().getId();
        this.sellerId = seasonTicket.getSellerId();
    }

    private Optional<SeasonTicket> validSeasonTicket(Set<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .filter(seasonTicket -> seasonTicket.getValidTo().equals(LocalDate.now()) || seasonTicket.getValidTo().isAfter(LocalDate.now()))
                .findFirst();
    }
}
