package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Airline;

public interface AirlineRepository extends CrudRepository<Airline,Long> {
    Airline findByName(String name);
}
