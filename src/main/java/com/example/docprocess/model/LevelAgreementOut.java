package com.example.docprocess.model;

import com.example.docprocess.constrant.Constant;

import java.util.HashMap;
import java.util.Map;

public class LevelAgreementOut {
    private int step;
    private String operation;
    private Map<String, String> usersVote;
    private String voteResult;

    public LevelAgreementOut() {
        step = 0;
        operation = Constant.MISSING_OPERATION;
        usersVote = new HashMap<>();
        voteResult = Constant.MISSING_OPERATION;
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

    public String getVoteResult() {
        return voteResult;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUsersVote(Map<String, String> usersVote) {
        this.usersVote = usersVote;
    }

    public void setVoteResult(String voteResult) {
        this.voteResult = voteResult;
    }
}
