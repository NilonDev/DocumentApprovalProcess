package com.example.docprocess.logic;

import com.example.docprocess.constrant.Constant;
import com.example.docprocess.model.DocumentApprovalProcess;
import com.example.docprocess.model.LevelAgreement;
import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class AppEngine {
    private Gson gson;
    private DocumentApprovalProcess document;
    private JsonElement documentTree;

    public String getStartJson () {
        return gson.toJson(document);
    }

    public String getEndJson () {
        return gson.toJson(documentTree);
    }

    public JsonElement getDocumentTree() {
        return documentTree;
    }

    public AppEngine() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void jsonRead(String pathIn) throws IOException {
        FileReader reader = new FileReader(pathIn);
        document = gson.fromJson(reader, DocumentApprovalProcess.class);
        reader.close();
        //System.out.println(document.idDocument);
    }

    public void jsonSave(String pathOut) throws IOException {
        FileWriter writer = new FileWriter(pathOut);
        gson.toJson(documentTree, writer);
        writer.flush();
        writer.close();
    }

    public void holdDocument() throws IOException {
        int stepCount;
        String levelResult;

        JsonElement docTree = gson.toJsonTree(document);        // весь json-объект

        JsonArray levelsJSON = docTree.getAsJsonObject()            // массив уровеней провдения документа
                .getAsJsonArray("levelsAgreement");

        stepCount = levelsJSON.size();

        for (int i = 0; i < stepCount; i++) {

            JsonElement levelJSON = levelsJSON.get(i).getAsJsonObject();
            LevelAgreement level = gson.fromJson(levelJSON, LevelAgreement.class);

            levelResult = stepResult(level.getOperation(), level.getUserVoting());

            docTree.getAsJsonObject()
                    .getAsJsonArray("levelsAgreement")
                    .get(i)
                    .getAsJsonObject()
                    .addProperty("levelResult", levelResult);
        }

        String finalResult = finalResult(docTree);
        docTree.getAsJsonObject().addProperty("finalResult", finalResult);

        // добавление последнего шага
        JsonElement mainUser = docTree.getAsJsonObject()
                .getAsJsonArray("levelsAgreement")
                .get(0)
                .getAsJsonObject()
                .deepCopy();

        mainUser.getAsJsonObject()
                .addProperty("step", stepCount + 1);
        mainUser.getAsJsonObject()
                .addProperty("levelResult", finalResult);

        docTree.getAsJsonObject()
                .getAsJsonArray("levelsAgreement")
                .add(mainUser);

        ////////////////////////////////////////

        documentTree = docTree;
        // вывод JSON в консоль
        //System.out.println(gson.toJson(documentTree));
    }

    public String stepResult(final String operation, Map<String, String> usersVoting) {
        boolean currentResult;

        if (operation.equals("or") || operation.equals("-"))
            currentResult = false;
        else if (operation.equals("and")) {
            currentResult = true;
        } else {
            currentResult = false;
        }

        for (Map.Entry<String, String> user : usersVoting.entrySet()) {
            switch (operation) {
                case "or":
                case "-":
                    if (user.getValue().equals(Constant.CONFIRM)) {
                        currentResult = currentResult | true;
                    } else if (user.getValue().equals(Constant.REJECT)) {
                        currentResult = currentResult | false;
                    }
                    break;
                case "and": {
                    if (user.getValue().equals(Constant.CONFIRM)) {
                        currentResult = currentResult & true;
                    } else if (user.getValue().equals(Constant.REJECT)) {
                        currentResult = currentResult & false;
                    }
                    break;
                }
            }
        }
        if (currentResult)
            return Constant.CONFIRM;
        else
            return Constant.REJECT;
    }

    public String finalResult(JsonElement documentTree) {
        JsonArray levels = documentTree.getAsJsonObject().getAsJsonArray("levelsAgreement");
        int stepCount = levels.size();

        for (int i = 0; i < stepCount; i++) {
            String levelResult = levels.get(i).
                    getAsJsonObject().
                    get("levelResult")
                    .toString();

            levelResult = levelResult.replace("\"", "");

            if (levelResult.equals(Constant.REJECT))
                return Constant.REJECT;
        }
        return Constant.CONFIRM;
    }
}