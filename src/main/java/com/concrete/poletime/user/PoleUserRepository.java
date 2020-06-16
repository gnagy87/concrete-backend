package com.concrete.poletime.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoleUserRepository extends CrudRepository<PoleUser, Long> {
    Optional<PoleUser> findPoleUserByEmail(String email);
    @Query(
            nativeQuery = true,
            value =
                    "SELECT * FROM users JOIN season_tickets ON users.id = season_tickets.user_id " +
                            "WHERE season_tickets.valid_to >= CURRENT_TIMESTAMP"
    )
    Iterable<PoleUser> findPoleUsersWithValidSeasonTicket();
}
