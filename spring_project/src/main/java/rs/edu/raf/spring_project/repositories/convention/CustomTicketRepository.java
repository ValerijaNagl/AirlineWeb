package rs.edu.raf.spring_project.repositories.convention;

import org.springframework.stereotype.Repository;
import rs.edu.raf.spring_project.model.entities.Ticket;

import java.util.List;

@Repository
public interface CustomTicketRepository {


    List<Ticket> searchTickets(String oneway, String departDate, String returnDate, String origin, String destination,String airline) ;


}
