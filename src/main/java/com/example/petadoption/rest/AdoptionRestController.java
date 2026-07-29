package com.example.petadoption.rest;

import com.example.petadoption.model.AdoptionRequest;
import com.example.petadoption.service.AdoptionRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionRestController {

    private final AdoptionRequestService
            adoptionRequestService;

    public AdoptionRestController(
            AdoptionRequestService adoptionRequestService
    ) {
        this.adoptionRequestService =
                adoptionRequestService;
    }

    /*
     * GET /api/adoptions
     */
    @GetMapping
    public ResponseEntity<List<AdoptionRequest>>
    getAllRequests() {

        return ResponseEntity.ok(
                adoptionRequestService.getAllRequests()
        );
    }

    /*
     * GET /api/adoptions/1
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<AdoptionRequest>
    getRequestById(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(
                adoptionRequestService
                        .getRequestById(requestId)
        );
    }

    /*
     * GET /api/adoptions/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AdoptionRequest>>
    getRequestsByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                adoptionRequestService
                        .getRequestsByUser(userId)
        );
    }

    /*
     * POST /api/adoptions
     *
     * Send values as form-data or x-www-form-urlencoded.
     */
    @PostMapping
    public ResponseEntity<AdoptionRequest>
    submitRequest(
            @RequestParam Long userId,
            @RequestParam Long petId,
            @RequestParam String reasonForAdoption,
            @RequestParam String homeType,
            @RequestParam Boolean hasOtherPets,
            @RequestParam(required = false)
            String experienceWithPets
    ) {
        AdoptionRequest savedRequest =
                adoptionRequestService.submitRequest(
                        userId,
                        petId,
                        reasonForAdoption,
                        homeType,
                        hasOtherPets,
                        experienceWithPets
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRequest);
    }

    /*
     * PUT /api/adoptions/1/approve
     */
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<AdoptionRequest>
    approveRequest(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(
                adoptionRequestService
                        .approveRequest(requestId)
        );
    }

    /*
     * PUT /api/adoptions/1/reject
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<AdoptionRequest>
    rejectRequest(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(
                adoptionRequestService
                        .rejectRequest(requestId)
        );
    }
}