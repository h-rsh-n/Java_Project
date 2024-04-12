package com.example.demo.Services;
import org.springframework.stereotype.Service;
import com.example.demo.Models.Candidate;
import com.example.demo.Repositories.CandidateRepository;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }
    public Candidate createCandidate(String name) {
        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidateRepository.save(candidate);
        return candidate;
    }
    public Candidate getCandidateByName(String name) {
        return candidateRepository.findByname(name);
    }
    public boolean existsCandidate(String name) {
        return getCandidateByName(name) != null;
    }
    public void joinParty(String candidateName, String party) {
        Candidate candidate = candidateRepository.findByname(candidateName);
        candidate.setPartyAffiliation(party);
        candidateRepository.save(candidate);
    }
}