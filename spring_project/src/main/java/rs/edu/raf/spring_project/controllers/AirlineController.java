package rs.edu.raf.spring_project.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Airline;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.Flight;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.services.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;
    private final TicketService ticketService;
    private final FlightService flightService;
    private final MyUserService myUserService;
    private final BookingService bookingService;

    public AirlineController(AirlineService airlineService, TicketService ticketService, FlightService flightService, MyUserService myUserService, BookingService bookingService) {
        this.airlineService = airlineService;
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.myUserService = myUserService;
        this.bookingService = bookingService;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Airline> getAllAirlines(){
        return airlineService.findAll();
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAirlineById(@RequestParam("airlineId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get airline which id is null");
        Optional<Airline> optionalAirline = airlineService.findById(id);
        if(optionalAirline.isPresent()) {
            return ResponseEntity.ok(optionalAirline.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // na osnovu tokena dobijamo korisnika iz baze (userdetailsservice) i znamo njegovu role. ovde ogranicavamo ko moze da koristi funkciju
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAirline(@RequestBody Airline airline){

        if(airline== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null airline");
        if(airline.getName().equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null airline");

        Airline a = airlineService.findByName(airline.getName());
        if(a != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null airline");
        }
        return ResponseEntity.ok(airlineService.save(airline));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAirline(@RequestBody Airline airline){

        if(airline== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't update null airline");
        if(airline.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't update airline with empty name");
        List<Ticket> tickets = ticketService.findAll();

        for(Ticket t : tickets){
            if(t.getAirline().getId() == airline.getId()) {
                t.setAirline(airline);
                ticketService.save(t);
            }
        }
        return ResponseEntity.ok(airlineService.save(airline));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable Long id) {

        if(id == null || id < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Airline with null or negative id doesn't exist");

        Optional<Airline> optionalAirline = airlineService.findById(id);
        if(optionalAirline.isPresent()) {
            Airline airline = optionalAirline.get();
            List<Ticket> tickets = ticketService.findAll();

            // mora da se bazi na redosled brisanja
            // prvo brisemo rezervacije iz korisnika, pa rezervacije, pa karte
            for(Ticket ticket : tickets){
                if(ticket.getAirline().getId() == id) {

                    List<MyUser> allusers = myUserService.findAll();
                    Optional<Ticket> optional = ticketService.findById(ticket.getId());

                    for(MyUser u : allusers){
                        u.getBookings().removeIf(b -> b.getTicket().getId()==ticket.getId());
                        myUserService.save(u);
                    }
                    // letu brisemo kartu koja sadrzi kartu sa tom kompanijom
                    Optional<Flight> f = flightService.findById(ticket.getFlight().getId());
                    if(f.isPresent()){
                        Flight flight = f.get();
                        flight.getTickets().remove(ticket);
                        flightService.save(flight);
                    }

                    List<Booking> bookings = bookingService.findAll();
                    for(Booking b : bookings){
                        if(b.getTicket().getId() == ticket.getId()){
                            bookingService.deleteById(b.getId());
                            ticketService.save(ticket);
                        }
                    }

                    ticketService.deleteById(ticket.getId());
                }
            }
            airlineService.deleteById(id);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airline is not deleted.");
    }

}
