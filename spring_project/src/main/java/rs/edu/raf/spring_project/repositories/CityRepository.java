package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.entities.City;

public interface CityRepository extends CrudRepository<City,Long> {
}
