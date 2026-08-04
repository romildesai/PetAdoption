package com.example.petadoption.controller;

import com.example.petadoption.model.AdoptionRequest;
import com.example.petadoption.model.AdoptionStatus;
import com.example.petadoption.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/petAdoption/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    //all adoption requests
    @GetMapping("/adoptionRequest")
    public String viewAllAdoptionRequest(Model m){
        m.addAttribute("viewRequest", adoptionRequestService.getAllRequests());
        return "admin";
    }

    //pending adoption requests
    @GetMapping("/pendingAdoptionRequest")
    public String viewPendingAdoptionRequest(Model m){
        m.addAttribute("viewPendingRequest", adoptionRequestService.getPendingRequests());
        return "admin";
    }

    //adoption request by id
    @GetMapping("/adoptionRequest/{id}")
    public String viewAdoptionRequestById(Model m,@PathVariable Long id){
        m.addAttribute("viewRequestById", adoptionRequestService.getRequestById(id));
        return "admin";
    }

    //approve adoption request
    @PostMapping("/adoptionRequest/{id}/approve")
    public String approveAdoptionRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AdoptionRequest request = adoptionRequestService.getRequestById(id);
        if (request == null) {
            throw new IllegalArgumentException("Adoption request not found");
        }

        if (request.getStatus() != AdoptionStatus.PENDING) {
            throw new IllegalStateException("This request has already been processed and cannot be approved again");
        }

        request.setStatus(AdoptionStatus.APPROVED);
        adoptionRequestService.save(request);

        redirectAttributes.addFlashAttribute("successMsg", "Adoption request approved successfully");
        return "redirect:/adoptionRequest";
        }

}
