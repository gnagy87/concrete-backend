package com.concrete.poletime.user;

import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.email.ConfirmationToken;
import com.concrete.poletime.exceptions.ConfirmationException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;

import javax.persistence.PersistenceException;
import javax.security.auth.login.LoginException;
import java.util.List;

public interface PoleUserService {
    boolean isExisted(String email);
    PoleUser registration(SetUserParamsDTO userParams) throws RegistrationException, ValidationException;
    Long login(LoginRequestDTO logRequest) throws RecordNotFoundException, LoginException, ValidationException, ConfirmationException;
    PoleUser loadUserByEmail(String email) throws RecordNotFoundException;
    PoleUser loadUserById(Long id) throws RecordNotFoundException;
    List getUsersWithValidSeasonTicket();
    PoleUserDTO updateRecords(PoleUser user, SetUserParamsDTO userParams) throws ValidationException;
    String confirmUser(ConfirmationToken confirmationToken) throws RecordNotFoundException, ConfirmationException;
    PoleUser saveUser(PoleUser user) throws PersistenceException;
}
