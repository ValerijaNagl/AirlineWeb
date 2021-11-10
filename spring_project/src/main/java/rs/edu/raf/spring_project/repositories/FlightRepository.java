package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.entities.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long> {
}
