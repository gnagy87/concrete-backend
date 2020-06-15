package com.concrete.poletime.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoleUserRepository extends CrudRepository<PoleUser, Long> {
    Optional<PoleUser> findPoleUserByEmail(String email);
}
