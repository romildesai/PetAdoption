package com.example.petadoption.rest;

import com.example.petadoption.model.Pet;
import com.example.petadoption.service.PetService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    private final PetService petService;

    public PetRESTController(
            PetService petService) {

        this.petService = petService;
    }

    @GetMapping
    public List<Pet> getPets() {

        return petService.getAvailablePets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(
            @PathVariable Long id) {

        Pet pet =
                petService.getPetById(id);

        if (pet == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(pet);
    }

    @GetMapping("/search")
    public List<Pet> searchPets(

            @RequestParam(required = false)
            String species,

            @RequestParam(required = false)
            String breed,

            @RequestParam(required = false)
            Integer age) {

        return petService.searchPets(
                species,
                breed,
                age
        );
    }
}
