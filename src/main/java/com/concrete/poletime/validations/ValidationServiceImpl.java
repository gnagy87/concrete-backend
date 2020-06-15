package com.concrete.poletime.validations;

import com.concrete.poletime.exceptions.ValidationException;
import org.springframework.stereotype.Service;

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
        if (!password.matches("[-a-zA-Z0-9!?._+]{6,16}")) {
            throw new ValidationException("Password is not valid! Length must be in 6-16 characters and [-a-zA-Z0-9!?._+] characters are allowed!");
        }
    }

    @Override
    public void nameValidation(String name) throws ValidationException {
        if (!name.matches("[a-zA-Z]{2,32}")) {
            throw new ValidationException("Name is not valid! Length must be in 2-20 characters and only letters are allowed!");
        }
    }
}
