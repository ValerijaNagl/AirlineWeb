package rs.edu.raf.spring_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.spring_project.model.auth.AuthReq;
import rs.edu.raf.spring_project.model.auth.AuthRes;
import rs.edu.raf.spring_project.services.MyUserDetailsService;
import rs.edu.raf.spring_project.util.JwtUtil;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@EnableWebSecurity
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final DaoAuthenticationProvider authenticationProvider;


    public AuthController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtil jwtUtil, DaoAuthenticationProvider authProvider, DaoAuthenticationProvider authenticationProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthReq authReq){

        System.out.println(authReq.toString());
        try {
            // authenticationProvider smo setovali password encoder, tako da sa autentikaciju saljemo obican string,
            // a authentication provider proverava u bazi gde je sacuvana enkriptovana sifra
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).body("User didn't pass authentication!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authReq.getUsername());

        System.out.println(userDetails.getAuthorities().toString());
        return ResponseEntity.ok(new AuthRes(jwtUtil.generateToken(userDetails), userDetails.getAuthorities().toString()));
    }

}
