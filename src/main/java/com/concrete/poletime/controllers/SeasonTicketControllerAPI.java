package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.SeasonTicketParamsDTO;
import com.concrete.poletime.dto.SeasonTicketUpdateParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.SeasonTicketException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.seasonticket.SeasonTicketService;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/seasonTicket")
@Validated
public class SeasonTicketControllerAPI {

  private SeasonTicketService seasonTicketService;
  private PoleUserService poleUserService;
  private AuthenticationService authService;


  @Autowired
  public SeasonTicketControllerAPI(SeasonTicketService seasonTicketService, PoleUserService poleUserService, AuthenticationService authService) {
    this.seasonTicketService = seasonTicketService;
    this.poleUserService = poleUserService;
    this.authService = authService;
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/create")
  public ResponseEntity setSeasonTicketToGuest(@Valid @RequestBody SeasonTicketParamsDTO seasonTicketParams,
                                               HttpServletRequest request) {
    try {
      Long sellerId = authService.getUserIdFromToken(request);
      PoleUser poleUser = poleUserService.loadUserByEmail(seasonTicketParams.getEmail());
      return ResponseEntity.ok().body(seasonTicketService.createSeasonTicket(seasonTicketParams, sellerId, poleUser));
    } catch (RecordNotFoundException | ValidationException | SeasonTicketException | DateConversionException exc) {
      throw new ResponseStatusException(
        (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
        exc.getMessage(),
        exc
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/update")
  public ResponseEntity updateSeasonTicket(@Valid @RequestBody SeasonTicketUpdateParamsDTO seasonTicketParams) {
    try {
      return ResponseEntity.ok().body(seasonTicketService.updateSeasonTicket(seasonTicketParams));
    } catch (SeasonTicketException | ValidationException | RecordNotFoundException | DateConversionException exc) {
      throw new ResponseStatusException(
        (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
        exc.getMessage(),
        exc
      );
    }
  }
}
