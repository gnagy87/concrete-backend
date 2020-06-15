package com.concrete.poletime.authentication;

import com.concrete.poletime.dto.AuthenticationResponseDTO;
import com.concrete.poletime.jwt.JwtUtil;
import com.concrete.poletime.security.MyUserDetails;
import com.concrete.poletime.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private JwtUtil jwtTokenUtil;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(JwtUtil jwtTokenUtil, MyUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationResponseDTO authentication(String email) {
        final MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponseDTO(token);
    }
}
