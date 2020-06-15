package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.LoginException;

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
    public ResponseEntity doRegistration(@RequestBody RegistrationRequestDTO regRequest) {
        try {
            return ResponseEntity.ok().body(poleUserService.registration(regRequest));
        } catch (RegistrationException|ValidationException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody LoginRequestDTO logRequest) {
        try {
            poleUserService.login(logRequest);
            return ResponseEntity.ok().body(authService.authentication(logRequest.getEmail()));
        } catch (LoginException|RecordNotFoundException|ValidationException exc) {
            throw new ResponseStatusException(
                    (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
                    exc.getMessage(),
                    exc
            );
        }
    }
}
