package com.concrete.poletime.controllers;

import com.concrete.poletime.authentication.AuthenticationService;
import com.concrete.poletime.dto.TrainingDateDTO;
import com.concrete.poletime.dto.SignUpDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.dto.UserToTrainingDTO;
import com.concrete.poletime.exceptions.DateConversionException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/training")
@Validated
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
  public ResponseEntity setTraining(@Valid @RequestBody TrainingParamsDTO trainingParams, HttpServletRequest request) {
    try {
      PoleUser user = authService.currentUser(request);
      return ResponseEntity.ok().body(trainingService.createTraining(trainingParams, user));
    } catch (AccessDeniedException exc) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, exc.getMessage(), exc);
    } catch (RecordNotFoundException|ValidationException| TrainingException | DateConversionException exc) {
      throw new ResponseStatusException(
          (exc instanceof RecordNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST,
          exc.getMessage(),
          exc
      );
    }
  }

  @PostMapping("/signup")
  public ResponseEntity signUpToTraining(@RequestBody SignUpDTO signUpDTO,
                                         HttpServletRequest request) {
    try {
      PoleUser user = authService.currentUser(request);
      return ResponseEntity.status(200).body(trainingService.signUpForTraining(signUpDTO.getTrainingId(), user));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PostMapping("/signdown")
  public ResponseEntity signDownFromTraining(@RequestBody SignUpDTO signUpDTO,
                                             HttpServletRequest request) {
    try {
      PoleUser user = authService.currentUser(request);
      return ResponseEntity.status(200).body(trainingService.signDownFromTraining(signUpDTO.getTrainingId(), user));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/isheld")
  public ResponseEntity setTrainingIdIsHeld(@RequestBody SignUpDTO signUpDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.setTrainingIsHeld(signUpDTO.getTrainingId()));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/ontraining")
  public ResponseEntity trainingWithUsers(@RequestBody SignUpDTO signUpDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.loadUsersByTraining(signUpDTO.getTrainingId()));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PostMapping("/grouptrainings")
  public ResponseEntity getGroupTrainings(@RequestBody TrainingDateDTO trainingDateDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.getGroupTrainings(
          trainingDateDTO.getFromDate(),
          trainingDateDTO.getToDate()));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @GetMapping("/nongrouptrainings")
  public ResponseEntity getNonPublicTrainings() {
    try {
      return ResponseEntity.status(200).body(trainingService.getNonGroupTrainings());
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/setuser")
  public ResponseEntity setUserToTraining(@RequestBody UserToTrainingDTO userToTrainingDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.setUserToTraining(
          userToTrainingDTO.getTrainingId(),
          userToTrainingDTO.getGuestUserId()));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/unsetuser")
  public ResponseEntity unSetUserToTraining(@RequestBody UserToTrainingDTO userToTrainingDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.unSetUserToTraining(
          userToTrainingDTO.getTrainingId(),
          userToTrainingDTO.getGuestUserId()));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER')")
  @PostMapping("/alltrainings")
  public ResponseEntity getAllTrainings(@RequestBody TrainingDateDTO trainingDateDTO) {
    try {
      return ResponseEntity.status(200).body(trainingService.getAllTrainings(
          trainingDateDTO.getFromDate(),
          trainingDateDTO.getToDate()));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}
