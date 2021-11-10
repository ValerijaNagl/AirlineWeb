package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.entities.Booking;

import java.awt.print.Book;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking,Long>{
        List<Booking> findByTicketId(Long id);
}
