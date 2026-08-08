package com.example.petadoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petadoption.model.Pet;
import com.example.petadoption.model.PetStatus;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByStatus(PetStatus status);

    @Query("""
            SELECT p
            FROM Pet p
            WHERE (:species IS NULL
                   OR LOWER(p.species) = LOWER(:species))
              AND (:breed IS NULL
                   OR LOWER(p.breed)
                   LIKE LOWER(CONCAT('%', :breed, '%')))
              AND (:age IS NULL
                   OR p.age = :age)
              AND p.status = :status
            """)
    List<Pet> searchPets(
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("age") Integer age,
            @Param("status") PetStatus status
    );
}