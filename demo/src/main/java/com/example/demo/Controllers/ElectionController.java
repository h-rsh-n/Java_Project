package com.example.demo.Controllers;

import com.example.demo.Models.Election;
import com.example.demo.Models.Candidate;
import com.example.demo.Services.ElectionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Election> pendingElections = electionService.getPendingElections();
        List<Election> ongoingElections = electionService.getOngoingElections();
        List<Election> completedElections = electionService.getCompletedElections();
        model.addAttribute("pendingElections", pendingElections);
        model.addAttribute("ongoingElections", ongoingElections);
        model.addAttribute("completedElections", completedElections);
        return "dashboard";
    }

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(Model model) {
        List<Election> pendingElections = electionService.getPendingElections();
        List<Election> ongoingElections = electionService.getOngoingElections();
        List<Election> completedElections = electionService.getCompletedElections();
        model.addAttribute("pendingElections", pendingElections);
        model.addAttribute("ongoingElections", ongoingElections);
        model.addAttribute("completedElections", completedElections);
        return "admin-dashboard";
    }

    @PostMapping("/adminDashboard/addElection")
    public String addElection(@RequestParam("name") String name) {
        Election election = new Election(name);
        electionService.addElection(election);
        return "redirect:/adminDashboard";
    }
    
    @PostMapping("/adminDashboard/startElection")
    public String startElection(@RequestParam("electionId") String electionId) {
        Election election = electionService.getElectionById(electionId);
        if (election != null) {
            electionService.startElection(election);
        }
        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/closeElection")
    public String closeElection(@RequestParam("electionId") String electionId) {
        Election election = electionService.getElectionById(electionId);
        if (election != null) {
            electionService.closeElection(election);
        }
        return "redirect:/adminDashboard";
    }

    @GetMapping("/adminDashboard/electionDetails")
    public String viewElectionDetailsAdmin(@RequestParam("electionId") String electionId, Model model) {
        Election election = electionService.getElectionById(electionId);
        model.addAttribute("election", election);
        return "election-details-admin";
    }

    @GetMapping("/candidateDashboard/electionDetails")
    public String viewElectionDetailsCandidate(@ModelAttribute("candidateName") String candidateName,  @RequestParam("electionId") String electionId, Model model) {
        Election election = electionService.getElectionById(electionId);
        model.addAttribute("election", election);
        model.addAttribute("candidateName", candidateName);
        return "election-details-candidate";
    }

    @GetMapping("/dashboard/electionDetails")
    public String viewElectionDetailsVoter(@RequestParam("electionId") String electionId, Model model) {
        Election election = electionService.getElectionById(electionId);
        model.addAttribute("election", election);
        return "election-details-voter";
    }


    @GetMapping("/election/candidateDashboard")
public String candidateDashboard(@RequestParam("candidateName") String candidateName, Model model) {
    List<Election> pendingElections = electionService.getPendingElections();
    List<Election> ongoingElections = electionService.getOngoingElections();
    List<Election> completedElections = electionService.getCompletedElections();
    model.addAttribute("pendingElections", pendingElections);
    model.addAttribute("ongoingElections", ongoingElections);
    model.addAttribute("completedElections", completedElections);
    model.addAttribute("candidateName", candidateName);
    return "candidate-dashboard";
}


    @PostMapping("/candidateDashboard/standForElection")
    public String standForElection(@RequestParam("electionId") String electionId,
    @RequestParam("candidateName") String candidateName) {
        electionService.standForElection(candidateName, electionId);
        return "redirect:/candidateDashboard";
    }
}
