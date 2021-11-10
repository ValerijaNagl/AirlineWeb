package rs.edu.raf.spring_project.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IService<Booking, Long>{

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findAll() {
        return (List<Booking>) bookingRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> findByTicketId(Long id){
        return bookingRepository.findByTicketId(id);
    }
}
