package com.example.petadoption.service;

import com.example.petadoption.model.User;
import com.example.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //register
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    //login
    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return user.get();
            }
        }
        return null;
    }

    //get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


}
