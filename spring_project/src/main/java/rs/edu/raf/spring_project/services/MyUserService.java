package rs.edu.raf.spring_project.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.Ticket;
import rs.edu.raf.spring_project.repositories.MyUserRepository;

import java.util.List;
import java.util.Optional;
@Service
public class MyUserService implements IService<MyUser, Long> {

    private final MyUserRepository userRepository;

    public MyUserService(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public MyUser save(MyUser var1) {
        return userRepository.save(var1);
    }

    @Override
    public Optional<MyUser> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    @Override
    public List<MyUser> findAll() {
        return (List<MyUser>) userRepository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    public MyUser findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
