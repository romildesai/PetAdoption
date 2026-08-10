package com.example.petadoption.controller;

import com.example.petadoption.model.Pet;
import com.example.petadoption.repository.PetRepository;
import com.example.petadoption.repository.UserRepository;
import com.example.petadoption.service.AdoptionHistoryService;
import com.example.petadoption.service.AdoptionRequestService;
import com.example.petadoption.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.petadoption.model.User;

@Controller
public class AdoptionController {

    private final AdoptionRequestService
            adoptionRequestService;

    private final AdoptionHistoryService
            adoptionHistoryService;

    private final PetRepository petRepository;
    private final UserService userService;

    public AdoptionController(
            AdoptionRequestService adoptionRequestService,
            AdoptionHistoryService adoptionHistoryService,
            PetRepository petRepository,
            UserService userService
    ) {
        this.adoptionRequestService =
                adoptionRequestService;

        this.adoptionHistoryService =
                adoptionHistoryService;

        this.petRepository = petRepository;

        this.userService = userService;
    }

    /*
     * Display adoption form.
     *
     * Example:
     * /adoptions/new/2
     */
    @GetMapping("/adoptions/new/{petId}")
    public String showAdoptionForm(
            @PathVariable Long petId,
            Authentication authentication,
            Model model
    ) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pet not found."
                        )
                );

        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("pet", pet);
        model.addAttribute("user", user);

        return "adoption-form";
    }

    /*
     * Submit adoption application.
     *
     * The HTML form must send:
     * userId
     * petId
     * reasonForAdoption
     * homeType
     * hasOtherPets
     * experienceWithPets
     */
    @PostMapping("/adoptions")
    public String submitRequest(
            @RequestParam Long userId,
            @RequestParam Long petId,
            @RequestParam String reasonForAdoption,
            @RequestParam String homeType,
            @RequestParam Boolean hasOtherPets,
            @RequestParam(required = false)
            String experienceWithPets,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adoptionRequestService.submitRequest(
                    userId,
                    petId,
                    reasonForAdoption,
                    homeType,
                    hasOtherPets,
                    experienceWithPets
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Adoption request submitted successfully."
            );

            return "redirect:/user/applications?userId="
                    + userId;

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "redirect:/adoptions/new/" + petId;
        }
    }

    /*
     * Show one user's applications.
     *
     * Example:
     * /user/applications?userId=1
     */
    @GetMapping("/user/applications")
    public String showUserApplications(
            @RequestParam Long userId,
            Model model
    ) {
        model.addAttribute(
                "applications",
                adoptionRequestService
                        .getRequestsByUser(userId)
        );

        model.addAttribute("userId", userId);

        return "my-applications";
    }

    /*
     * Admin: show all applications.
     */
    @GetMapping("/admin/applications")
    public String showAllApplications(Model model) {
        model.addAttribute(
                "applications",
                adoptionRequestService.getAllRequests()
        );

        return "admin-applications";
    }

    /*
     * Admin: approve an application.
     */
    @PostMapping(
            "/admin/applications/{requestId}/approve"
    )
    public String approveRequest(
            @PathVariable Long requestId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adoptionRequestService.approveRequest(
                    requestId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Adoption request approved successfully."
            );

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/applications";
    }

    /*
     * Admin: reject an application.
     */
    @PostMapping(
            "/admin/applications/{requestId}/reject"
    )
    public String rejectRequest(
            @PathVariable Long requestId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adoptionRequestService.rejectRequest(
                    requestId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Adoption request rejected."
            );

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/applications";
    }

    /*
     * Admin: view complete adoption history.
     */
    @GetMapping("/admin/history")
    public String showAllAdoptionHistory(
            Model model
    ) {
        model.addAttribute(
                "history",
                adoptionHistoryService.getAllHistory()
        );

        return "adoption-history";
    }

    /*
     * User: view their adoption history.
     */
    @GetMapping("/user/history")
    public String showUserAdoptionHistory(
            Authentication authentication,
            Model model
    ) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute(
                "history",
                adoptionHistoryService
                        .getHistoryByUser(user.getId())
        );
        model.addAttribute("user", user);
        return "user-adoption-history";
    }
}