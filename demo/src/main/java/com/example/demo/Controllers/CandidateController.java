package com.example.demo.Controllers;

import org.springframework.ui.Model;
import com.example.demo.Services.CandidateService;
import com.example.demo.Models.Candidate;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("candidateName")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/candidateLogin")
    public String candidateLogin() {
        return "candidate-login";
    }
    
    @PostMapping("/candidateLogin")
    public String candidateLogin(@RequestParam("name") String name, Model model) {
        if (!candidateService.existsCandidate(name)) {
            candidateService.createCandidate(name);
        }
        model.addAttribute("candidateName", name);
        return "redirect:/candidateDashboard";
    }

    @GetMapping("/selectParty")
    public String selectParty(Model model) {
        List<String> partyOptions = Arrays.asList("Party A", "Party B", "Party C");
        model.addAttribute("partyOptions", partyOptions);
        return "select-party";
    }

    @PostMapping("/joinParty")
    public String joinParty(@RequestParam("party") String party, @ModelAttribute("candidateName") String candidateName) {
        candidateService.joinParty(candidateName, party);
        return "redirect:/candidateDashboard";
    }

    @GetMapping("/candidateDashboard")
    public String candidateDashboard(Model model) {
        String candidateName = (String) model.getAttribute("candidateName");
        Candidate candidate = candidateService.getCandidateByName(candidateName);
        
        if (candidate.getPartyAffiliation() == null || candidate.getPartyAffiliation().isEmpty()) {
            return "redirect:/selectParty";
        } else {
            return "forward:/election/candidateDashboard?candidateName=" + candidateName;
        }
    }
}
