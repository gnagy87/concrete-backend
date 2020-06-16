package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.AuthenticationResponseDTO;
import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserControllerAPI {

    private PoleUserService poleUserService;
    private AuthenticationService authService;

    @Autowired
    public UserControllerAPI(PoleUserService poleUserService, AuthenticationService authService) {
        this.poleUserService = poleUserService;
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity doRegistration(@RequestBody SetUserParamsDTO userParams) {
        try {
            return ResponseEntity.ok().body(poleUserService.registration(userParams));
        } catch (RegistrationException|ValidationException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody LoginRequestDTO logRequest) {
        try {
            Long userId = poleUserService.login(logRequest);
            AuthenticationResponseDTO auth = authService.authentication(userId);
            return ResponseEntity.ok().body(auth);
        } catch (LoginException|RecordNotFoundException|ValidationException exc) {
            throw new ResponseStatusException(
                    (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
                    exc.getMessage(),
                    exc
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestBody SetUserParamsDTO userParams, HttpServletRequest request) {
        try {
            PoleUser currentUser = authService.currentUser(request);
            return ResponseEntity.ok().body(poleUserService.updateRecords(currentUser, userParams));
        } catch (RecordNotFoundException|ValidationException exc) {
            throw new ResponseStatusException(
                    (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
                    exc.getMessage(),
                    exc);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/withValidSeasonTicket")
    public ResponseEntity getUsersWithValidSeasonTickets() {
        try {
            return ResponseEntity.ok().body(poleUserService.getUsersWithValidSeasonTicket());
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
}
