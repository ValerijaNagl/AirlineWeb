package rs.edu.raf.spring_project.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "flight")
    @JsonIgnore
    @ToString.Exclude
    private List<Ticket> tickets;
    @ManyToOne()
    private City origin;
    @ManyToOne()
    private City destination;
}
