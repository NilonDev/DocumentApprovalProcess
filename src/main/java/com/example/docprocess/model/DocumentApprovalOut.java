package com.example.docprocess.model;

import java.util.List;

public class DocumentApprovalOut {
    private int idDocument;
    private List<LevelAgreementOut> levelsAgreement;

    public int getIdDocument() {
        return idDocument;
    }

    public List<LevelAgreementOut> getLevelsAgreement() {
        return levelsAgreement;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    public void setLevelsAgreement(List<LevelAgreementOut> levelsAgreement) {
        this.levelsAgreement = levelsAgreement;
    }
}
