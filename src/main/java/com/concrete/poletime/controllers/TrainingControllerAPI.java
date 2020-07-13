package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.training.TrainingService;
import com.concrete.poletime.user.PoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/training")
public class TrainingControllerAPI {
  private TrainingService trainingService;
  private AuthenticationService authService;

  @Autowired
  public TrainingControllerAPI(TrainingService trainingService, AuthenticationService authService) {
    this.trainingService = trainingService;
    this.authService = authService;
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/create")
  public ResponseEntity setTraining(@RequestBody TrainingParamsDTO trainingParams, HttpServletRequest request) {
    try {
      PoleUser user = authService.currentUser(request);
      return ResponseEntity.ok().body(trainingService.createTraining(trainingParams, user));
    } catch (AccessDeniedException exc) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, exc.getMessage(), exc);
    } catch (RecordNotFoundException|ValidationException| TrainingException exc) {
      throw new ResponseStatusException(
          (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
          exc.getMessage(),
          exc
      );
    }
  }

  @PostMapping("/signup")
  public ResponseEntity signUpToTraining(@RequestParam("trainingId") Long trainingId,
                                         HttpServletRequest request) {
    try {
      PoleUser user = authService.currentUser(request);
      return ResponseEntity.status(200).body(trainingService.signUpForTraining(trainingId, user));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }
}
