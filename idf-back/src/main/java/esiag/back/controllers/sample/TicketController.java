// TicketController.java

package esiag.back.controllers.sample;

import esiag.back.models.sample.Ticket;
import esiag.back.models.sample.Visitor;
import esiag.back.repositories.sample.VisitorRepository;
import esiag.back.services.sample.TicketService;
import esiag.back.services.sample.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitorService visitorService;


    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findById(@PathVariable Long id){
        return new ResponseEntity<>(ticketService.findByIdTicket(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<Ticket>> findAllTickets() {
        return new ResponseEntity<>(ticketService.findAllTicket(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTicket(@PathVariable Long id){
        boolean isRemoved = ticketService.deleteTicket(id);
        if(!isRemoved){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@RequestBody Ticket ticket) {
        // Check if the visitor exists, or create a new one if not

        Visitor visitor = visitorService.saveVisitor(ticket.getVisitor());

        // Set the saved/updated visitor in the ticket
        ticket.setVisitor(visitor);

        // Save the ticket to the database
        Ticket savedTicket = ticketService.addTicket(ticket);

        if (savedTicket != null) {
            // Return the saved ticket to the frontend

            return new ResponseEntity<>(savedTicket, HttpStatus.OK);
        } else {
            // If ticket addition fails, return an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
