package esiag.back.services.sample;

import esiag.back.models.sample.Ticket;
import esiag.back.repositories.sample.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    public Ticket addTicket(Ticket ticket){
        Ticket ticketAdded=ticketRepository.save(ticket);
        if (ticketAdded==null){
            return ticketAdded;
        }
        return null;
    }
    public Ticket findByIdTicket(Long idTicket) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
        return optionalTicket.orElse(null);
    }

    public Iterable<Ticket> findAllTicket(){
        return ticketRepository.findAll();
    }


    public boolean deleteTicket(Long idTicket) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
        if (optionalTicket.isPresent()) {
            optionalTicket.ifPresent(sample -> ticketRepository.delete(sample));
            return true;
        }
        return false;
    }

//    public static void main(String[] args) {
//        TicketService ts=new TicketService();
//        ts.addTicket(new Ticket())
//    }
}
