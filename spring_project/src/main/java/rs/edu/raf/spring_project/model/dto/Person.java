package rs.edu.raf.spring_project.model.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class Person implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
}
