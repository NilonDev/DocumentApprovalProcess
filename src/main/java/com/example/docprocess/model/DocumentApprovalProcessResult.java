package com.example.docprocess.model;

import java.util.ArrayList;
import java.util.List;

public class DocumentApprovalProcessResult {

    public DocumentApprovalProcessResult() {
        idDocument = -1;
        List<LevelAgreementResult> levelsAgreement = new ArrayList<>();
    }
    private int idDocument;
    private List<LevelAgreementResult> levelsAgreement;

    public int getIdDocument() {
        return idDocument;
    }
    public List<LevelAgreementResult> getLevelsAgreement() {
        return levelsAgreement;
    }
    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
    public void setLevelsAgreement(List<LevelAgreementResult> levelsAgreement) {
        this.levelsAgreement = levelsAgreement;
    }
}
