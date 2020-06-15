package com.concrete.poletime.seasonticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonTicketServiceImpl implements SeasonTicketService {

    private SeasonTicketRepository seasonTicketRepo;

    @Autowired
    public SeasonTicketServiceImpl(SeasonTicketRepository seasonTicketRepo) {
        this.seasonTicketRepo = seasonTicketRepo;
    }
}
