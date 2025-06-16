package com.example.demo.Repository;

import com.example.demo.Entity.VotingConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotingConfigRepository extends MongoRepository<VotingConfig, String> {
}
