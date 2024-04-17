package com.example.demo.Controllers;

import com.example.demo.Models.CandidateVotesAdapter;
import com.example.demo.Models.Election;
import com.example.demo.Repositories.CandidateRepository;
import com.example.demo.Services.ElectionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



import java.util.List;
import java.util.Map;

@Controller
public class ElectionController {

    private final ElectionService electionService;
    private final CandidateRepository candidateRepository;

    public ElectionController(ElectionService electionService, CandidateRepository candidateRepository) {
        this.electionService = electionService;
        this.candidateRepository = candidateRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @RequestParam("userId") String userId) {
        List<Election> pendingElections = electionService.getPendingElections();
        List<Election> ongoingElections = electionService.getOngoingElections();
        List<Election> completedElections = electionService.getCompletedElections();
        model.addAttribute("pendingElections", pendingElections);
        model.addAttribute("ongoingElections", ongoingElections);
        model.addAttribute("completedElections", completedElections);
        model.addAttribute("userId", userId);
        return "dashboard";
    }
    
    @GetMapping("/dashboard/electionDetails")
    public String viewElectionDetailsVoter(@RequestParam("electionId") String electionId,
            @RequestParam("userId") String userId, Model model) {
        CandidateVotesAdapter candidateVotesAdapter = new CandidateVotesAdapter(candidateRepository);
        Election election = electionService.getElectionById(electionId);
        model.addAttribute("userId", userId);
        model.addAttribute("election", election);
        Map<String, Integer> originalCandidateVotes = election.getCandidateVotes();
        Map<String, Integer> adaptedCandidateVotes = candidateVotesAdapter.adaptCandidateVotes(originalCandidateVotes);
        model.addAttribute("candidatesVotes", adaptedCandidateVotes);
        return "election-details-voter";
    }
    
    @GetMapping("/dashboard/electionDetails/vote")
    public String makeVote(@RequestParam("electionId") String electionId,
            @RequestParam("userId") String userId, @RequestParam("candidateId") String candidateId) {
        electionService.vote(electionId, candidateId, userId);
        return "redirect:/dashboard/electionDetails?userId=" + userId + "&electionId=" + electionId;
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
            electionService.declareWinner(election);
        }
        return "redirect:/adminDashboard";
    }

    @GetMapping("/adminDashboard/electionDetails")
    public String viewElectionDetailsAdmin(@RequestParam("electionId") String electionId, Model model) {
        Election election = electionService.getElectionById(electionId);
        model.addAttribute("election", election);
        CandidateVotesAdapter candidateVotesAdapter = new CandidateVotesAdapter(candidateRepository);
        Map<String, Integer> originalCandidateVotes = election.getCandidateVotes();
        Map<String, Integer> adaptedCandidateVotes = candidateVotesAdapter.adaptCandidateVotes(originalCandidateVotes);
        model.addAttribute("candidatesVotes", adaptedCandidateVotes);
        System.out.println(adaptedCandidateVotes);
        return "election-details-admin";
    }

    @GetMapping("/candidateDashboard/electionDetails")
    public String viewElectionDetailsCandidate(@RequestParam("userId") String userId, @RequestParam("electionId") String electionId, Model model) {
        Election election = electionService.getElectionById(electionId);
        System.out.println(userId);
        model.addAttribute("election", election);
        model.addAttribute("userId", userId);
        CandidateVotesAdapter candidateVotesAdapter = new CandidateVotesAdapter(candidateRepository);
        Map<String, Integer> originalCandidateVotes = election.getCandidateVotes();
        Map<String, Integer> adaptedCandidateVotes = candidateVotesAdapter.adaptCandidateVotes(originalCandidateVotes);
        model.addAttribute("candidatesVotes", adaptedCandidateVotes);
        return "election-details-candidate";
    }


    @GetMapping("/election/candidateDashboard")
    public String candidateDashboard(@RequestParam("userId") String userId, Model model) {
    List<Election> pendingElections = electionService.getPendingElections();
    List<Election> ongoingElections = electionService.getOngoingElections();
    List<Election> completedElections = electionService.getCompletedElections();
    model.addAttribute("userId", userId);
    model.addAttribute("pendingElections", pendingElections);
    model.addAttribute("ongoingElections", ongoingElections);
    model.addAttribute("completedElections", completedElections);
    return "candidate-dashboard";
}


@PostMapping("/candidateDashboard/standForElection")
public String standForElection(@RequestParam("userId") String userId, @RequestParam("electionId") String electionId,
        Model model) {
        System.out.println("/candidateDashboard/standForElection");
        model.addAttribute("userId", userId);
        electionService.standForElection(userId, electionId);
        return "redirect:/candidateDashboard?userId=" + userId;
    }
}
