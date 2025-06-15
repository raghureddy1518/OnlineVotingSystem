package com.example.demo.Controller;

import com.example.demo.Entity.Candidate;
import com.example.demo.Entity.Voter;
import com.example.demo.Repository.CandidateRepository;
import com.example.demo.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class VoterController {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Value("${voting.start}")
    private String votingStart;

    @Value("${voting.deadline}")
    private String votingDeadline;

    private String getVotingStatus() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(votingStart, formatter);
        LocalDateTime deadline = LocalDateTime.parse(votingDeadline, formatter);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(start)) {
            return "not_started";
        } else if (now.isAfter(deadline)) {
            return "closed";
        } else {
            return "open";
        }
    }

    @GetMapping("/voter/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/voter/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        Voter voter = voterRepository.findByUsername(username);
        if (voter != null && voter.getPassword().equals(password)) {

            String status = getVotingStatus();
            if (status.equals("not_started")) {
                model.addAttribute("error", "Voting has not started yet.");
                return "message";
            } else if (status.equals("closed")) {
                model.addAttribute("error", "Voting is closed.");
                return "message";
            }

            if (voter.isHasVoted()) {
                model.addAttribute("message", "You have already voted.");
            }

            List<Candidate> candidates = candidateRepository.findAll();
            model.addAttribute("voter", voter);
            model.addAttribute("username", username);
            model.addAttribute("candidates", candidates);
            return "vote";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/voter/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/voter/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (voterRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already registered");
            return "register";
        }

        Voter voter = new Voter(username, password);
        voter.setHasVoted(false);
        voterRepository.save(voter);
        return "redirect:/voter/login";
    }

    @PostMapping("/vote")
    public String castVote(@RequestParam("username") String username,
                           @RequestParam("candidateId") String candidateId,
                           Model model) {

        String status = getVotingStatus();
        if (status.equals("not_started")) {
            model.addAttribute("error", "Voting has not started yet.");
            return "message";
        } else if (status.equals("closed")) {
            model.addAttribute("error", "Voting is closed. You cannot vote now.");
            return "message";
        }

        Voter voter = voterRepository.findByUsername(username);
        if (voter == null) {
            model.addAttribute("error", "Voter not found.");
            return "message";
        }

        if (voter.isHasVoted()) {
            model.addAttribute("message", "You have already voted.");
            return "message";
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            model.addAttribute("error", "Candidate not found.");
            return "message";
        }

        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        voter.setHasVoted(true);
        voterRepository.save(voter);

        model.addAttribute("message", "Your vote has been submitted successfully!");
        return "message";
    }
}
