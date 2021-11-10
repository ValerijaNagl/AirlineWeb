package rs.edu.raf.spring_project.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.entities.Airline;
import rs.edu.raf.spring_project.model.entities.Flight;
import rs.edu.raf.spring_project.repositories.AirlineRepository;
import rs.edu.raf.spring_project.repositories.FlightRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineService implements IService<Airline, Long>{

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public Airline save(Airline airline) {
        return airlineRepository.save(airline);
    }

    public Airline findByName(String airline){
        return airlineRepository.findByName(airline);
    }

    @Override
    public Optional<Airline> findById(Long id) {
        return airlineRepository.findById(id);
    }

    @Override
    public List<Airline> findAll() {
        return (List<Airline>) airlineRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        airlineRepository.deleteById(id);
    }
}
