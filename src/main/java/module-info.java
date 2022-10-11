module com.example.docprocess {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.docprocess to javafx.fxml;

    exports com.example.docprocess;
    exports com.example.docprocess.controller;
    opens com.example.docprocess.controller to javafx.fxml;

    opens  com.example.docprocess.model to com.google.gson;
    exports com.example.docprocess.model;

}