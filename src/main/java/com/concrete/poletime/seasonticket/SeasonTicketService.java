package com.concrete.poletime.seasonticket;

import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SeasonTicketDTO;
import com.concrete.poletime.dto.SeasonTicketParamsDTO;
import com.concrete.poletime.dto.SeasonTicketUpdateParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.SeasonTicketException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;

public interface SeasonTicketService {
    PoleUserDTO createSeasonTicket(SeasonTicketParamsDTO seasonTicketParams, Long sellerId, PoleUser poleUser)
        throws SeasonTicketException, ValidationException, DateConversionException;

    SeasonTicketDTO updateSeasonTicket(SeasonTicketUpdateParamsDTO seasonTicketParams)
        throws SeasonTicketException, ValidationException, RecordNotFoundException, DateConversionException;

    SeasonTicket loadSeasonTicketById(Long seasonTicketId) throws RecordNotFoundException;
}
