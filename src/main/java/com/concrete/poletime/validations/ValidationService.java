package com.concrete.poletime.validations;

import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.seasonticket.SeasonTicket;

import java.util.Date;
import java.util.Set;

public interface ValidationService {
    void emailValidation(String email) throws ValidationException;
    void passwordValidation(String password) throws ValidationException;
    void nameValidation(String name) throws ValidationException;
    void userRegistrationValidator(SetUserParamsDTO regRequest) throws ValidationException;
    void validityDateValidator(String validityDate) throws ValidationException;
    void amountValidator(int amount) throws ValidationException;
    void trainingDateValidator(String date) throws ValidationException;
    boolean trainingHallValidator(String hall);
    boolean trainingTypeValidator(String type);
    boolean trainingLevelValidator(String level);
    void currentSigUpTimeIsNotAbove(Long trainingFrom, Long signUpAttempt) throws ValidationException;
    SeasonTicket userHasValidSeasonTicket(Set<SeasonTicket> tickets, Date trainingFrom) throws ValidationException;
    void isTrainingLimitExceeded(int limit, int participants) throws ValidationException;
    void validateTrainingParams(TrainingParamsDTO trainingParams) throws ValidationException;
}
