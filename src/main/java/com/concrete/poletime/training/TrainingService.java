package com.concrete.poletime.training;

import com.concrete.poletime.dto.TrainingDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.TrainingException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.user.PoleUser;
import org.springframework.security.access.AccessDeniedException;

public interface TrainingService {
    TrainingDTO createTraining(TrainingParamsDTO trainingParams, PoleUser user) throws AccessDeniedException, TrainingException, ValidationException;
}
