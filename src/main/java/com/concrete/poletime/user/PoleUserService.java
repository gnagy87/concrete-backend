package com.concrete.poletime.user;

import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.dto.RegistrationResponseDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface PoleUserService {
    boolean isExisted(String email);
    RegistrationResponseDTO registration(SetUserParamsDTO userParams) throws RegistrationException, ValidationException;
    Long login(LoginRequestDTO logRequest) throws RecordNotFoundException, LoginException, ValidationException;
    PoleUser loadUserByEmail(String email) throws RecordNotFoundException;
    PoleUser loadUserById(Long id) throws RecordNotFoundException;
    List getUsersWithValidSeasonTicket();
    PoleUserDTO updateRecords(PoleUser user, SetUserParamsDTO userParams) throws ValidationException;
}
