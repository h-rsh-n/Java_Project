package com.example.demo.Models;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.Repositories.CandidateRepository;

public class CandidateVotesAdapter {
    
    private final CandidateRepository candidateRepository;
    
    public CandidateVotesAdapter(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }
    
    public Map<String, Integer> adaptCandidateVotes(Map<String, Integer> originalCandidateVotes) {
        Map<String, Integer> adaptedCandidateVotes = new HashMap<>();
        
        // Iterate through the original candidateVotes map and populate the adapted map with names
        for (Map.Entry<String, Integer> entry : originalCandidateVotes.entrySet()) {
            Candidate candidate = candidateRepository.findById(entry.getKey()).orElse(null);
            if (candidate != null) {
                adaptedCandidateVotes.put(candidate.getName(), entry.getValue());
            }
        }
        
        return adaptedCandidateVotes;
    }
}
