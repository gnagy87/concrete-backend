package com.concrete.poletime.authentication;

import com.concrete.poletime.dto.AuthenticationResponseDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.user.PoleUser;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponseDTO authentication(Long userId);
    PoleUser currentUser(HttpServletRequest request) throws RecordNotFoundException;
    String getToken(HttpServletRequest request);
}
