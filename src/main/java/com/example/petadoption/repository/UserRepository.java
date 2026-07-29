package com.example.petadoption.repository;

import com.example.petadoption.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //check whether the email and password exist in the database
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByFullNameContaining(String fullName);
}
