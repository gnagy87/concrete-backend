package com.concrete.poletime.seasonticket;

import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SeasonTicketParamsDTO;
import com.concrete.poletime.exceptions.SeasonTicketException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;

import java.util.Set;

public interface SeasonTicketService {
    PoleUserDTO createSeasonTicket(SeasonTicketParamsDTO seasonTicketParams, Long sellerId, PoleUser poleUser) throws SeasonTicketException, ValidationException;
}
