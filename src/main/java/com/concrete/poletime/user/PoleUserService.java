package com.concrete.poletime.user;

import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.dto.RegistrationResponseDTO;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;

public interface PoleUserService {
    boolean isExisted(String email);
    RegistrationResponseDTO registration(RegistrationRequestDTO regRequest) throws RegistrationException, ValidationException;
}
