package com.concrete.poletime.validations;

import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

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
}
