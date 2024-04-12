package com.example.demo.Services;
import org.springframework.stereotype.Service;
import com.example.demo.Models.Election;
import com.example.demo.Models.Candidate;
import com.example.demo.Repositories.CandidateRepository;
import com.example.demo.Repositories.ElectionRepository;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@Service
public class ElectionService {
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public ElectionService(ElectionRepository electionRepository, CandidateRepository candidateRepository) {
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
    }

    public void addCandidateToElection(String electionId, String candidateName) {
        Election election = electionRepository.findById(electionId).orElse(null);
        if (election != null) {
            List<Candidate> candidates = election.getCandidates();
            Candidate candidate = candidateRepository.findByname(candidateName);
            candidates.add(candidate);
            election.setCandidates(candidates);
            electionRepository.save(election);
        }
    }

    public void standForElection(String candidateName, String electionId) {
        Election election = electionRepository.findById(electionId).orElse(null);
        if (election != null) {
            if (candidateName != null) {
                if (!isCandidateFromSamePartyInElection(election, candidateName)) {
                    addCandidateToElection(electionId, candidateName);
                    return;
                } else {
                    System.out.println("Candidate from the same party already in the election.");
                    return;
                }
            }
            System.out.println("Candidate does not exist.");
        }
    }
    
    private boolean isCandidateFromSamePartyInElection(Election election, String candidateName) {
        Candidate candidate = candidateRepository.findByname(candidateName);
        if (candidate == null) {
            return true;
        }
        String candidateParty = candidate.getPartyAffiliation();
        for (Candidate c : election.getCandidates()) {
            if (c.getPartyAffiliation() != null && c.getPartyAffiliation().equals(candidateParty)) {
                return true;
            }
        }
        return false;
    }
    
    
    public List<Election> getPendingElections() {
        return electionRepository.findByStatus("pending");
    }
    public List<Election> getOngoingElections() {
        return electionRepository.findByStatus("ongoing");
    }
    public List<Election> getCompletedElections() {
        return electionRepository.findByStatus("completed");
    }

    public void addElection(Election election) {
        electionRepository.save(election);
    }

    public void startElection(Election election) {
        election.setStatus("ongoing");
        election.setOpenDate(new Date());
        electionRepository.save(election);
    }

    public void closeElection(Election election) {
        election.setStatus("completed");
        election.setCloseDate(new Date());
        electionRepository.save(election);
    }

    public Election getElectionById(String electionId) {
        Optional<Election> optionalElection = electionRepository.findById(electionId);
        return optionalElection.orElse(null);
    }

}
