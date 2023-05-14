package nazar.sekh.rate_it.services;

import nazar.sekh.rate_it.dao.TicketDAO;
import nazar.sekh.rate_it.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketDAO ticketDAO;

    public Ticket save(Ticket ticket) {
        return ticketDAO.save(ticket);
    }

    public List<Ticket> findAll() {
        return ticketDAO.findAll();
    }

    public Ticket findById(int id) {
        return ticketDAO.findTicketById(id);
    }
}
