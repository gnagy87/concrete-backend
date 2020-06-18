package com.concrete.poletime.dto;

import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.user.PoleUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SeasonTicketDTO {
    private Long id;
    private Date validFrom;
    private Date validTo;
    private int amount;
    private Long userId;
    private Long sellerId;

    public SeasonTicketDTO(PoleUser poleUser) {
        SeasonTicket currentSeasonTicket = validSeasonTicket(poleUser.getSeasonTickets());
        this.id = currentSeasonTicket.getId();
        this.validFrom = currentSeasonTicket.getValidFrom();
        this.validTo = currentSeasonTicket.getValidTo();
        this.amount = currentSeasonTicket.getAmount();
        this.userId = currentSeasonTicket.getPoleUser().getId();
        this.sellerId = currentSeasonTicket.getSellerId();
    }

    private SeasonTicket validSeasonTicket(Set<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .filter(seasonTicket -> seasonTicket.getValidTo().equals(new Date()) || seasonTicket.getValidTo().after(new Date()))
                .findFirst().get();
    }
}
