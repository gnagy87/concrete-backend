package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.NoTrainingRepresentedException;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserService;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

  private TrainingRepository trainingRepo;
  private ValidationService validationService;
  private PoleUserService userService;

  @Autowired
  public TrainingServiceImpl(TrainingRepository trainingRepo, ValidationService validationService,
                             PoleUserService userService) {
    this.trainingRepo = trainingRepo;
    this.validationService = validationService;
    this.userService = userService;
  }

  @Override
  public TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user)
      throws AccessDeniedException, TrainingException, ValidationException {
    roleFilter(user, trainingParams.getType());
    validationService.validateTrainingParams(trainingParams);
    Date trainingFrom = dateParser(trainingParams.getTrainingFrom());
    Date trainingTo = dateParser(trainingParams.getTrainingTo());
    if (trainingTimeCalculator(trainingFrom, trainingTo) < 60) throw new TrainingException("Training can not be shorter than 60 min");
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
      ValidationException, NoTrainingRepresentedException, PersistenceException {
    Training training = loadTrainingById(trainingId);
    validationService.isTrainingLimitExceeded(training.getPersonLimit(), training.getParticipants());
    validationService.currentSigUpTimeIsNotAbove(training.getTrainingFrom().getTime(),
        new Date(System.currentTimeMillis()).getTime());
    validationService.userHasValidSeasonTicket(user.getSeasonTickets(), training.getTrainingFrom());
    training.setParticipants(training.getParticipants() + 1);
    user.addTraining(training);
    PoleUser signedUpUser = userService.saveUser(user);
    return signedUpUser.getTrainings().stream()
        .map(TrainingDTO::new)
        .filter(t -> !t.isHeld())
        .collect(Collectors.toList());
  }

  @Override
  public Training loadTrainingById(Long trainingId) throws NoTrainingRepresentedException {
    return trainingRepo.findById(trainingId).orElseThrow(
        () -> new NoTrainingRepresentedException(
            "No training present with given id(" + trainingId + ") !"));
  }

  private Date dateParser(String dateToParse) throws TrainingException {
    try {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateToParse);
    } catch (Exception e) {
      throw new TrainingException(e.getMessage());
    }
  }

  private void roleFilter(PoleUser user, String type) throws AccessDeniedException {
    if (type.toUpperCase().equals(TrainingType.GROUP.toString()) && !user.getRole().equals(Role.ADMIN)) {
      throw new AccessDeniedException("User is not able to create GROUP training!");
    }
  }

  private long trainingTimeCalculator(Date trainingFrom, Date trainingTo) throws TrainingException {
    if (trainingTo.before(trainingFrom)) throw new TrainingException("trainingTo can not be smaller than trainingFrom");
    return (trainingTo.getTime() - trainingFrom.getTime()) / (60 * 1000);
  }

  private boolean isTrainingAccepted(String hall, Date trainingFrom, Date trainingTo) {
    return trainingRepo.findTrainingInSameTime(hall, trainingFrom, trainingTo).isPresent();
  }
}
