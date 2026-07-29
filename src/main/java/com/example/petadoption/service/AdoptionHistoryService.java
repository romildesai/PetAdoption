package com.example.petadoption.service;

import com.example.petadoption.model.AdoptionHistory;
import com.example.petadoption.repository.AdoptionHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionHistoryService {

    private final AdoptionHistoryRepository
            adoptionHistoryRepository;

    public AdoptionHistoryService(
            AdoptionHistoryRepository
                    adoptionHistoryRepository
    ) {
        this.adoptionHistoryRepository =
                adoptionHistoryRepository;
    }

    public List<AdoptionHistory> getAllHistory() {
        return adoptionHistoryRepository
                .findAllByOrderByAdoptionDateDesc();
    }

    public List<AdoptionHistory> getHistoryByUser(
            Long userId
    ) {
        return adoptionHistoryRepository
                .findByUserIdOrderByAdoptionDateDesc(userId);
    }

    public AdoptionHistory getHistoryById(Long id) {
        return adoptionHistoryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adoption history not found."
                        )
                );
    }
}