package com.example.petadoption.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String publicHome() {
        return "index";
    }
}
