package rs.edu.raf.spring_project.model.entities;

import lombok.Data;
import org.hibernate.engine.internal.Cascade;
import rs.edu.raf.spring_project.model.auth.MyUser;

import javax.persistence.*;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean available;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "ID")
    private Flight flight;

    @JoinColumn(name = "TICKET_ID", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Ticket ticket;

}
