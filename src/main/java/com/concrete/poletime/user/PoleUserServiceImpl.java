package com.concrete.poletime.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoleUserServiceImpl implements PoleUserService {

    private PoleUserRepository poleUserRepo;

    @Autowired
    public PoleUserServiceImpl(PoleUserRepository poleUserRepo) {
        this.poleUserRepo = poleUserRepo;
    }
}
