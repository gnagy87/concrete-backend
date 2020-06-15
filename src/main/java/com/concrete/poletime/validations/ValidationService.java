package com.concrete.poletime.validations;

import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.exceptions.ValidationException;

public interface ValidationService {
    void emailValidation(String email) throws ValidationException;
    void passwordValidation(String password) throws ValidationException;
    void nameValidation(String name) throws ValidationException;
    void userRegistrationValidator(RegistrationRequestDTO regRequest) throws ValidationException;
}
