package rs.edu.raf.spring_project.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import rs.edu.raf.spring_project.model.auth.Role;
import rs.edu.raf.spring_project.model.entities.Booking;
import rs.edu.raf.spring_project.model.entities.TypeOfUser;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private TypeOfUser type;
    @OneToMany(cascade=CascadeType.REFRESH)
    private List<Booking> bookings;
}
