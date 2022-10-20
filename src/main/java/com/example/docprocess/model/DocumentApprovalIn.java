package com.example.docprocess.model;

import java.util.ArrayList;

/**
 * DocumentApprovalProcess - процесс согласования документа
 * idDocument - идентификатор документа
 * LevelAgreement[] - уровни проведения документа
 */

public class DocumentApprovalIn {
    private int idDocument;
    private LevelAgreementIn[] levelsAgreement;

    public int getIdDocument() {
        return idDocument;
    }

    public LevelAgreementIn[] getLevelsAgreement() {
        return levelsAgreement;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
    public void setLevelsAgreement(LevelAgreementIn[] levelsAgreement) {
        this.levelsAgreement = levelsAgreement;
    }
}
