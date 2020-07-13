package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.NoTrainingRepresentedException;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.PersistenceException;
import java.util.List;

public interface TrainingService {
    TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user) throws
        AccessDeniedException,
        TrainingException,
        ValidationException;

    List<TrainingDTO> signUpForTraining(Long trainingId, PoleUser user) throws
        ValidationException,
        NoTrainingRepresentedException,
        PersistenceException;

    Training loadTrainingById(Long trainingId) throws NoTrainingRepresentedException;
}
