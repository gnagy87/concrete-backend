package com.concrete.poletime.controllers;

import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
public class UserControllerAPI {

    private PoleUserService poleUserService;

    @Autowired
    public UserControllerAPI(PoleUserService poleUserService) {
        this.poleUserService = poleUserService;
    }

    @PostMapping("/registration")
    public ResponseEntity doRegistration(@RequestBody RegistrationRequestDTO regRequest) {
        try {
            return ResponseEntity.ok().body(poleUserService.registration(regRequest));
        } catch (RegistrationException|ValidationException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
}
