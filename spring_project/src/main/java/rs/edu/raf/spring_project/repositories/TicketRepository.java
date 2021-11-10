package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.entities.Airline;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.repositories.convention.CustomTicketRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long>, CustomTicketRepository {
}
