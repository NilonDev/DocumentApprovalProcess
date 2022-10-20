package com.example.docprocess.logic;

import com.example.docprocess.constrant.Constant;
import com.example.docprocess.model.DocumentApprovalProcess;
import com.example.docprocess.model.DocumentApprovalProcessResult;
import com.example.docprocess.model.LevelAgreement;
import com.example.docprocess.model.LevelAgreementResult;
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
    private DocumentApprovalProcess documentInput;
    private DocumentApprovalProcessResult documentOutput;

    public AppEngine() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public DocumentApprovalProcess getDocumentInput() {
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
        documentInput = gson.fromJson(reader, DocumentApprovalProcess.class);
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

        LevelAgreement[] listLevel = documentInput.getLevelsAgreement();
        int stepLength = listLevel.length;

        List<LevelAgreementResult> listOfLevelsResult = new ArrayList<>();

        for (int i = 0; i < stepLength; i++) {
            LevelAgreement currentStep = listLevel[i];
            LevelAgreementResult tempResult = new LevelAgreementResult();
            tempResult.setStep(currentStep.getStep());
            tempResult.setOperation(currentStep.getOperation());
            tempResult.setUsersVote(createUserWithVote(currentStep, voteResult));
            listOfLevelsResult.add(tempResult);
        }

        documentOutput = new DocumentApprovalProcessResult();
        documentOutput.setIdDocument(documentInput.getIdDocument());
        documentOutput.setLevelsAgreement(processingStepByStep(listOfLevelsResult));
        //System.out.println(gson.toJson(documentOutput));
    }


    public Map<String, String> createUserWithVote(LevelAgreement level, Map<String, String> voteResult) {
        Map<String, String> userWithVote = new HashMap<String, String>();

        for (int i = 0; i < level.getUsers().length; i++) {
            String userName = level.getUsers()[i];
            userWithVote.put(userName, voteResult.get(userName));
        }
        return userWithVote;
    }

    public List<LevelAgreementResult> processingStepByStep(List<LevelAgreementResult> voteList) {
        boolean currentResult;
        boolean finalResult = true;

        for (int i = 0; i < voteList.size(); i++) {
            LevelAgreementResult currentLVL = voteList.get(i);
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

        LevelAgreementResult finalLEVEL = new LevelAgreementResult();
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