package com.concrete.poletime.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoleUserRepository extends CrudRepository<PoleUser, Long> {
}
