package rs.edu.raf.spring_project.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TICKET")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "ID")
    private Flight flight;
    private boolean oneway;
    private Date departDate;
    private Date returnDate;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AIRLINE_ID", referencedColumnName = "ID")
    private Airline airline;
    private Long count;
}
