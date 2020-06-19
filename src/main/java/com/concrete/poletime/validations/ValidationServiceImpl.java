package com.concrete.poletime.validations;

import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.training.Training;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void emailValidation(String email) throws ValidationException {
        if (!email.matches("[a-z0-9._-]{3,32}@[a-z0-9._-]{2,20}.[a-z]{2,3}")) {
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
}