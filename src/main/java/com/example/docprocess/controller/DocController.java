package com.example.docprocess.controller;

import com.example.docprocess.MainApplication;
import com.example.docprocess.constrant.Constant;
import com.example.docprocess.logic.AppEngine;
import com.example.docprocess.model.LevelAgreementIn;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DocController implements VoteDialogController.Callback {
    private AppEngine appEngine = new AppEngine();
    @FXML
    public MenuItem aboutProgram;
    @FXML
    public Button processingBtn;
    @FXML
    public Button saveBtn;
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
        removeTempData();
        FileChooser fileChooser = new FileChooser();        // для работы с проводником
        fileChooser.setTitle("Выбрать Json файл");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("json", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            appEngine.jsonRead(selectedFile.getAbsolutePath());
            nextVote();
            openSuccess();
        } else {
            openError();
        }
    }

    @FXML
    protected void processingJsonFile() throws IOException {
        appEngine.processingDocument(userVoteResult);
        loadSuccess();
        // вывод финального окна
        createResultForm(appEngine.getFinalResult());
    }

    @FXML
    protected void saveJsonFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить результаты в Json");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("json", "*.json")
        );

        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            appEngine.jsonSave(selectedFile.getAbsolutePath());
            loadSuccess();
        } else {
            // loadError();
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
        processingBtn.setDisable(false);
        setTextJson(textAreaIn, appEngine.getStartJson());

        loadLabel.setText("Json-файл не обработан");
        processingBtn.setDisable(false);
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    @FXML
    protected void loadSuccess() throws IOException {
        loadLabel.setText("Файл обработан!");
        processingBtn.setDisable(false);
        saveBtn.setDisable(false);
        setSuccessImg(loadImage);
        setTextJson(textAreaOut, appEngine.getEndJson());
    }

    @FXML
    protected void openError() throws IOException {
        openLabel.setText("Ожидается открытие Json-файла");
        setErrorImg(openImage);
        processingBtn.setDisable(true);
        saveBtn.setDisable(true);
        textAreaIn.clear();

        loadLabel.setText("Json-файл не обработан");
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    @FXML
    protected void loadError() throws IOException {
        loadLabel.setText("Json-файл не обработан");
        processingBtn.setDisable(false);
        setErrorImg(loadImage);
        textAreaOut.clear();
    }

    /**
     * Поля и методы для работы с окном голосования (user-vote-view)
     * countForm = количество отображаемых окон голосования
     * currentStep = текущий шаг голосования
     * userVoteResult = коллекция, которая хранит результаты голосования по типу:
     * (имя пользователя : true / false)
     */

    private int countForm = 0;
    private int currentStep = 0;
    private Map<String, String> userVoteResult = new HashMap<>();

    public Map<String, String> getUserVoteResult() {
        return userVoteResult;
    }

    private void removeTempData() {
        countForm = 0;
        currentStep = 0;
        userVoteResult.clear();
    }

    protected void countFormDecrement() {
        countForm--;
    }

    /**
     * createVoteDialog() - Создание окна согласования
     *
     * @param step     = текуший шаг, на котором проходит согласование
     * @param userName = имя голосующего пользователя
     */
    @FXML
    protected void createVoteDialog(String step, String userName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("user-vote-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Согласование");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });

        VoteDialogController dialogController = (VoteDialogController) fxmlLoader.getController();
        dialogController.registerCallBack(this);

        if (step.equals("1")) dialogController.dialogTextVote.setText("Начать проведение документа?");

        dialogController.stepNumber.setText(step);
        dialogController.userName.setText(userName);

        countForm++;
    }

    /**
     * nextVote() - создание всех окон голосования на шаге согласование
     */

    @FXML
    protected void nextVote() throws IOException {
        int maxStep = appEngine.getDocumentInput().getLevelsAgreement().length;

        if (countForm == 0 && currentStep < maxStep) {
            LevelAgreementIn level = appEngine.getDocumentInput().getLevelsAgreement()[currentStep];
            for (int k = 0; k < level.getUsers().length; k++) {
                createVoteDialog(String.valueOf(level.getStep()), level.getUsers()[k]);
            }
            currentStep++;
        }
    }

    /**
     * callBackUserVote - коллбэк, который вызывается, когда пользователь голосует
     * "@param userName = Имя пользователя
     * "@param userVote = Голос пользователя
     */
    @Override
    public void callBackUserVote(String step, String userName, String userVote) throws IOException {
        if (step.equals("1") && userVote.equals(Constant.FALSE_VOTE)) {     // если отменить обработку
            removeTempData();
            openError();
            return;
        }
        countFormDecrement();                       // счетчик форм -1
        userVoteResult.put(userName, userVote);     // добавление результата в коллекцию
        nextVote();                                  // переход к следующему шагу согласования
    }

    @FXML
    protected void createAboutForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("about-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("О программе");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void createResultForm(String resultVote) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("result-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Результаты");
        stage.setScene(scene);

        ResultDialogController resultController = (ResultDialogController) fxmlLoader.getController();
        if (resultVote.toLowerCase().equals("true")) {
            setSuccessImg(resultController.imageResult);
            resultController.resultLabel.setText("TRUE");
        } else if (resultVote.toLowerCase().equals("false")) {
            setErrorImg(resultController.imageResult);
            resultController.resultLabel.setText("FALSE");
        }
        stage.show();
    }
}
