package nazar.sekh.rate_it.dao;

import nazar.sekh.rate_it.models.Ticket;
import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketDAO extends JpaRepository<Ticket, Integer> {
    @Query("select t from Ticket t where t.id=:id")
    Ticket findTicketById(int id);
}
