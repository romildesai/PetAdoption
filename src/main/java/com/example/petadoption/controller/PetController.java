package com.example.petadoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    public String displayPets(Model model) {

        model.addAttribute(
                "pets",
                petService.getAvailablePets()
        );

        return "pets";
    }

    @GetMapping("/pets/{id}")
    public String displayPetDetails(
            @PathVariable Long id,
            Model model) {

        Pet pet = petService.getPetById(id);

        if (pet == null) {
            return "redirect:/pets";
        }

        model.addAttribute("pet", pet);

        return "pet-details";
    }

    @GetMapping("/pets/search")
    public String searchPets(
            @RequestParam(required = false)
            String species,

            @RequestParam(required = false)
            String breed,

            @RequestParam(required = false)
            Integer age,

            Model model) {

        model.addAttribute(
                "pets",
                petService.searchPets(
                        species,
                        breed,
                        age
                )
        );

        model.addAttribute("species", species);
        model.addAttribute("breed", breed);
        model.addAttribute("age", age);

        return "pets";
    }
}