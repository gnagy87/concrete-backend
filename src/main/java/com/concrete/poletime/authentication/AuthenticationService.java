package com.concrete.poletime.authentication;

import com.concrete.poletime.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO authentication(String email);
}
