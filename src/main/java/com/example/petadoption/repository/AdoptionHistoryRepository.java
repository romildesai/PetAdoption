package com.example.petadoption.repository;

import com.example.petadoption.model.AdoptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionHistoryRepository
        extends JpaRepository<AdoptionHistory, Long> {

    List<AdoptionHistory> findAllByOrderByAdoptionDateDesc();

    List<AdoptionHistory> findByUserIdOrderByAdoptionDateDesc(
            Long userId
    );

    boolean existsByAdoptionRequestId(Long adoptionRequestId);
}