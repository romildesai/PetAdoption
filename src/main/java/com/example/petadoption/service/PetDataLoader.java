package com.example.petadoption.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PetDataLoader implements ApplicationRunner {

    private final PetAPIService petAPIService;

    public PetDataLoader(PetAPIService petAPIService) {
        this.petAPIService = petAPIService;
    }

    @Override
    public void run(ApplicationArguments args) {
        petAPIService.importPets();
    }
}
