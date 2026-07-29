package com.example.petadoption.repository;

import com.example.petadoption.model.AdoptionRequest;
import com.example.petadoption.model.AdoptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRequestRepository
        extends JpaRepository<AdoptionRequest, Long> {

    List<AdoptionRequest> findAllByOrderByRequestDateDesc();

    List<AdoptionRequest> findByUserIdOrderByRequestDateDesc(
            Long userId
    );

    List<AdoptionRequest> findByStatusOrderByRequestDateAsc(
            AdoptionStatus status
    );

    List<AdoptionRequest> findByPetIdAndStatus(
            Long petId,
            AdoptionStatus status
    );

    boolean existsByUserIdAndPetIdAndStatus(
            Long userId,
            Long petId,
            AdoptionStatus status
    );
}