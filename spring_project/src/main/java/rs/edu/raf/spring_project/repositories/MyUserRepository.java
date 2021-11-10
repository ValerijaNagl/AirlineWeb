package rs.edu.raf.spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.spring_project.model.auth.MyUser;

import java.util.List;

public interface MyUserRepository extends CrudRepository<MyUser, Long> {
    MyUser findByUsername(String username);

}
