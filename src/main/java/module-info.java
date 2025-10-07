module hr.javafx.project.csmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires java.sql;


    opens hr.javafx.project.csmt to javafx.fxml;
    exports hr.javafx.project.csmt;
    exports hr.javafx.project.csmt.controller;
    exports hr.javafx.project.csmt.enums;
    exports hr.javafx.project.csmt.utils;
    exports hr.javafx.project.csmt.model;
    opens hr.javafx.project.csmt.controller to javafx.fxml;
    exports hr.javafx.project.csmt.controller.login;
    opens hr.javafx.project.csmt.controller.login to javafx.fxml;
    exports hr.javafx.project.csmt.controller.manager;
    opens hr.javafx.project.csmt.controller.manager to javafx.fxml;
    exports hr.javafx.project.csmt.controller.member;
    opens hr.javafx.project.csmt.controller.member to javafx.fxml;
    exports hr.javafx.project.csmt.exception to javafx.fxml;
}