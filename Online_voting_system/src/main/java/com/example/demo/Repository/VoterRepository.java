package com.example.demo.Repository;

import com.example.demo.Entity.Voter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoterRepository extends MongoRepository<Voter, String> {
    Voter findByUsername(String username);
    long countByHasVoted(boolean hasVoted);
}
