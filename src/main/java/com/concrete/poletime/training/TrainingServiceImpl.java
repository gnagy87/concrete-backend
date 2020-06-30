package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepo;
    private ValidationService validationService;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepo, ValidationService validationService) {
        this.trainingRepo = trainingRepo;
        this.validationService = validationService;
    }

    @Override
    public TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user) throws AccessDeniedException, TrainingException, ValidationException {
        roleFilter(user, trainingParams.getType());
        trainingDateValidatorHelper(trainingParams.getTrainingFrom(), trainingParams.getTrainingTo());
        if (!validationService.trainingHallValidator(trainingParams.getHall())) throw new ValidationException("Hall is not acceptable!");
        if (!validationService.trainingTypeValidator(trainingParams.getType())) throw new ValidationException("Type is not acceptable!");
        if (trainingParams.getType().toUpperCase().equals(TrainingType.GROUP.toString())) {
            if (!validationService.trainingLevelValidator(trainingParams.getLevel())) throw new ValidationException("Level is not acceptable!");
        }
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

    private void trainingDateValidatorHelper(String trainingFrom, String trainingTo) throws ValidationException {
        validationService.trainingDateValidator(trainingFrom);
        validationService.trainingDateValidator(trainingTo);
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
        if (trainingTo.before(trainingFrom)) throw new TrainingException("trainingTo van not be smaller than trainingFrom");
        return (trainingTo.getTime() - trainingFrom.getTime()) / (60 * 1000);
    }

    private boolean isTrainingAccepted(String hall, Date trainingFrom, Date trainingTo) {
        return trainingRepo.findTrainingInSameTime(hall, trainingFrom, trainingTo).isPresent();
    }
}
