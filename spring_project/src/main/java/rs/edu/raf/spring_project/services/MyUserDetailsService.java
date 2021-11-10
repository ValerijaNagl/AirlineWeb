package rs.edu.raf.spring_project.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.TypeOfUser;
import rs.edu.raf.spring_project.repositories.MyUserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepository userRepository;

    public MyUserDetailsService(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new User(myUser.getUsername(), myUser.getPassword(), getGrantedAuthorities(myUser));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(MyUser myUser) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(myUser.getType() != null && myUser.getType().toString().equals(TypeOfUser.ADMIN.toString())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return grantedAuthorities;
    }
}