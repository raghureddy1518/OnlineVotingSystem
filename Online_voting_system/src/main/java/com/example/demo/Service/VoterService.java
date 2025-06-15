package com.example.demo.Service;

import com.example.demo.Entity.Voter;
import com.example.demo.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    public Voter registerVoter(String username, String password) {
        if (voterRepository.findByUsername(username) != null) {
            return null; // username already exists
        }
        Voter voter = new Voter(username, password);
        return voterRepository.save(voter);
    }

    public Voter authenticateVoter(String username, String password) {
        Voter voter = voterRepository.findByUsername(username);
        if (voter != null && voter.getPassword().equals(password)) {
            return voter;
        }
        return null;
    }

    public Voter findByUsername(String username) {
        return voterRepository.findByUsername(username);
    }
}
