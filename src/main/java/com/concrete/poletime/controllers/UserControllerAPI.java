package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.*;
import com.concrete.poletime.email.ConfirmationToken;
import com.concrete.poletime.email.ConfirmationTokenService;
import com.concrete.poletime.email.EmailSenderService;
import com.concrete.poletime.exceptions.ConfirmationException;
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
    private ConfirmationTokenService confirmationTokenService;
    private EmailSenderService emailSenderService;

    @Autowired
    public UserControllerAPI(PoleUserService poleUserService,
                             AuthenticationService authService,
                             ConfirmationTokenService confirmationTokenService,
                             EmailSenderService emailSenderService) {
        this.poleUserService = poleUserService;
        this.authService = authService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/registration")
    public ResponseEntity doRegistration(@RequestBody SetUserParamsDTO userParams) {
        try {
            PoleUser newUser = poleUserService.registration(userParams);
            ConfirmationToken confirmationToken = confirmationTokenService.setConfirmationTokenToUser(newUser);
            emailSenderService.setConfirmationEmail(newUser, confirmationToken);
            return ResponseEntity.ok().body(new UserStatusDTO(200, "User is registered successfully", newUser.getEmail()));
        } catch (RegistrationException|ValidationException|ConfirmationException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity confirmRegistration(@RequestParam("token")String confirmationToken) {
        try {
            ConfirmationToken token = confirmationTokenService.loadConfirmationToken(confirmationToken);
            String email = poleUserService.confirmUser(token);
            return ResponseEntity.ok().body(new UserStatusDTO(200, "Account verified", email));
        } catch (RecordNotFoundException|ConfirmationException exc) {
            throw new ResponseStatusException(
                    (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
                    exc.getMessage(),
                    exc
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody LoginRequestDTO logRequest) {
        try {
            Long userId = poleUserService.login(logRequest);
            AuthenticationResponseDTO auth = authService.authentication(userId);
            return ResponseEntity.ok().body(auth);
        } catch (ConfirmationException exc) {
            return ResponseEntity.badRequest().body(new UserStatusDTO(400, exc.getMessage(), logRequest.getEmail()));
        } catch (LoginException|RecordNotFoundException|ValidationException exc) {
            throw new ResponseStatusException(
                    (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
                    exc.getMessage(),
                    exc
            );
        }
    }

    @GetMapping("/resend-confirm-token")
    public ResponseEntity resendConfirmationToken(@RequestParam("email")String email) {
        try {
            PoleUser user = poleUserService.loadUserByEmail(email);
            ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenToResend(user);
            emailSenderService.setConfirmationEmail(user, confirmationToken);
            return ResponseEntity.ok().body(new UserStatusDTO(200, "Confirmation email sent", email));
        } catch (RecordNotFoundException|ConfirmationException exc) {
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
