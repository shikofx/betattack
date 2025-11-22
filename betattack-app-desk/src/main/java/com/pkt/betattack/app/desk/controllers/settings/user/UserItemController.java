package com.pkt.betattack.app.desk.controllers.settings.user;

import com.pkt.betattack.app.api.pojo.user.User;
import com.pkt.betattack.app.desk.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserItemController extends Node {

    private User user;

    private UserEditorController editorController;

    @FXML
    private AnchorPane userItemPane;

    @FXML
    private VBox userTableItemBox;

    @FXML
    private GridPane userItemGrid;

    @FXML
    private Text userItemUpdateDate;

    @FXML
    private Text userItemName;

    @FXML
    private Text userItemEmail;

    @FXML
    private Text userItemCreateDate;

    @FXML
    void clickOnGrid(MouseEvent event) {
        Shake shake = new Shake(userItemGrid)
                .cycles(2);
        shake.play();
        editorController.fillUserData(user);
        editorController.setUserAfter(user);
    }

    @FXML
    void clickOn(MouseEvent event) {

    }

    @FXML
    void initialize() {
        if(user != null) {
            userItemName.setText(user.getName());
            userItemEmail.setText(user.getEmail());
            userItemCreateDate.setText(user.getCreatedAt());
            userItemUpdateDate.setText(user.getUpdatedAt());
        }
    }

    public UserItemController user(User user) {
        this.user = user;
        return this;
    }

    public UserItemController editorController(UserEditorController controller){
        this.editorController = controller;
        return this;
    }
}
