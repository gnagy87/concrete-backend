package com.concrete.poletime.email;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
    @Query(
            value = "SELECT * FROM confirmation_token WHERE user_id = ?1",
            nativeQuery = true
    )
    Optional<ConfirmationToken> findByPoleUser(Long userId);
}
