package rs.edu.raf.spring_project.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Response<T> {
    private ResponseStatus status;
    private List<T> body;
}
