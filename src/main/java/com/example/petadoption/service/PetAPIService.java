package com.example.petadoption.service;

import com.example.petadoption.model.Pet;
import com.example.petadoption.model.PetStatus;
import com.example.petadoption.repository.PetRepository;
import tools.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PetAPIService {

    private final PetRepository petRepository;
    private final RestTemplate restTemplate;

    private static final String API_URL =
            "https://dog.ceo/api/breeds/image/random/20";

    private final Random random = new Random();

    private final List<String> names = List.of(
            "Buddy",
            "Max",
            "Charlie",
            "Rocky",
            "Cooper",
            "Milo",
            "Teddy",
            "Bailey",
            "Leo",
            "Lucky",
            "Daisy",
            "Luna",
            "Bella",
            "Lucy",
            "Molly",
            "Ruby"
    );

    public PetAPIService(PetRepository petRepository) {
        this.petRepository = petRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Pet> fetchPets() {

        ResponseEntity<JsonNode> response =
                restTemplate.getForEntity(
                        API_URL,
                        JsonNode.class
                );

        JsonNode body = response.getBody();

        List<Pet> pets = new ArrayList<>();

        if (body == null) {
            return pets;
        }

        JsonNode images = body.path("message");

        for (JsonNode image : images) {

            String imageUrl = image.asText();

            String breed = getBreedFromImageUrl(imageUrl);

            String name = generateName();

            Pet pet = new Pet();

            pet.setName(name);
            pet.setSpecies("Dog");
            pet.setBreed(breed);
            pet.setAge(generateAge());
            pet.setGender(generateGender());

            pet.setDescription(
                    generateDescription(name, breed)
            );

            pet.setImageUrl(imageUrl);

            pet.setStatus(PetStatus.AVAILABLE);

            pets.add(pet);
        }

        return pets;
    }

    public void importPets() {

        if (petRepository.count() == 0) {

            List<Pet> pets = fetchPets();

            petRepository.saveAll(pets);
        }
    }

    private String getBreedFromImageUrl(String imageUrl) {

        String breedPart =
                imageUrl.split("/breeds/")[1]
                        .split("/")[0];

        breedPart = breedPart.replace("-", " ");

        String[] words = breedPart.split(" ");

        StringBuilder breed = new StringBuilder();

        for (String word : words) {

            breed.append(
                    Character.toUpperCase(
                            word.charAt(0)
                    )
            );

            breed.append(word.substring(1));

            breed.append(" ");
        }

        return breed.toString().trim();
    }

    private String generateName() {

        return names.get(
                random.nextInt(names.size())
        );
    }

    private Integer generateAge() {

        return random.nextInt(10) + 1;
    }

    private String generateGender() {

        if (random.nextBoolean()) {
            return "Male";
        }

        return "Female";
    }

    private String generateDescription(
            String name,
            String breed) {

        return name
                + " is a friendly "
                + breed
                + " looking for a loving home.";
    }
}
