package rs.edu.raf.spring_project.model.entities;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String type;
}
