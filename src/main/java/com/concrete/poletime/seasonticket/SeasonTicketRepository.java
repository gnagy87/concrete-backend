package com.concrete.poletime.seasonticket;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonTicketRepository extends CrudRepository<SeasonTicket, Long> {
    @Query(
            value = "SELECT * FROM season_tickets WHERE user_id = ?1 ORDER BY valid_to DESC LIMIT 1",
            nativeQuery = true
    )
    Optional<SeasonTicket> findLastSeasonTicket(Long userId);
}
