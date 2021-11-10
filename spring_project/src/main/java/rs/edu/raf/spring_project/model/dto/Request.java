package rs.edu.raf.spring_project.model.dto;

import lombok.Data;

@Data
public class Request<T> {
    private RequestType type;
    private T body;
}
