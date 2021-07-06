package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FotoUserDetailsService implements UserDetailsService {
    @Autowired private FotoUserRepository FotoUserRepository;
    @Autowired private PasswordEncoder passwordencoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Schritt 1: notwendige Daten zu Login-Name 'username' besorgen, z.B. aus Datenbank
        FotoUser user = FotoUserRepository.findById(username).get();
        if (user == null) { throw new UsernameNotFoundException(username); }
        // Schritt 2: Spring 'User'-Objekt mit relevanten Daten für 'username' zurückgeben
        return org.springframework.security.core.userdetails.User
        .withUsername(username)
        .password(passwordencoder.encode(user.getPassword())) // falls in DB encoded gespeichert
        .roles("USER") // Rolle könnte auch aus DB kommen
        .build();
    }
}