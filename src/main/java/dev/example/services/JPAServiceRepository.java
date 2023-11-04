package dev.example.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAServiceRepository extends JpaRepository<Service, String> {
    
}
