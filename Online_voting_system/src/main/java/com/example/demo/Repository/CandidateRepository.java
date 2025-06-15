package com.example.demo.Repository;

import com.example.demo.Entity.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
}
