package com.example.petadoption.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_requests")
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reasonForAdoption;

    @Column(nullable = false)
    private String homeType;

    @Column(nullable = false)
    private Boolean hasOtherPets;

    @Column(columnDefinition = "TEXT")
    private String experienceWithPets;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public AdoptionRequest() {
    }

    @PrePersist
    public void setDefaultValues() {
        if (requestDate == null) {
            requestDate = LocalDateTime.now();
        }

        if (status == null) {
            status = AdoptionStatus.PENDING;
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getReasonForAdoption() {
        return reasonForAdoption;
    }

    public void setReasonForAdoption(String reasonForAdoption) {
        this.reasonForAdoption = reasonForAdoption;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public Boolean getHasOtherPets() {
        return hasOtherPets;
    }

    public void setHasOtherPets(Boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }

    public String getExperienceWithPets() {
        return experienceWithPets;
    }

    public void setExperienceWithPets(String experienceWithPets) {
        this.experienceWithPets = experienceWithPets;
    }

    public AdoptionStatus getStatus() {
        return status;
    }

    public void setStatus(AdoptionStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}