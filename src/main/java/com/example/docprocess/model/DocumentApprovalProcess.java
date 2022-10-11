package com.example.docprocess.model;

/**
 * DocumentApprovalProcess - процесс согласования документа
 * idDocument - идентификатор документа
 * LevelAgreement[] - уровни проведения документа
 */

public class DocumentApprovalProcess {
    private int idDocument;
    private LevelAgreement[] levelsAgreement;

    public int getIdDocument() {
        return idDocument;
    }
    public LevelAgreement[] getLevelsAgreement() {
        return levelsAgreement;
    }
    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
    public void setLevelsAgreement(LevelAgreement[] levelsAgreement) {
        this.levelsAgreement = levelsAgreement;
    }
}
