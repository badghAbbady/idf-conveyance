package esiag.back.repositories.sample;

import esiag.back.models.sample.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket,Long> {
}
