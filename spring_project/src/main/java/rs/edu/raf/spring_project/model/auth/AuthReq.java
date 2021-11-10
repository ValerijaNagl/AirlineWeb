package rs.edu.raf.spring_project.model.auth;

import lombok.Data;

@Data
public class AuthReq {
    private String username;
    private String password;
}
