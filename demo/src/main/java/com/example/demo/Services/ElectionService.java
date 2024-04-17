package com.example.demo.Services;
import org.springframework.stereotype.Service;
import com.example.demo.Models.Election;
import com.example.demo.Models.Voter;
import com.example.demo.Models.Candidate;
import com.example.demo.Repositories.CandidateRepository;
import com.example.demo.Repositories.ElectionRepository;
import com.example.demo.Repositories.VoterRepository;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Optional;

@Service
public class ElectionService {
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;

    public ElectionService(ElectionRepository electionRepository, CandidateRepository candidateRepository,
            VoterRepository voterRepository) {
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
    }

    
    public boolean vote(String electionId, String candidateId, String userId) {
        Election election = electionRepository.findById(electionId).orElse(null);
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        Voter user = voterRepository.findById(userId).orElse(null);
        if (user.getElections().contains(electionId)) {
            System.out.println("User has already voted in this election.");
            return false;
        }
        if (election != null && candidate != null && user != null) {
            
            // Update user's voted elections list
            user.getElections().add(electionId);
            System.out.println(user.getElections());
            voterRepository.save(user);

            // Update electionVotes in Election object
            int candidateVotes = election.getCandidateVotes().getOrDefault(candidateId, -1);
            election.getCandidateVotes().put(candidateId, candidateVotes + 1);
            System.out.println(election.getCandidateVotes());

            // Update electionVotes in Candidate object
            int electionVotes = candidate.getElectionVotes().getOrDefault(electionId, -1);
            candidate.getElectionVotes().put(electionId, electionVotes + 1);

            // Save updated objects
            electionRepository.save(election);
            candidateRepository.save(candidate);
            return true;
        }
        return false;

    }

    public void addCandidateToElection(String electionId, String candidateId) {
        System.out.println("addCandidateToElection");
        Election election = electionRepository.findById(electionId).orElse(null);
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (election != null && candidate != null) {
            int candidateVotes = election.getCandidateVotes().getOrDefault(candidateId, -1);
            election.getCandidateVotes().put(candidateId, candidateVotes + 1);
            electionRepository.save(election);

            int electionVotes = candidate.getElectionVotes().getOrDefault(electionId, -1);
            candidate.getElectionVotes().put(electionId, electionVotes + 1);
            candidateRepository.save(candidate);
        }
    }

    public void standForElection(String candidateId, String electionId) {
        System.out.println("standForElection");
        Election election = electionRepository.findById(electionId).orElse(null);
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (election != null) {
            if (candidate != null) {
                System.out.println("election and candidate prsent");
                if (!isCandidateFromSamePartyInElection(election, candidateId)) {
                    addCandidateToElection(electionId, candidateId);
                    return;
                } else {
                    System.out.println("Candidate from the same party already in the election.");
                    return;
                }
            }
            System.out.println("Candidate does not exist.");
        }
    }
    
    private boolean isCandidateFromSamePartyInElection(Election election, String candidateId) {
        System.out.println("isCandidateFromSamePartyInElection");
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            return true;
        }
        String candidateParty = candidate.getPartyAffiliation();
        for (String c : election.getCandidateVotes().keySet()) {
            Candidate ca = candidateRepository.findById(c).orElse(null);
            if (ca.getPartyAffiliation() != null && ca.getPartyAffiliation().equals(candidateParty)) {
                return true;
            }
        }
        return false;
    }
    
    public void declareWinner(Election election) {
        Candidate winner = null;
    int maxVotes = Integer.MIN_VALUE;

    for (Map.Entry<String, Integer> entry : election.getCandidateVotes().entrySet()) {
        if (entry.getValue() > maxVotes) {
            maxVotes = entry.getValue();
            winner = candidateRepository.findById(entry.getKey()).orElse(null);
            election.setStatus("completed");
            election.setCloseDate(new Date());
            election.setWinner(winner);
            electionRepository.save(election);
        }
    }
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

    public Election getElectionById(String electionId) {
        Optional<Election> optionalElection = electionRepository.findById(electionId);
        return optionalElection.orElse(null);
    }

}
