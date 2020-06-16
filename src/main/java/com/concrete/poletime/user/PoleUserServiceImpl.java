package com.concrete.poletime.user;

import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.dto.RegistrationResponseDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PoleUserServiceImpl implements PoleUserService {

    private PoleUserRepository poleUserRepo;
    private ValidationService validation;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PoleUserServiceImpl(PoleUserRepository poleUserRepo, ValidationService validation, PasswordEncoder passwordEncoder) {
        this.poleUserRepo = poleUserRepo;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isExisted(String email) {
        return poleUserRepo.findPoleUserByEmail(email).isPresent();
    }

    @Override
    public RegistrationResponseDTO registration(SetUserParamsDTO userParams) throws RegistrationException, ValidationException {
        validation.userRegistrationValidator(userParams);
        if (isExisted(userParams.getEmail())) {
            throw new RegistrationException("User with email: " + userParams.getEmail() + "has already been registered");
        }
        poleUserRepo.save(new PoleUser(userParams.getEmail(), userParams.getFirstName(), userParams.getLastName(), passwordEncoder.encode(userParams.getPassword())));
        return new RegistrationResponseDTO(200, "User is successfully registered", userParams.getEmail());
    }

    @Override
    public Long login(LoginRequestDTO logRequest) throws RecordNotFoundException, LoginException, ValidationException {
        loginValidationHelper(logRequest.getEmail(), logRequest.getPassword());
        PoleUser foundUser = loadUserByEmail(logRequest.getEmail());
        if (!passwordEncoder.matches(logRequest.getPassword(), foundUser.getPassword())) {
            throw new LoginException("Password is not correct!");
        }
        return foundUser.getId();
    }

    private void loginValidationHelper(String email, String password) throws ValidationException {
        validation.emailValidation(email);
        validation.passwordValidation(password);
    }

    @Override
    public PoleUser loadUserByEmail(String email) throws RecordNotFoundException {
        return poleUserRepo.findPoleUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("User does not exist"));
    }

    @Override
    public List getUsersWithValidSeasonTicket() {
        List<PoleUserDTO> poleUserDTOS = new ArrayList<>();
        poleUserRepo.findPoleUsersWithValidSeasonTicket().forEach(
                poleUser -> poleUserDTOS.add(new PoleUserDTO(poleUser))
        );
        return poleUserDTOS;
    }

    @Override
    public PoleUser loadUserById(Long id) throws RecordNotFoundException {
        return poleUserRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User does not exist"));
    }

    @Override
    public PoleUserDTO updateRecords(PoleUser user, SetUserParamsDTO userParams) throws ValidationException {
        updateEmail(user, userParams.getEmail());
        updateName(user, userParams.getFirstName(), true);
        updateName(user, userParams.getLastName(), false);
        updatePassword(user, userParams.getPassword());
        poleUserRepo.save(user);
        return new PoleUserDTO(user);

    }

    private PoleUser updateEmail(PoleUser user, String email) throws ValidationException {
        if (email != null) {
            validation.emailValidation(email);
            user.setEmail(email);
        }
        return user;
    }

    private PoleUser updateName(PoleUser user, String name, boolean isFirstName) throws ValidationException {
        if (name != null) {
            validation.nameValidation(name);
            if (isFirstName) {
                user.setFirstName(name);
            } else {
                user.setLastName(name);
            }
        }
        return user;
    }

    private PoleUser updatePassword(PoleUser user, String password) throws ValidationException {
        if (password != null) {
            validation.passwordValidation(password);
            user.setPassword(password);
        }
        return user;
    }
}
