package com.example.docprocess.model;

import java.util.Map;

/**
 * LevelAgreement - уровень согласования документа
 *
 * step - шаг/уровень согласования
 * operation - операция для 2+ пользователей (and, or)
 * userVoting - пользователи и их голос (user1 : "yes"; user2 : "no")
 */
public class LevelAgreement {
    private int step;
    private String operation;
    private Map<String, String> userVoting;

    public int getStep() {
        return step;
    }
    public String getOperation() {
        return operation;
    }
    public Map<String, String> getUserVoting() {
        return userVoting;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUserVoting(Map<String, String> userVoting) {
        this.userVoting = userVoting;
    }
}
