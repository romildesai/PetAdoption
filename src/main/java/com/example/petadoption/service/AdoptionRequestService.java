package com.example.petadoption.service;

import com.example.petadoption.model.AdoptionHistory;
import com.example.petadoption.model.AdoptionRequest;
import com.example.petadoption.model.AdoptionStatus;
import com.example.petadoption.model.Pet;
import com.example.petadoption.model.PetStatus;
import com.example.petadoption.model.User;
import com.example.petadoption.repository.AdoptionHistoryRepository;
import com.example.petadoption.repository.AdoptionRequestRepository;
import com.example.petadoption.repository.PetRepository;
import com.example.petadoption.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final AdoptionHistoryRepository adoptionHistoryRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public AdoptionRequestService(
            AdoptionRequestRepository adoptionRequestRepository,
            AdoptionHistoryRepository adoptionHistoryRepository,
            UserRepository userRepository,
            PetRepository petRepository
    ) {
        this.adoptionRequestRepository =
                adoptionRequestRepository;

        this.adoptionHistoryRepository =
                adoptionHistoryRepository;

        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @Transactional
    public AdoptionRequest submitRequest(
            Long userId,
            Long petId,
            String reasonForAdoption,
            String homeType,
            Boolean hasOtherPets,
            String experienceWithPets
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with ID: " + userId
                        )
                );

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pet not found with ID: " + petId
                        )
                );

        if (pet.getStatus() == PetStatus.ADOPTED) {
            throw new IllegalStateException(
                    "This pet has already been adopted."
            );
        }

        boolean pendingRequestExists =
                adoptionRequestRepository
                        .existsByUserIdAndPetIdAndStatus(
                                userId,
                                petId,
                                AdoptionStatus.PENDING
                        );

        if (pendingRequestExists) {
            throw new IllegalStateException(
                    "You already submitted a request for this pet."
            );
        }

        AdoptionRequest adoptionRequest =
                new AdoptionRequest();

        adoptionRequest.setUser(user);
        adoptionRequest.setPet(pet);
        adoptionRequest.setRequestDate(LocalDateTime.now());
        adoptionRequest.setReasonForAdoption(
                reasonForAdoption
        );
        adoptionRequest.setHomeType(homeType);
        adoptionRequest.setHasOtherPets(hasOtherPets);
        adoptionRequest.setExperienceWithPets(
                experienceWithPets
        );
        adoptionRequest.setStatus(AdoptionStatus.PENDING);

        return adoptionRequestRepository.save(
                adoptionRequest
        );
    }

    public List<AdoptionRequest> getAllRequests() {
        return adoptionRequestRepository
                .findAllByOrderByRequestDateDesc();
    }

    public List<AdoptionRequest> getPendingRequests() {
        return adoptionRequestRepository
                .findByStatusOrderByRequestDateAsc(
                        AdoptionStatus.PENDING
                );
    }

    public List<AdoptionRequest> getRequestsByUser(
            Long userId
    ) {
        return adoptionRequestRepository
                .findByUserIdOrderByRequestDateDesc(userId);
    }

    public AdoptionRequest getRequestById(Long requestId) {
        return adoptionRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adoption request not found."
                        )
                );
    }

    @Transactional
    public AdoptionRequest approveRequest(Long requestId) {
        AdoptionRequest adoptionRequest =
                getRequestById(requestId);

        if (adoptionRequest.getStatus()
                != AdoptionStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending requests can be approved."
            );
        }

        Pet pet = adoptionRequest.getPet();

        if (pet.getStatus() == PetStatus.ADOPTED) {
            throw new IllegalStateException(
                    "This pet has already been adopted."
            );
        }

        // Approve the selected request
        adoptionRequest.setStatus(
                AdoptionStatus.APPROVED
        );

        // Mark pet as adopted
        pet.setStatus(PetStatus.ADOPTED);

        adoptionRequestRepository.save(adoptionRequest);
        petRepository.save(pet);

        // Reject all other pending requests for the same pet
        List<AdoptionRequest> otherRequests =
                adoptionRequestRepository
                        .findByPetIdAndStatus(
                                pet.getId(),
                                AdoptionStatus.PENDING
                        );

        for (AdoptionRequest otherRequest : otherRequests) {
            if (!otherRequest.getId().equals(requestId)) {
                otherRequest.setStatus(
                        AdoptionStatus.REJECTED
                );
            }
        }

        adoptionRequestRepository.saveAll(otherRequests);

        // Create adoption history
        boolean historyExists =
                adoptionHistoryRepository
                        .existsByAdoptionRequestId(requestId);

        if (!historyExists) {
            AdoptionHistory history =
                    new AdoptionHistory();

            history.setUser(adoptionRequest.getUser());
            history.setPet(pet);
            history.setAdoptionRequest(adoptionRequest);
            history.setAdoptionDate(LocalDate.now());

            adoptionHistoryRepository.save(history);
        }

        return adoptionRequest;
    }

    @Transactional
    public AdoptionRequest rejectRequest(Long requestId) {
        AdoptionRequest adoptionRequest =
                getRequestById(requestId);

        if (adoptionRequest.getStatus()
                != AdoptionStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending requests can be rejected."
            );
        }

        adoptionRequest.setStatus(
                AdoptionStatus.REJECTED
        );

        return adoptionRequestRepository.save(
                adoptionRequest
        );
    }

    //save request for admin controller
    public void save(AdoptionRequest request) {
        adoptionRequestRepository.save(request);
    }
}