package com.concrete.poletime.user;

import com.concrete.poletime.dto.LoginRequestDTO;
import com.concrete.poletime.dto.PoleUserDTO;
import com.concrete.poletime.dto.SetUserParamsDTO;
import com.concrete.poletime.email.ConfirmationToken;
import com.concrete.poletime.exceptions.ConfirmationException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
  public PoleUser registration(SetUserParamsDTO userParams) throws RegistrationException, ValidationException {
    validation.userRegistrationValidator(userParams);
    if (isExisted(userParams.getEmail())) {
      throw new RegistrationException("User with email: " + userParams.getEmail() + " has already been registered");
    }
    PoleUser newUser = new PoleUser(userParams.getEmail(), userParams.getFirstName(), userParams.getLastName(), passwordEncoder.encode(userParams.getPassword()));
    saveUser(newUser);
//    poleUserRepo.save(newUser);
    return newUser;
  }

  @Override
  public Long login(LoginRequestDTO logRequest) throws RecordNotFoundException, LoginException, ValidationException, ConfirmationException {
    loginValidationHelper(logRequest.getEmail(), logRequest.getPassword());
    PoleUser foundUser = loadUserByEmail(logRequest.getEmail());
    if (!passwordEncoder.matches(logRequest.getPassword(), foundUser.getPassword())) {
      throw new LoginException("Password is not correct!");
    }
    if (!foundUser.isEnabled()) throw new ConfirmationException("User has not verified yet!");
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
    saveUser(user);
//    poleUserRepo.save(user);
    return new PoleUserDTO(user);
  }

  @Override
  public String confirmUser(ConfirmationToken confirmationToken) throws RecordNotFoundException, ConfirmationException {
    PoleUser userToConfirm = loadUserById(confirmationToken.getUser().getId());
    if (userToConfirm.isEnabled()) throw new ConfirmationException("User has already been verified");
    userToConfirm.setEnabled(true);
    poleUserRepo.save(userToConfirm);
    return userToConfirm.getEmail();
  }

  @Override
  public PoleUser saveUser(PoleUser user) throws PersistenceException {
    try {
      return poleUserRepo.save(user);
    } catch (Exception e) {
      throw new PersistenceException("Could not save given user to DB !", e);
    }
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
      user.setPassword(passwordEncoder.encode(password));
    }
    return user;
  }
}
