package com.example.petadoption.controller;

import com.example.petadoption.model.User;
import com.example.petadoption.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/petAdoption/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //register
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/petAdoption/user/profile";
    }

    //login
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpSession session) {
        User loginUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            return "redirect:/petAdoption/user/profile";
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    //logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/petAdoption/user/login";
    }

    //profile
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "redirect:/petAdoption/user/login";
    }

    //get user by id
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile";
        }
       return "error";

    }

}
