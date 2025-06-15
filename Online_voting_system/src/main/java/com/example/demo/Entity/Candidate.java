package com.example.demo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidate")
public class Candidate {

    @Id
    private String id;

    private String candidateName;
    private String partyName;
    private String partySymbolPath;
    private String candidatePhotoPath;
    private int voteCount = 0; // ✅ Correct field name

    public Candidate() {}

    public Candidate(String candidateName, String partyName, String partySymbolPath, String candidatePhotoPath) {
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.partySymbolPath = partySymbolPath;
        this.candidatePhotoPath = candidatePhotoPath;
        this.voteCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartySymbolPath() {
        return partySymbolPath;
    }

    public void setPartySymbolPath(String partySymbolPath) {
        this.partySymbolPath = partySymbolPath;
    }

    public String getCandidatePhotoPath() {
        return candidatePhotoPath;
    }

    public void setCandidatePhotoPath(String candidatePhotoPath) {
        this.candidatePhotoPath = candidatePhotoPath;
    }

    // ✅ Getter for voteCount
    public int getVoteCount() {
        return voteCount;
    }

    // ✅ Setter for voteCount
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
