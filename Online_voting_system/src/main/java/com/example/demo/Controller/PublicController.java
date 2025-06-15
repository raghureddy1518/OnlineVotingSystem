package com.example.demo.Controller;

import com.example.demo.Repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PublicController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Value("${voting.deadline}")
    private String votingDeadline;

    @GetMapping("/results")
    public String showPublicResults(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime deadline = LocalDateTime.parse(votingDeadline, formatter);
        LocalDateTime availableTime = deadline.plusMinutes(30);

        if (LocalDateTime.now().isBefore(availableTime)) {
            model.addAttribute("message", "Results will be available 30 minutes after voting ends.");
            return "publicresults";
        }

        model.addAttribute("candidates", candidateRepository.findAll());
        return "publicresults";
    }
}
