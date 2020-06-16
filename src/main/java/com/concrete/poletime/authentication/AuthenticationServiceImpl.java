package com.concrete.poletime.authentication;

import com.concrete.poletime.dto.AuthenticationResponseDTO;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.jwt.JwtUtil;
import com.concrete.poletime.security.MyUserDetails;
import com.concrete.poletime.security.MyUserDetailsService;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private JwtUtil jwtTokenUtil;
    private MyUserDetailsService userDetailsService;
    private PoleUserService poleUserService;

    @Autowired
    public AuthenticationServiceImpl(JwtUtil jwtTokenUtil, MyUserDetailsService userDetailsService, PoleUserService poleUserService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.poleUserService = poleUserService;
    }

    @Override
    public AuthenticationResponseDTO authentication(Long userId) {
        final MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserById(userId);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponseDTO(token);
    }

    @Override
    public PoleUser currentUser(HttpServletRequest request) throws RecordNotFoundException {
        String token = getToken(request);
        Long userId = jwtTokenUtil.extractUserId(token);
        return poleUserService.loadUserById(userId);
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }
}
