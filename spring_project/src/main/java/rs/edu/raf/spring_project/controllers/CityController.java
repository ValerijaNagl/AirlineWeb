package rs.edu.raf.spring_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.entities.City;
import rs.edu.raf.spring_project.model.entities.Flight;
import rs.edu.raf.spring_project.services.CityService;
import rs.edu.raf.spring_project.services.FlightService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<City> getAllCities(){
        return cityService.findAll();
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCityById(@RequestParam("cityId") Long id){
        if(id== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't get city which id is null");
        Optional<City> optionalcity = cityService.findById(id);
        if(optionalcity.isPresent()) {
            return ResponseEntity.ok(optionalcity.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  createCity(@RequestBody City city){
        if(city== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null booking");
        return ResponseEntity.ok(cityService.save(city));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  updateCity(@RequestBody City city){
        if(city== null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create null booking");
        return ResponseEntity.ok(cityService.save(city));
    }
}
