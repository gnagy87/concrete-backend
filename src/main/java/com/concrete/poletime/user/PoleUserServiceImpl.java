package com.concrete.poletime.user;

import com.concrete.poletime.dto.RegistrationRequestDTO;
import com.concrete.poletime.dto.RegistrationResponseDTO;
import com.concrete.poletime.exceptions.RegistrationException;
import com.concrete.poletime.exceptions.ValidationException;
import com.concrete.poletime.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public RegistrationResponseDTO registration(RegistrationRequestDTO regRequest) throws RegistrationException, ValidationException {
        validation.userRegistrationValidator(regRequest);
        if (isExisted(regRequest.getEmail())) {
            throw new RegistrationException("User with email: " + regRequest.getEmail() + "has already been registered");
        }
        poleUserRepo.save(new PoleUser(regRequest.getEmail(), regRequest.getFirstName(), regRequest.getLastName(), passwordEncoder.encode(regRequest.getPassword())));
        return new RegistrationResponseDTO(200, "User is successfully registered", regRequest.getEmail());
    }
}
