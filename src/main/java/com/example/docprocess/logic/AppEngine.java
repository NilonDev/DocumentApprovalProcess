package com.example.docprocess.logic;

import com.example.docprocess.constrant.Constant;
import com.example.docprocess.model.DocumentApprovalIn;
import com.example.docprocess.model.DocumentApprovalOut;
import com.example.docprocess.model.LevelAgreementIn;
import com.example.docprocess.model.LevelAgreementOut;
import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppEngine {
    private Gson gson;
    private DocumentApprovalIn documentInput;
    private DocumentApprovalOut documentOutput;

    public AppEngine() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public DocumentApprovalIn getDocumentInput() {
        return documentInput;
    }

    public String getStartJson() {
        return gson.toJson(documentInput);
    }

    public String getEndJson() {
        return gson.toJson(documentOutput);
    }


    public void jsonRead(String pathIn) throws IOException {
        FileReader reader = new FileReader(pathIn);
        documentInput = gson.fromJson(reader, DocumentApprovalIn.class);
        reader.close();
    }

    public void jsonSave(String pathOut) throws IOException {
        FileWriter writer = new FileWriter(pathOut);
        gson.toJson(documentOutput, writer);
        writer.flush();
        writer.close();
    }


    public void processingDocument(Map<String, String> voteResult) throws IOException {
        /*
        JsonElement docTree = gson.toJsonTree(documentInput);                         // весь json-объект

        JsonArray levelsJSON = docTree.getAsJsonObject()                              // массив уровеней проведения документа
                .getAsJsonArray("levelsAgreement");

         */

        LevelAgreementIn[] listLevel = documentInput.getLevelsAgreement();
        int stepLength = listLevel.length;

        List<LevelAgreementOut> listOfLevelsResult = new ArrayList<>();

        for (int i = 0; i < stepLength; i++) {
            LevelAgreementIn currentStep = listLevel[i];
            LevelAgreementOut tempResult = new LevelAgreementOut();
            tempResult.setStep(currentStep.getStep());
            tempResult.setOperation(currentStep.getOperation());
            tempResult.setUsersVote(createUserWithVote(currentStep, voteResult));
            listOfLevelsResult.add(tempResult);
        }

        documentOutput = new DocumentApprovalOut();
        documentOutput.setIdDocument(documentInput.getIdDocument());
        documentOutput.setLevelsAgreement(processingStepByStep(listOfLevelsResult));
        //System.out.println(gson.toJson(documentOutput));
    }


    public Map<String, String> createUserWithVote(LevelAgreementIn level, Map<String, String> voteResult) {
        Map<String, String> userWithVote = new HashMap<String, String>();

        for (int i = 0; i < level.getUsers().length; i++) {
            String userName = level.getUsers()[i];
            userWithVote.put(userName, voteResult.get(userName));
        }
        return userWithVote;
    }

    public List<LevelAgreementOut> processingStepByStep(List<LevelAgreementOut> voteList) {
        boolean currentResult;
        boolean finalResult = true;

        for (int i = 0; i < voteList.size(); i++) {
            LevelAgreementOut currentLVL = voteList.get(i);
            String operation = currentLVL.getOperation();

            if (operation.equals(Constant.OR_OPERATION) || operation.equals(Constant.MISSING_OPERATION))
                currentResult = false;
            else if (operation.equals(Constant.AND_OPERATION)) {
                currentResult = true;
            } else {
                currentResult = false;
            }

            for (Map.Entry<String, String> user : currentLVL.getUsersVote().entrySet()) {
                switch (operation) {
                    case Constant.OR_OPERATION:
                    case Constant.MISSING_OPERATION:
                    case Constant.START_OPERATION:
                        if (user.getValue().equals(Constant.TRUE_VOTE)) {
                            currentResult = currentResult | true;
                        } else if (user.getValue().equals(Constant.FALSE_VOTE)) {
                            currentResult = currentResult | false;
                        }
                        break;
                    case Constant.AND_OPERATION: {
                        if (user.getValue().equals(Constant.TRUE_VOTE)) {
                            currentResult = currentResult & true;
                        } else if (user.getValue().equals(Constant.FALSE_VOTE)) {
                            currentResult = currentResult & false;
                        }
                        break;
                    }
                }
            }

            if (currentResult)
                currentLVL.setVoteResult(Constant.TRUE_VOTE);
            else {
                currentLVL.setVoteResult(Constant.FALSE_VOTE);
                finalResult = false;
            }
        }

        LevelAgreementOut finalLEVEL = new LevelAgreementOut();
        finalLEVEL.setStep(voteList.size() + 1);
        finalLEVEL.setOperation(Constant.FINISH_OPERATION);
        finalLEVEL.setUsersVote(voteList.get(0).getUsersVote());

        if (finalResult) {
            finalLEVEL.setVoteResult(Constant.TRUE_VOTE);
        } else {
            finalLEVEL.setVoteResult(Constant.FALSE_VOTE);
        }

        voteList.add(finalLEVEL);
        return voteList;
    }
}