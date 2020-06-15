package com.concrete.poletime.security;

import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private PoleUserRepository poleUserRepo;

    @Autowired
    public MyUserDetailsService(PoleUserRepository poleUserRepo) {
        this.poleUserRepo = poleUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PoleUser user = poleUserRepo.findPoleUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user does not exist!"));
        return new MyUserDetails(user);
    }
}
