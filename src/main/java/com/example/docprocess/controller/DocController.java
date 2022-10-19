package com.example.docprocess.controller;

import com.example.docprocess.MainApplication;
import com.example.docprocess.logic.AppEngine;
import com.example.docprocess.model.LevelAgreement;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DocController implements VoteDialogController.Callback {
    private AppEngine appEngine = new AppEngine();
    @FXML
    private Button openBtn;
    @FXML
    private Button loadBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private TextField pathFileIn;
    @FXML
    private ImageView openImage;
    @FXML
    private ImageView loadImage;
    @FXML
    private Label openLabel;
    @FXML
    private Label loadLabel;
    @FXML
    private TextArea textAreaIn;
    @FXML
    private TextArea textAreaOut;

    @FXML
    protected void openJsonFile() throws IOException {
        FileChooser fileChooser = new FileChooser();        // для работы с проводником
        fileChooser.setTitle("Выбрать Json файл");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("json", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            appEngine.jsonRead(selectedFile.getAbsolutePath());
            openSuccess();

        } else {
            openError();
        }

        nextVote();
    }

    @FXML
    protected void loadJsonFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить результаты в Json");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("json", "*.json")
        );

        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            //appEngine.holdDocument();
            appEngine.jsonSave(selectedFile.getAbsolutePath());
            loadSuccess();
        } else {
            loadError();
        }
    }

    @FXML
    protected void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void setSuccessImg(ImageView imageView) throws IOException {
        InputStream is = getClass().getResourceAsStream("/com/example/docprocess/success.png");
        Image image = new Image(is);
        imageView.setImage(image);
        is.close();
    }

    @FXML
    protected void setErrorImg(ImageView imageView) throws IOException {
        InputStream is = getClass().getResourceAsStream("/com/example/docprocess/error.png");
        Image image = new Image(is);
        imageView.setImage(image);
        is.close();
    }

    @FXML
    protected void setTextJson(TextArea textArea, String data) {
        textArea.setText(data);
    }

    @FXML
    protected void openSuccess() throws IOException {
        openLabel.setText("Файл выбран!");
        setSuccessImg(openImage);
        loadBtn.setDisable(false);
        setTextJson(textAreaIn, appEngine.getStartJson());

        loadLabel.setText("Json-файл не обработан");
        loadBtn.setDisable(false);
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    @FXML
    protected void loadSuccess() throws IOException {
        loadLabel.setText("Файл обработан и сохранен!");
        loadBtn.setDisable(false);
        setSuccessImg(loadImage);
        setTextJson(textAreaOut, appEngine.getEndJson());
    }

    @FXML
    protected void openError() throws IOException {
        openLabel.setText("Ожидается открытие Json-файла");
        setErrorImg(openImage);
        loadBtn.setDisable(true);
        textAreaIn.clear();

        loadLabel.setText("Json-файл не обработан");
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    @FXML
    protected void loadError() throws IOException {
        loadLabel.setText("Json-файл не обработан");
        loadBtn.setDisable(false);
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    /**
     * Для работы с VoteDialog
     */

    private int countForm = 0;
    private int currentStep = 1;

    private Map<String, String> userVoteResult = new HashMap<>();

    protected void countFormDecrement() {
        countForm--;
    }

    /**
     *
     * Создание окна согласования
     */

    @FXML
    protected void createVoteDialog(String step, String userName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("user-vote-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("User Voting");
        stage.setScene(scene);
        stage.show();

        VoteDialogController dialogController = (VoteDialogController) fxmlLoader.getController();
        dialogController.registerCallBack(this);
        dialogController.stepNumber.setText(step);
        dialogController.userName.setText(userName);

        countForm++;
    }

    /**
     *
     * Создание всех окон на шаге согласование
     */

    @FXML
    protected void nextVote() throws IOException {
        LevelAgreement level;
        if (countForm == 0 || currentStep == 1) {

            try {
                level = appEngine.getDocument().getLevelsAgreement()[currentStep];
            } catch (IndexOutOfBoundsException e) {
                return;
            }

            for (int k = 0; k < level.getUsers().length; k++) {
                createVoteDialog(String.valueOf(level.getStep()), level.getUsers()[k]);
            }
            currentStep++;
        }
    }

    /**
     *
     * Получение результата голсования и продвижение на следующий шаг
     */

    @Override
    public void callBackUserVote(String userName, String userVote) {
        countFormDecrement();
        userVoteResult.put(userName, userVote);
        try {
            nextVote();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
