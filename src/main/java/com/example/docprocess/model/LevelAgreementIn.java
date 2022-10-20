package com.example.docprocess.model;

/**
 * LevelAgreement - уровень согласования документа
 * <p>
 * step - шаг/уровень согласования
 * operation - операция для 2+ пользователей (and, or)
 * userVoting - пользователи и их голос (user1 : "true"; user2 : "false")
 */
public class LevelAgreementIn {
    private int step;
    private String operation;
    private String[] users;

    public int getStep() {
        return step;
    }

    public String getOperation() {
        return operation;
    }

    public String[] getUsers() {
        return users;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }
}
