package rs.edu.raf.spring_project.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
}
