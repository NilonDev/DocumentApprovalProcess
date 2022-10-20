package com.example.docprocess.controller;

import com.example.docprocess.constrant.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VoteDialogController {

    interface Callback{
        void callBackUserVote(String step, String userName, String userVote) throws IOException;
    }
    Callback callback;

    public void registerCallBack(Callback callback){
        this.callback = callback;
    }

    @FXML
    protected Label dialogTextVote;
    @FXML
    protected Label stepNumber;
    @FXML
    protected Label userName;
    @FXML
    protected Button trueVoteBtn;
    @FXML
    protected Button falseVoteBtn;

    @FXML
    protected void trueBtn() throws IOException {
        Stage stage = (Stage) trueVoteBtn.getScene().getWindow();
        stage.close();
        callback.callBackUserVote(stepNumber.getText(), userName.getText(), Constant.TRUE_VOTE);
    }
    @FXML
    protected void falseBtn() throws IOException {
        Stage stage = (Stage) falseVoteBtn.getScene().getWindow();
        stage.close();
        callback.callBackUserVote(stepNumber.getText(), userName.getText(), Constant.FALSE_VOTE);
    }
}
