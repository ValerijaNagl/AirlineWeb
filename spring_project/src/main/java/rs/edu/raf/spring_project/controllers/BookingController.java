package rs.edu.raf.spring_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.model.entities.UserDto;
import rs.edu.raf.spring_project.services.BookingService;
import rs.edu.raf.spring_project.services.MyUserService;
import rs.edu.raf.spring_project.services.TicketService;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TicketService ticketService;
    private final MyUserService myUserService;

    public BookingController(BookingService bookingService, TicketService ticketService, MyUserService myUserService) {
        this.bookingService = bookingService;
        this.ticketService = ticketService;
        this.myUserService = myUserService;
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> getAllBookings(){
        return bookingService.findAll();
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookingById(@RequestParam("bookingId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get airline which id is null");
        Optional<Booking> optional = bookingService.findById(id);
        if(optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBooking(@RequestBody Booking booking){
        if(!isBookingValid(booking)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create booking with null fields");
        if(booking== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null booking");
        return ResponseEntity.ok(bookingService.save(booking));
    }


    public boolean isBookingValid(Booking booking){
        if(booking.getTicket() == null) return false;
        if(booking.getFlight() == null) return false;
        return true;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBooking(@RequestBody Booking booking){
        if(!isBookingValid(booking)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't update booking with null fields");
        if(booking== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't update null booking");
        return ResponseEntity.ok(bookingService.save(booking));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/buy")
    public ResponseEntity<?> buyTickets(@RequestParam("username") String username){

        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter username can't be '' ");
        if(username==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter username can't be null");
        Optional<MyUser> optionalMyUser = Optional.ofNullable(myUserService.findByUsername(username));
        if(optionalMyUser.isPresent()){

            List<Booking> bookings = optionalMyUser.get().getBookings();
            System.out.println(bookings);
            List<Booking> empty = new ArrayList<>();
            optionalMyUser.get().setBookings(empty);
            myUserService.save(optionalMyUser.get());

            for(Booking b : bookings){
                Optional<Booking> optionalBooking = bookingService.findById(b.getId());
                if(optionalBooking.isPresent()) {
                    Optional<Ticket> optionalTicket = ticketService.findById(optionalBooking.get().getTicket().getId());
                    if(optionalTicket.isPresent()){
                        optionalTicket.get().setCount(optionalTicket.get().getCount() - 1);
                        ticketService.save(optionalTicket.get());
                    }
                    bookingService.deleteById(optionalBooking.get().getId());
                }
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id, @RequestParam("username") String username) {
        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter username can't be '' ");
        if(username==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter username can't be null");

        Optional<MyUser> optionalMyUser = Optional.ofNullable(myUserService.findByUsername(username));

        Optional<Booking> optionalBooking = bookingService.findById(id);

        optionalMyUser.get().getBookings().remove(optionalBooking.get());
        myUserService.save(optionalMyUser.get());
        System.out.println(optionalBooking.get());
        if(optionalBooking.isPresent()) {
            bookingService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/ticket/{id}")
    public ResponseEntity<?> deleteBookingByTicketId(@RequestParam("ticketId") Long ticketId) {

       List<Booking> bookings = bookingService.findAll();

       for(Booking b : bookings){
            if(b.getTicket().getId() == ticketId){
                Optional<Booking> optionalBooking = bookingService.findById(b.getId());
                if(optionalBooking.isPresent()) {
                    optionalBooking.get().setTicket(null);
                    bookingService.deleteById(b.getId());
                }
            }
       }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/bookvalid")
    public boolean canUserBookATicket(@RequestParam("ticketId") Long ticketId) {

        List<Booking> bookings = bookingService.findAll();
        Optional<Ticket> t = ticketService.findById(ticketId);
        int count = 0;

        for(Booking b : bookings){
            if(b.getTicket().getId() == ticketId)
                count++;
        }

        System.out.println(count);
        if(count >= t.get().getCount()) return false;
        return true;
    }


}
