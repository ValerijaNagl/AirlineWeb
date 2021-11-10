package rs.edu.raf.spring_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.entities.Flight;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.services.FlightService;
import rs.edu.raf.spring_project.services.TicketService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Flight> getAllFlights(){
        List<Flight> f = flightService.findAll();
        for(Flight fl : f)
            System.out.println(fl.getTickets());
        return f;
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFlightById(@RequestParam("flightId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get flight which id is null");
        Optional<Flight> optionalflight = flightService.findById(id);
        if(optionalflight.isPresent()) {
            return ResponseEntity.ok(optionalflight.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flight createFlight(@RequestBody Flight flight){
        return flightService.save(flight);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flight updateFlight(@RequestBody Flight flight){
        return flightService.save(flight);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<?> deleteflight(@PathVariable Long id) {
//        Optional<Flight> optional = flightService.findById(id);
//        if(optional.isPresent()) {
//            Flight flight = optional.get();
//
////            for (int i = 0; i < student.getCourses().size(); i++) {
////                student.getCourses().get(i).removeStudent(student);
////            }
//            flightService.deleteById(id);
//
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
}
