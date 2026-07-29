package com.example.petadoption.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "adoption_history")
public class AdoptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate adoptionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "adoption_request_id",
            nullable = false,
            unique = true
    )
    private AdoptionRequest adoptionRequest;

    public AdoptionHistory() {
    }

    @PrePersist
    public void setDefaultDate() {
        if (adoptionDate == null) {
            adoptionDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
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

    public AdoptionRequest getAdoptionRequest() {
        return adoptionRequest;
    }

    public void setAdoptionRequest(
            AdoptionRequest adoptionRequest
    ) {
        this.adoptionRequest = adoptionRequest;
    }
}