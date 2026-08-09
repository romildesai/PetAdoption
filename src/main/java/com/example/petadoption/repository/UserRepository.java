package com.example.petadoption.repository;

import com.example.petadoption.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Find user by email for login and account lookup
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
