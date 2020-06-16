package com.concrete.poletime.user;

import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.dto.RegistrationResponseDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface PoleUserService {
    boolean isExisted(String email);
    RegistrationResponseDTO registration(RegistrationRequestDTO regRequest) throws RegistrationException, ValidationException;
    void login(LoginRequestDTO logRequest) throws RecordNotFoundException, LoginException, ValidationException;
    PoleUser loadUserByEmail(String email) throws RecordNotFoundException;
    List getUsersWithValidSeasonTicket();
}
