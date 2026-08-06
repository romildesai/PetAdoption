package com.example.petadoption.service;

import com.example.petadoption.model.User;
import com.example.petadoption.model.UserRole;
import com.example.petadoption.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //register
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
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
        return userRepository.findByFullNameContainingIgnoreCase(fullName);
    }
}
