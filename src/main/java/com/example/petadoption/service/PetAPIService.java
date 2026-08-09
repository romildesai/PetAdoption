package com.example.petadoption.service;

import com.example.petadoption.model.Pet;
import com.example.petadoption.model.PetStatus;
import com.example.petadoption.repository.PetRepository;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetAPIService {

    private final PetRepository petRepository;
    private final RestTemplate restTemplate;

    @Value("${rescuegroups.api.url}")
    private String apiUrl;

    @Value("${rescuegroups.api.key}")
    private String apiKey;

    public PetAPIService(PetRepository petRepository) {
        this.petRepository = petRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Pet> fetchPets() {

        String url = apiUrl
                + "/public/animals/search/available/haspic/"
                + "?limit=25&include=species";

        HttpHeaders headers = new HttpHeaders();

        headers.set(
                HttpHeaders.CONTENT_TYPE,
                "application/vnd.api+json"
        );

        headers.set(
                HttpHeaders.AUTHORIZATION,
                apiKey
        );

        HttpEntity<Void> request =
                new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        JsonNode.class
                );

        JsonNode body = response.getBody();

        List<Pet> pets = new ArrayList<>();

        if (body == null) {
            return pets;
        }

        Map<String, String> speciesMap =
                getSpecies(body);

        for (JsonNode animal : body.path("data")) {

            JsonNode attributes =
                    animal.path("attributes");

            String speciesId =
                    animal.path("relationships")
                            .path("species")
                            .path("data")
                            .path("id")
                            .asText();

            String species =
                    speciesMap.get(speciesId);

            Pet pet = new Pet();

            pet.setName(
                    getText(attributes, "name")
            );

            pet.setSpecies(species);

            pet.setBreed(
                    getText(attributes, "breedPrimary")
            );

            pet.setAge(
                    convertAge(
                            getText(
                                    attributes,
                                    "ageString"
                            )
                    )
            );

            pet.setGender(
                    getText(attributes, "sex")
            );

            pet.setDescription(
                    getText(
                            attributes,
                            "descriptionText"
                    )
            );

            pet.setImageUrl(
                    getText(
                            attributes,
                            "pictureThumbnailUrl"
                    )
            );

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

    private Map<String, String> getSpecies(
            JsonNode body) {

        Map<String, String> speciesMap =
                new HashMap<>();

        for (JsonNode item : body.path("included")) {

            if (item.path("type")
                    .asText()
                    .equals("species")) {

                String id =
                        item.path("id").asText();

                String species =
                        item.path("attributes")
                                .path("singular")
                                .asText();

                speciesMap.put(id, species);
            }
        }

        return speciesMap;
    }

    private String getText(
            JsonNode attributes,
            String field) {

        JsonNode value = attributes.get(field);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asText();
    }

    private Integer convertAge(String ageString) {

        if (ageString == null ||
                ageString.isBlank()) {

            return null;
        }

        String lower =
                ageString.toLowerCase();

        String[] parts =
                lower.split(" ");

        for (int i = 0;
             i < parts.length - 1;
             i++) {

            if (parts[i + 1]
                    .startsWith("year")) {

                try {
                    return Integer.parseInt(
                            parts[i]
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        if (lower.contains("month")) {
            return 0;
        }

        return null;
    }
}
