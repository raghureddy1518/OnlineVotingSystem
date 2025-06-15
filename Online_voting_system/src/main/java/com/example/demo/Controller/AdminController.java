package com.example.demo.Controller;

import com.example.demo.Entity.Admin;
import com.example.demo.Entity.Candidate;
import com.example.demo.Repository.AdminRepository;
import com.example.demo.Repository.CandidateRepository;
import com.example.demo.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VoterRepository voterRepository;

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "adminlogin";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            model.addAttribute("candidates", candidateRepository.findAll());
            model.addAttribute("totalVoters", voterRepository.count());
            model.addAttribute("votesCast", voterRepository.countByHasVoted(true));
            return "admindashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "adminlogin";
        }
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        model.addAttribute("totalVoters", voterRepository.count());
        model.addAttribute("votesCast", voterRepository.countByHasVoted(true));
        return "admindashboard";
    }

    @PostMapping("/admin/addCandidate")
    public String addCandidate(@RequestParam("candidateName") String candidateName,
                               @RequestParam("partyName") String partyName,
                               @RequestParam("partySymbol") MultipartFile partySymbol,
                               @RequestParam("candidatePhoto") MultipartFile candidatePhoto,
                               Model model) {
        try {
            String partySymbolPath = saveImage(partySymbol);
            String candidatePhotoPath = saveImage(candidatePhoto);

            Candidate candidate = new Candidate(candidateName, partyName, partySymbolPath, candidatePhotoPath);
            candidate.setVoteCount(0);
            candidateRepository.save(candidate);

            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload files");
            model.addAttribute("candidates", candidateRepository.findAll());
            model.addAttribute("totalVoters", voterRepository.count());
            model.addAttribute("votesCast", voterRepository.countByHasVoted(true));
            return "admindashboard";
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }

    @PostMapping("/admin/deleteCandidate")
    public String deleteCandidate(@RequestParam String id, RedirectAttributes redirectAttributes) {
        candidateRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Candidate deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/editCandidate")
    public String showEditForm(@RequestParam("id") String id, Model model) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate == null) {
            model.addAttribute("error", "Candidate not found.");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("candidate", candidate);
        return "editcandidate";
    }

    @PostMapping("/admin/updateCandidate")
    public String updateCandidate(@RequestParam("id") String id,
                                  @RequestParam("candidateName") String candidateName,
                                  @RequestParam("partyName") String partyName,
                                  @RequestParam(value = "partySymbol", required = false) MultipartFile partySymbol,
                                  @RequestParam(value = "candidatePhoto", required = false) MultipartFile candidatePhoto,
                                  RedirectAttributes redirectAttributes) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate == null) {
            redirectAttributes.addFlashAttribute("error", "Candidate not found.");
            return "redirect:/admin/dashboard";
        }

        candidate.setCandidateName(candidateName);
        candidate.setPartyName(partyName);

        try {
            if (partySymbol != null && !partySymbol.isEmpty()) {
                String partySymbolPath = saveImage(partySymbol);
                candidate.setPartySymbolPath(partySymbolPath);
            }
            if (candidatePhoto != null && !candidatePhoto.isEmpty()) {
                String candidatePhotoPath = saveImage(candidatePhoto);
                candidate.setCandidatePhotoPath(candidatePhotoPath);
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed.");
            return "redirect:/admin/dashboard";
        }

        candidateRepository.save(candidate);
        redirectAttributes.addFlashAttribute("toastMessage", "Candidate updated successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/voters")
    public String viewVoters(Model model) {
        model.addAttribute("voters", voterRepository.findAll());
        return "voterlist";
    }
}
