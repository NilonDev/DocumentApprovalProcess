package com.example.docprocess.model;

import java.util.HashMap;
import java.util.Map;

public class LevelAgreementResult {

    public LevelAgreementResult() {
        step = 0;
        operation = "missing";
        usersVote = new HashMap<>();
        voteResult = "missing";
    }

    private int step;
    private String operation;

    private Map<String, String> usersVote;
    private String voteResult;

    public String getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(String voteResult) {
        this.voteResult = voteResult;
    }

    public int getStep() {
        return step;
    }

    public String getOperation() {
        return operation;
    }

    public Map<String, String> getUsersVote() {
        return usersVote;
    }

    public void setUsersVote(Map<String, String> usersVote) {
        this.usersVote = usersVote;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        String str = "";
        for (Map.Entry<String, String> entry : usersVote.entrySet()) {
            str += entry.getKey() + " : " + entry.getValue() + "\n";
        }
        return str;
    }
}
