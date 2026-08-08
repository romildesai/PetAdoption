package com.example.petadoption.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.petadoption.model.Pet;
import com.example.petadoption.model.PetStatus;
import com.example.petadoption.repository.PetRepository;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAvailablePets() {
        return petRepository.findByStatus(PetStatus.AVAILABLE);
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> searchPets(
            String species,
            String breed,
            Integer age) {

        species = convertBlankToNull(species);
        breed = convertBlankToNull(breed);

        return petRepository.searchPets(
                species,
                breed,
                age,
                PetStatus.AVAILABLE
        );
    }

    private String convertBlankToNull(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}