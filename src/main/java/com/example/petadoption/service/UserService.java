package com.example.petadoption.service;

import com.example.petadoption.model.User;
import com.example.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //register
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    //get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    //search user by full name
    public List<User> findByFullName(String fullName) {
        return userRepository.findByFullNameContaining(fullName);
    }
}
