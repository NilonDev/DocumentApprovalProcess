package com.example.docprocess.controller;

import com.example.docprocess.constrant.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultDialogController {
    @FXML
    protected Label dialogText;
    @FXML
    protected Button okBtn;
    @FXML
    protected ImageView imageResult;
    @FXML
    protected Label resultLabel;

    @FXML
    public void okCloseButtonAction() {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
}
