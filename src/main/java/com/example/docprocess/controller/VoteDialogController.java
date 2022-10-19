package com.example.docprocess.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VoteDialogController {
    /*
    private DocController docController = MainApplication
            .getFxmlLoader()
            .getController();

     */
    interface Callback{
        void callBackUserVote(String userName, String userVote);
    }
    Callback callback;

    public void registerCallBack(Callback callback){
        this.callback = callback;
    }
    @FXML
    protected Label stepNumber;
    @FXML
    protected Label userName;
    @FXML
    protected Button trueVoteBtn;
    @FXML
    protected Button falseVoteBtn;
    private final String TRUE_VOTE = "true";
    private final String FALSE_VOTE = "false";

    @FXML
    protected void trueBtn() throws IOException {
        Stage stage = (Stage) trueVoteBtn.getScene().getWindow();
        stage.close();
        callback.callBackUserVote(userName.getText(), TRUE_VOTE);
    }

    @FXML
    protected void falseBtn() throws IOException {
        Stage stage = (Stage) falseVoteBtn.getScene().getWindow();
        stage.close();
        callback.callBackUserVote(userName.getText(), FALSE_VOTE);
    }
}
