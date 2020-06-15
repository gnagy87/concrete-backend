package com.concrete.poletime.seasonticket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonTicketRepository extends CrudRepository<SeasonTicket, Long> {
}
