package rs.edu.raf.spring_project.model.auth;

import lombok.Data;

@Data
public class AuthRes {
    private final String jwt;
    private final String type;
}
