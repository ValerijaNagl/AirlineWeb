package rs.edu.raf.spring_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.Flight;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.services.BookingService;
import rs.edu.raf.spring_project.services.FlightService;
import rs.edu.raf.spring_project.services.MyUserService;
import rs.edu.raf.spring_project.services.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final FlightService flightService;
    private final BookingService bookingService;
    private final MyUserService myUserService;

    public TicketController(TicketService ticketService, FlightService flightService, BookingService bookingService, MyUserService myUserService) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.myUserService = myUserService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket> getAllTickets(){
        return ticketService.findAll();
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTicketById(@RequestParam("ticketId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get ticket which id is null");
        Optional<Ticket> optionalTicket = ticketService.findById(id);
        if(optionalTicket.isPresent()) {
            return ResponseEntity.ok(optionalTicket.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/airline", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTicketsByAirlineId(@RequestParam("airlineId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get airline's tickets when airlineId is null");
        List<Ticket> tickets = ticketService.findAll();
        List<Ticket> ticketsForAirline = new ArrayList<>();

        for(Ticket t : tickets){
            if(t.getAirline().getId() == id)
                ticketsForAirline.add(t);
        }

        return ResponseEntity.ok(ticketsForAirline);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket){
        if(!isTicketValid(ticket)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create ticket with wrong parameters");
       // Optional<Flight> f = flightService.findById(ticket.getFlight().getId());
        Ticket t = ticketService.save(ticket);
        return ResponseEntity.ok(t);
    }

    boolean isTicketValid(Ticket t){

        if(!t.isOneway()){
            if(t.getDepartDate().after(t.getReturnDate())) return false;
            if(t.getReturnDate() == null) return false;
        }
       if(t.getDepartDate() == null) return false;
       if(t.getAirline() == null) return false;
       if(t.getFlight() == null) return false;
       if(t.getCount() == null) return false;
       if(t.getCount() <= 0) return false;
       return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTicket(@RequestBody Ticket ticket){
        if(!isTicketValid(ticket)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create ticket with wrong parameters");
        Optional<Flight> newF = flightService.findById(ticket.getFlight().getId());
        Optional<Ticket> optionalTicket = ticketService.findById(ticket.getId());
        Optional<Flight> oldF = flightService.findById(optionalTicket.get().getFlight().getId());

        if(newF.isPresent()){
            System.out.println(ticket.getId());
            Flight newFlight = newF.get();
            newFlight.getTickets().add(ticket);
            Flight oldFlight = oldF.get();
            oldFlight.getTickets().remove(ticket);
            System.out.println(newFlight.toString());
            System.out.println(oldFlight.toString());
            flightService.save(newFlight);
            flightService.save(oldFlight);
        }
        for(Booking b : bookingService.findAll()){
            if(b.getTicket().getId() == ticket.getId()){
                b.setTicket(ticket);
                b.setFlight(ticket.getFlight());
                bookingService.save(b);
            }
        }
        return ResponseEntity.ok(ticketService.save(ticket));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't delete ticket with id null");
        Optional<Ticket> optional = ticketService.findById(id);
        if(optional.isPresent()) {
            Ticket ticket = optional.get();

            List<MyUser> allusers = myUserService.findAll();

            for(MyUser u : allusers){
                u.getBookings().removeIf(b -> b.getTicket().getId()==ticket.getId());
                myUserService.save(u);
            }

            Optional<Flight> f = flightService.findById(ticket.getFlight().getId());
            if(f.isPresent()){
                Flight flight = f.get();
                flight.getTickets().remove(ticket);
                flightService.save(flight);
            }

            List<Booking> bookings = bookingService.findAll();
            for(Booking b : bookings){
                if(b.getTicket().getId() == id){
                    bookingService.deleteById(b.getId());
                    ticketService.save(optional.get());
                }
            }

            ticketService.deleteById(optional.get().getId());

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket>  searchTickets(@RequestParam(value = "oneway", required = false) String oneway,
                                  @RequestParam(value = "origin", required = false) String origin,
                                  @RequestParam(value = "destination", required = false) String destination,
                                  @RequestParam(value = "departDate", required = false) String departDate,
                                  @RequestParam(value = "returnDate", required = false) String returnDate,
                                    @RequestParam(value="airline", required = false) String airline){
        List<Ticket> tickets = ticketService.search(oneway, departDate, returnDate,  origin,  destination, airline);
        return tickets;

    }
}
