package com.concrete.poletime.validations;

import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.dto.TrainingParamsDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.TrainingIsHeldUnsettableException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.training.Training;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import com.concrete.poletime.utils.dateservice.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

  private DateService dateService;

  @Autowired
  public ValidationServiceImpl(DateService dateService) {
    this.dateService = dateService;
  }

  @Override
  public void emailValidation(String email) throws ValidationException {
    if (!email.matches("[a-z0-9._-]{3,32}@[a-z0-9._-]{2,20}\\.[a-z]{2,3}")) {
      throw new ValidationException("Email format is not valid!");
    }
  }

  @Override
  public void passwordValidation(String password) throws ValidationException {
    String regex = "^(?=.*[0-9]{2})"
        + "(?=.*[a-z])(?=.*[A-Z])"
        + "(?=.*[@#$%^&+=])"
        + "(?=\\S+$).{8,20}$";

    if (!password.matches(regex)) {
      throw new ValidationException("Password is not valid! " +
          "Password must contain at least 2 digits, " +
          "and minimum one lowercase alphabet, " +
          "and minimum one uppercase alphabet," +
          "and minimum one of following special characters: @#$%^&+= " +
          "while spaces are not allowed, " +
          "and password length is minimum 8 but maximum 20 characters long.");
    }
  }

  @Override
  public void nameValidation(String name) throws ValidationException {
    if (!name.matches("[a-zA-Z]{2,32}")) {
      throw new ValidationException("Name is not valid! Length must be in 2-20 characters and only letters are allowed!");
    }
  }

  @Override
  public void userRegistrationValidator(SetUserParamsDTO regRequest) throws ValidationException {
    emailValidation(regRequest.getEmail());
    nameValidation(regRequest.getFirstName());
    nameValidation(regRequest.getLastName());
    passwordValidation(regRequest.getPassword());
  }

  @Override
  public void validityDateValidator(String validityDate) throws ValidationException {
    if (!validityDate.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
      throw new ValidationException("Validity date is not accepted! Correct format: YYYY-MM-DD");
    }
  }

  @Override
  public void amountValidator(int amount) throws ValidationException {
    if (!Arrays.asList(5, 10, 15, 20).contains(amount)) {
      throw new ValidationException("Amount must be: 5 / 10 / 15 / 20");
    }
  }

  @Override
  public void trainingDateValidator(String date) throws ValidationException {
    if (!date.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):(00|15|30|45)")) {
      throw new ValidationException("Date is not accepted! Correct format: YYYY-MM-DD HH:mm | minutes can only be in '00', '15', '30', '45'");
    }
  }

  @Override
  public boolean trainingHallValidator(String hall) {
    return Arrays.stream(TrainingHall.values())
        .filter(trainingHall -> hall.toUpperCase().equals(trainingHall.toString()))
        .findFirst()
        .isPresent();
  }

  @Override
  public boolean trainingTypeValidator(String type) {
    return Arrays.stream(TrainingType.values())
        .filter(trainingType -> type.toUpperCase().equals(trainingType.toString()))
        .findFirst()
        .isPresent();
  }

  @Override
  public boolean trainingLevelValidator(String level) {
    return Arrays.stream(TrainingLevel.values())
        .filter(trainingLevel -> level.toUpperCase().equals(trainingLevel.toString()))
        .findFirst()
        .isPresent();
  }

  @Override
  public void currentSigUpTimeIsNotAbove(Long trainingFrom, Long signUpAttempt) throws ValidationException {
    long oneWeek = 7 * 24 * 60 * 60 * 1000L;
    if(Math.abs(signUpAttempt - trainingFrom) > oneWeek) {
      throw new ValidationException(
          "Invalid sign up attempt! Sorry, sign up to any training shall be within 7 days before training, not above!");
    }
  }

  @Override
  public SeasonTicket userHasValidSeasonTicket(Set<SeasonTicket> tickets,
                                          Date trainingFrom) throws ValidationException, DateConversionException {
    LocalDate trainingDate = dateService.convertTrainingDateToLocalDate(trainingFrom);
    for (SeasonTicket ticket : tickets) {
      LocalDate validFrom = ticket.getValidFrom();
      LocalDate validTo = ticket.getValidTo();
      if(trainingDate.isBefore(validTo) &&
          (trainingDate.isAfter(validFrom) || trainingDate.isEqual(validFrom))) {
        return ticket;
      }
    }
    throw new ValidationException("User does not have valid season ticket to participate on given training!");
  }

  @Override
  public void userHasAmountToUse(SeasonTicket ticket) throws ValidationException {
    if (ticket.getAmount() <= ticket.getUsed()) {
      throw new ValidationException("User has reached ticket limit. No amount to be used!");
    }
  }

  @Override
  public void isTrainingLimitExceeded(int limit, int participants) throws ValidationException {
    if (participants >= limit) {
      throw new ValidationException("Cannot sign up to given training. Limit has already been reached.");
    }
  }

  @Override
  public void validateTrainingParams(TrainingParamsDTO trainingParams) throws ValidationException {
    if (!trainingHallValidator(trainingParams.getHall())) throw new ValidationException("Hall is not acceptable!");
    if (!trainingTypeValidator(trainingParams.getType())) throw new ValidationException("Type is not acceptable!");
    if (trainingParams.getType().toUpperCase().equals(TrainingType.GROUP.toString())) {
      if (!trainingLevelValidator(trainingParams.getLevel())) throw new ValidationException("Level is not acceptable!");
    }
    trainingDateValidatorHelper(trainingParams.getTrainingFrom(), trainingParams.getTrainingTo());
  }

  @Override
  public void ticketDateFilter(LocalDate date) throws ValidationException {
    if (!(LocalDate.now().isEqual(date) || date.isBefore(LocalDate.now().plusDays(5)))) {
      throw new ValidationException("validFrom can not be smaller than today's date and has to be in 5 days from today!");
    }
  }

  private void trainingDateValidatorHelper(String trainingFrom, String trainingTo) throws ValidationException {
    trainingDateValidator(trainingFrom);
    trainingDateValidator(trainingTo);
  }

  @Override
  public void userFilter(PoleUser poleUser) throws ValidationException {
    if (!poleUser.isEnabled() || poleUser.getRole().equals(Role.ADMIN) || poleUser.getRole().equals(Role.TRAINER)) {
      throw new ValidationException("User is not acceptable! User is not enabled/ADMIN/TRAINER");
    }
  }

  @Override
  public void validate24hours(Long trainingFrom, Long now) throws ValidationException {
    long oneDay = 24 * 60 * 60 * 1000L;
    if(Math.abs(now - trainingFrom) < oneDay) {
      throw new ValidationException("Unable to sign down from training, within 24hrs before training start.");
    }
  }

  @Override
  public void validateSignUpAttempt(Training training, PoleUser user, int withTimeLimit) throws ValidationException {
    userFilter(user);
    if (training.getType().toString().equals(TrainingType.GROUP.toString())) {
      isTrainingLimitExceeded(training.getPersonLimit(), training.getPoleUsers().size());
    }
    if (withTimeLimit == 1) {
      currentSigUpTimeIsNotAbove(training.getTrainingFrom().getTime(), new Date(System.currentTimeMillis()).getTime());
    }
    if (training.getPoleUsers().contains(user)) {
      throw new ValidationException("Invalid attempt! User already participate on given training!");
    }
  }

  @Override
  public void validateSignDownAttempt(Training training, PoleUser user, int withTimeLimit) throws ValidationException {
    userFilter(user);
    if (!training.getPoleUsers().contains(user)) {
      throw new ValidationException("Invalid attempt! User is not participate on given training!");
    }
    if (withTimeLimit == 1) {
      validate24hours(training.getTrainingFrom().getTime(), new Date(System.currentTimeMillis()).getTime());
    }
  }

  @Override
  public void doesTrainingIsHeldSettable(Long trainingTo, Long now) throws TrainingIsHeldUnsettableException {
    if ((now - trainingTo) < 0) {
      throw new TrainingIsHeldUnsettableException(
          "Cannot set training isHeld before training has been finished! :) Hogy gondolod? Nem is értem...");
    }
  }

  @Override
  public void validateFromDateIsNotAfter(Date fromDate, Date toDate) throws ValidationException {
    if(toDate.before(fromDate)) {throw new ValidationException(
        "Invalid interval of dates: toDate is earlier than fromDate!");}
  }

  @Override
  public void hasNoOverlappingTickets(LocalDate newTicketValidFrom, Set<SeasonTicket> seasonTickets) throws ValidationException {
    for (SeasonTicket ticket : seasonTickets) {
      if (!ticket.getValidTo().isBefore(newTicketValidFrom)) {
        throw new ValidationException("User already has got valid season ticket in given time period." +
            " Given new ticket's 'validFrom', must not overlap with 'validTo' of any ticket that belongs to given user!");
      }
    }
  }
}