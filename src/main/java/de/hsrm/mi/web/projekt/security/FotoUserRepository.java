package de.hsrm.mi.web.projekt.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoUserRepository extends JpaRepository<FotoUser, String> {
    
}