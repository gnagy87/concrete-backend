package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.*;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.TrainingType;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.PersistenceException;
import java.util.List;

public interface TrainingService {
    TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user) throws
        AccessDeniedException, TrainingException, ValidationException, DateConversionException;

    List<TrainingDTO> signUpForTraining(Long trainingId, PoleUser user) throws
        ValidationException, NoTrainingRepresentedException, PersistenceException, DateConversionException;

    Training loadTrainingById(Long trainingId) throws NoTrainingRepresentedException;

    List<TrainingDTO> signDownFromTraining(Long trainingId, PoleUser user) throws
        NoTrainingRepresentedException, ValidationException, DateConversionException;

    TrainingDTO setTrainingIsHeld(Long trainingId) throws NoTrainingRepresentedException, TrainingIsHeldUnsettableException;
    List<PoleUser> loadUsersByTraining(Long trainingId) throws NoTrainingRepresentedException;
    List<TrainingDTO> getGroupTrainings(String fromDate, String toDate) throws ValidationException, DateConversionException, CannotLoadDataFromDbException;
    List<TrainingDTO> getNonGroupTrainings() throws CannotLoadDataFromDbException;
    List<TrainingDTO> setUserToTraining(Long trainingId, Long guestUserId) throws NoTrainingRepresentedException, RecordNotFoundException, ValidationException, TrainingTypeException;
    void isTrainingTypeGroup(TrainingType trainingType) throws TrainingTypeException;
    List<TrainingDTO> unSetUserToTraining(Long trainingId, Long guestUserId) throws NoTrainingRepresentedException, TrainingTypeException, RecordNotFoundException, ValidationException;
}
