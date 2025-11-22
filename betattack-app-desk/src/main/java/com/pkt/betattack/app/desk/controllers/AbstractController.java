package com.pkt.betattack.app.desk.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {


    public static final String LOG_IN_PAGE_PATH = "../fxml/logIn.fxml";
    public static final String APP_PAGE_PATH = "../fxml/app.fxml";

    public void openPageAndHideCurrent(Node node, String page) {
        node.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(page));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static Object getController(Node node) {
        Object controller = null;
        Node currentNode = node;
        do {
            controller = currentNode.getUserData();
            currentNode = currentNode.getParent();
        } while (controller == null && currentNode != null);
        return controller;
    }

    public static Object getGameController(Node node) {
        Object controller = null;
        Node currentNode = node;
        do {
            controller = currentNode.getUserData();
            currentNode = currentNode.getParent();
        } while (controller == null && currentNode != null);
        return controller;
    }
}
