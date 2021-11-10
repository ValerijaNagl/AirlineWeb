package rs.edu.raf.spring_project.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.repositories.TicketRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements IService<Ticket, Long>{

    private final rs.edu.raf.spring_project.repositories.TicketRepository TicketRepository;

    public TicketService(TicketRepository TicketRepository) {
        this.TicketRepository = TicketRepository;
    }

    @Override
    public Ticket save(Ticket Ticket) {
        return TicketRepository.save(Ticket);
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return TicketRepository.findById(id);
    }

    @Override
    public List<Ticket> findAll() {
        return (List<Ticket>) TicketRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        TicketRepository.deleteById(id);
    }

    public List<Ticket> search(String oneway, String departDate, String returnDate, String origin, String destination, String airline){
        return TicketRepository.searchTickets(oneway,departDate,returnDate,origin, destination, airline);
    }
}
