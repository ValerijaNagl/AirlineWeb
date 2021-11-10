package rs.edu.raf.spring_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.model.entities.TypeOfUser;
import rs.edu.raf.spring_project.model.entities.UserDto;
import rs.edu.raf.spring_project.services.BookingService;
import rs.edu.raf.spring_project.services.MyUserService;
import rs.edu.raf.spring_project.services.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController@RequestMapping("/api/users")
public class MyUserController {

    private final MyUserService userService;
    private final BookingService bookingService;
    private final TicketService ticketService;
    private final PasswordEncoder passwordEncoder;

    public MyUserController(MyUserService userService, BookingService bookingService, TicketService ticketService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.ticketService = ticketService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  createUser(@RequestBody UserDto newUser) {

        if(newUser==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Null user can't be created");

        if(!isUserValid(newUser)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with bad data can't be created");

        Optional<MyUser> optionalMyUser = Optional.ofNullable(userService.findByUsername(newUser.getUsername()));

        if(optionalMyUser.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with that name already exists!");
        }else{
            MyUser newU = new MyUser();
            newU.setType(TypeOfUser.valueOf(newUser.getType()));
            String password = passwordEncoder.encode(newUser.getPassword());
            newU.setPassword(password);
            newU.setUsername(newUser.getUsername());
            ArrayList<Booking> bookings = new ArrayList<>();
            newU.setBookings(bookings);
            userService.save(newU);
            return ResponseEntity.ok(newU);
        }
    }

    public boolean isUserValid(UserDto user){
        if(user.getPassword().toString().length() < 6) return false;
        if(user.getPassword() == null) return false;
        if(user.getUsername() == null || user.getUsername().equals("")) return false;
        if(user.getType()==null || user.getType().equals("")) return false;

        return true;

    }

    @GetMapping(value="/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsersBookings(@RequestParam("username") String username){

        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
        if(username == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");

        Optional<MyUser> optionalMyUser = Optional.ofNullable(userService.findByUsername(username));
        if(optionalMyUser.isPresent()) {
            return ResponseEntity.ok(optionalMyUser.get().getBookings());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value="/username", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByUsername(@RequestParam("username") String username){

        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
        if(username == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");

        Optional<MyUser> optionalMyUser = Optional.ofNullable(userService.findByUsername(username));
        if(optionalMyUser.isPresent()) {
            return ResponseEntity.ok(optionalMyUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // kada pravimo rezervaciju
    @PostMapping(value="/booking/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bookATicket(@PathVariable() String username,
                                               @RequestBody Booking booking){

        if(!isBookingValid(booking)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create booking with null fields");
        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
        if(username == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
        if(booking != null) {
            Booking newBooking = bookingService.save(booking);
            Optional<MyUser> optionalMyUser = Optional.ofNullable(userService.findByUsername(username));
            if (optionalMyUser.isPresent()) {
                MyUser user = optionalMyUser.get();
                user.getBookings().add(newBooking);
                userService.save(user);
                return ResponseEntity.ok(optionalMyUser.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    public boolean isBookingValid(Booking booking){
        if(booking.getTicket() == null) return false;
        if(booking.getFlight() == null) return false;
        return true;
    }

//    @DeleteMapping(value="/booking/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> deleteUsersBooking(@PathVariable() String username,
//                                         @RequestParam("bookingId") Long bookingId){
//
//        if(username.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
//        if(username == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with null username doesn't exist");
//
//        if(bookingId != null) {
//            Optional<MyUser> optionalMyUser = Optional.ofNullable(userService.findByUsername(username));
//            if (optionalMyUser.isPresent()) {
//                MyUser user = optionalMyUser.get();
//                user.getBookings().removeIf(b -> b.getId()==bookingId);
//                userService.save(user);
//                return ResponseEntity.ok(optionalMyUser.get());
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }



}
