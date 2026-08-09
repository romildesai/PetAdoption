package com.example.petadoption.controller;

import com.example.petadoption.model.User;
import com.example.petadoption.service.AdoptionHistoryService;
import com.example.petadoption.service.AdoptionRequestService;
import com.example.petadoption.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/petAdoption/user")
public class UserController {

    private final UserService userService;
    private final AdoptionHistoryService adoptionHistoryService;
    private final AdoptionRequestService adoptionRequestService;

    public UserController(UserService userService, AdoptionHistoryService adoptionHistoryService, AdoptionRequestService adoptionRequestService) {
        this.userService = userService;
        this.adoptionHistoryService = adoptionHistoryService;
        this.adoptionRequestService = adoptionRequestService;
    }

    //login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //home page
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return "redirect:/petAdoption/user/login";
        }
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
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        //check email exist
        try {
            userService.registerUser(user);
            return "redirect:/petAdoption/user/login";
        } catch(IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    //View their own adoption history
    @GetMapping("/history")
    public String history(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return "redirect:/petAdoption/user/login";
        }
        model.addAttribute("history", adoptionHistoryService.getHistoryByUser(user.getId()));
        return "adoption-history";
    }

    //View their own applications
    @GetMapping("/applications")
    public String applications(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return "redirect:/petAdoption/user/login";
        }
        model.addAttribute("applications", adoptionRequestService.getRequestsByUser(user.getId()));
        return "my-applications";
    }
}
