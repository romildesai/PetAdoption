package com.example.petadoption.controller;

import com.example.petadoption.service.AdoptionHistoryService;
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
    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

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
    public String approveAdoptionRequest(@PathVariable Long id, RedirectAttributes redirectAttributes){
        adoptionRequestService.approveRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "Adoption request approved successfully");
        return "redirect:/petAdoption/admin/adoptionRequest";

    }

    //reject adoption request
    @PostMapping("/adoptionRequest/{id}/reject")
    public String rejectAdoptionRequest(@PathVariable Long id, RedirectAttributes redirectAttributes){
       adoptionRequestService.rejectRequest(id);
       redirectAttributes.addFlashAttribute("successMessage", "Adoption request rejected");
       return "redirect:/petAdoption/admin/adoptionRequest";
    }

    //view all history
    @GetMapping("/adoption/history")
    public String viewAdoptionHistory(Model m){
        m.addAttribute("viewAdoptionHistory", adoptionHistoryService.getAllHistory());
        return "adoption-history";
    }

    //view history by id
    @GetMapping("/adoption/history/{id}")
    public String viewAdoptionHistory(@PathVariable Long id ,Model m){
        m.addAttribute("viewAdoptionHistory", adoptionHistoryService.getHistoryById(id));
        return "adoption-history";
    }


}
