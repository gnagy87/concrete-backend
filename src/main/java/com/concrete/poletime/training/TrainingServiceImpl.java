package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.NoTrainingRepresentedException;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserService;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import com.concrete.poletime.utils.dateservice.DateService;
import com.concrete.poletime.utils.timeservice.TimeService;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

  private TrainingRepository trainingRepo;
  private ValidationService validationService;
  private PoleUserService userService;
  private TimeService timeService;
  private DateService dateService;

  @Autowired
  public TrainingServiceImpl(TrainingRepository trainingRepo, ValidationService validationService,
                             PoleUserService userService, TimeService timeService, DateService dateService) {
    this.trainingRepo = trainingRepo;
    this.validationService = validationService;
    this.userService = userService;
    this.timeService = timeService;
    this.dateService = dateService;
  }

  @Override
  public TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user)
      throws AccessDeniedException, TrainingException, ValidationException, DateConversionException {
    roleFilter(user, trainingParams.getType());
    validationService.validateTrainingParams(trainingParams);
    Date trainingFrom = dateService.trainingDateParser(trainingParams.getTrainingFrom());
    Date trainingTo = dateService.trainingDateParser(trainingParams.getTrainingTo());
    if (timeService.trainingTimeCalculator(trainingFrom, trainingTo) < 60) throw new TrainingException("Training can not be shorter than 60 min");
    if (isTrainingAccepted(trainingParams.getHall().toUpperCase(), trainingFrom, trainingTo)) throw new TrainingException("There is another training in the same time period");
    Training training = new Training(
      trainingFrom,
      trainingTo,
      TrainingHall.valueOf(trainingParams.getHall().toUpperCase()),
      trainingParams.getType().toUpperCase().equals(TrainingType.GROUP.toString()) ? trainingParams.getPersonLimit() : 0,
      TrainingType.valueOf(trainingParams.getType().toUpperCase()),
      trainingParams.getType().toUpperCase().equals((TrainingType.GROUP.toString())) ? TrainingLevel.valueOf(trainingParams.getLevel().toUpperCase()) : null,
      user.getId());
    trainingRepo.save(training);
    return new TrainingDTO(training);
  }

  @Override
  public List<TrainingDTO> signUpForTraining(Long trainingId, PoleUser user) throws
      ValidationException, NoTrainingRepresentedException, PersistenceException, DateConversionException {
    Training training = loadTrainingById(trainingId);
    validationService.validateSignUpAttempt(training, user);
    SeasonTicket ticket = validationService.userHasValidSeasonTicket(user.getSeasonTickets(), training.getTrainingFrom());
    validationService.userHasAmountToUse(ticket);
    ticket.setUsed(ticket.getUsed() + 1);
    training.setParticipants(training.getParticipants() + 1);
    user.addTraining(training);
    PoleUser signedUpUser = userService.saveUser(user);
    return convertToDTOList(signedUpUser.getTrainings());
  }

  @Override
  public Training loadTrainingById(Long trainingId) throws NoTrainingRepresentedException {
    return trainingRepo.findById(trainingId).orElseThrow(
        () -> new NoTrainingRepresentedException (
            "No training present with given id(" + trainingId + ") !"));
  }

  @Override
  public List<TrainingDTO> signDownFromTraining(Long trainingId, PoleUser user) throws
      NoTrainingRepresentedException, ValidationException, DateConversionException {
    Training training = loadTrainingById(trainingId);
    validationService.validateSignDownAttempt(training, user);
    SeasonTicket ticket = validationService.userHasValidSeasonTicket(user.getSeasonTickets(), training.getTrainingFrom());
    user.removeTraining(training);
    ticket.setUsed(ticket.getUsed() - 1);
    training.setParticipants(training.getParticipants() - 1);
    PoleUser signedDownUser = userService.saveUser(user);
    return convertToDTOList(signedDownUser.getTrainings());
  }

  public List<TrainingDTO> convertToDTOList(Set<Training> trainings) {
    return trainings.stream()
        .map(TrainingDTO::new)
        .filter(t -> !t.isHeld())
        .collect(Collectors.toList());
  }

  private void roleFilter(PoleUser user, String type) throws AccessDeniedException {
    if (type.toUpperCase().equals(TrainingType.GROUP.toString()) && !user.getRole().equals(Role.ADMIN)) {
      throw new AccessDeniedException("User is not able to create GROUP training!");
    }
  }

  private boolean isTrainingAccepted(String hall, Date trainingFrom, Date trainingTo) {
    return trainingRepo.findTrainingInSameTime(hall, trainingFrom, trainingTo).isPresent();
  }
}
