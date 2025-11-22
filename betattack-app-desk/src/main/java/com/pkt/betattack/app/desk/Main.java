package com.pkt.betattack.app.desk;

import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.socket.SocketService;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public void init() {

    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        URL rootURL = null;
        try {
            rootURL = this.getClass().getResource("fxml/main/app.fxml");
            root = FXMLLoader.load(rootURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Анализ ставок");
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        AuthConfig.MM_SOCKET = new SocketService().getSocket();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

