package com.example.petadoption.controller;

import com.example.petadoption.model.User;
import com.example.petadoption.repository.UserRepository;
import com.example.petadoption.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/petAdoption/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //home page
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("user", user);
        return "home";
    }

    //register
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        //check email exist
        User registeredUser = userService.findByEmail(user.getEmail());
        if (registeredUser != null) {
            model.addAttribute("error", "Email already in use");
            return "register";
        }
        //save new register user
        userService.registerUser(user);
        return "redirect:/petAdoption/user/login";
    }

    //get all users
    @GetMapping("/search/users")
    public String findAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    //find user by full name
    @GetMapping("/search")
    public String findUserByFullName(@RequestParam String fullName, Model model) {
        List<User> users = userService.findByFullName(fullName);
        model.addAttribute("users", users);
        return "user-details";
    }

}
