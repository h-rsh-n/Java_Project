package com.example.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Models.Candidate;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Candidate findByname(String name);
}
