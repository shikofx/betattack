package com.pkt.betattack.app.desk.controllers.settings.user;

import com.pkt.betattack.app.api.client.controllers.UserApiController;
import com.pkt.betattack.app.api.pojo.user.User;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.desk.controllers.AbstractController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class UserEditorController extends AbstractController {


    public static final String FX_BORDER_NO_COLOR = "-fx-border-color: ";
    public static final String FX_BORDER_COLOR = "-fx-border-color: ";
    public static final String FX_COLOR_RED = "#FB282A";
    public static final String INCORRECT_PASS_WARNING_MESSAGE = "* - пароль тольжен быть не меньше 8 символов";
    public static final String EMPTY_STRING = "";
    @FXML
    private GridPane editorGridPane;

    @FXML
    private VBox editorBox;

    @FXML
    private Text loginWarning;

    @FXML
    private TextField nameField;

    @FXML
    private Text emailWarning;

    @FXML
    private TextField emailField;

    @FXML
    private Text passwordWarning;

    @FXML
    private TextField passwordField;

    @FXML
    private Text passwordConfirmWarning;

    @FXML
    private TextField passwordConfirmField;

    @FXML
    private Button resetButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox userListBox;

    private String defaultConfirmPasswordText;
    private User userBefore = new User();
    private User userAfter = new User();
    private User activeUser = new User();

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    @FXML
    void addUser(ActionEvent event) {
        new UserApiController().addUser(this, userAfter);
    }

    @FXML
    void deleteUser(ActionEvent event) {
        new UserApiController().deleteUser(this, activeUser);
    }

    @FXML
    void saveUser(ActionEvent event) {
        userAfter.setId(activeUser.getId());
        new UserApiController().updateUser(this, userAfter);
    }

    @FXML
    void userNameTypeText(KeyEvent event) {
        userAfter.setName(nameField.getText().trim());
        checkResetConditions();
        checkSaveConditions();
    }

    @FXML
    void emailTypeText(KeyEvent event) {
        userAfter.setEmail(emailField.getText().trim());
        checkResetConditions();
        checkSaveConditions();
                checkAddConditions();
        checkDeleteConditions();
    }

    @FXML
    void passwordTypeText(KeyEvent event) {
        String password = passwordField.getText();
        userAfter.setPassword(password);
        checkResetConditions();
        checkSaveConditions();
        checkPassword();
        checkPasswordConfirmation();
        checkAddConditions();
    }

    @FXML
    void passwordConfirmationTypeText(KeyEvent event) {
        checkPasswordConfirmation();
        checkAddConditions();
    }

    @FXML
    void resetUserToCurrent(ActionEvent event) {
        fillDefaultData();
    }

    @FXML
    public void initialize() {
        fillDefaultData();
    }

    private void checkDeleteConditions() {
        if (userBefore.getEmail().equals(userAfter.getEmail())
            && !userBefore.getEmail().equals(AuthConfig.currentUser.getEmail())) {
            deleteButton.setDisable(false);
            return;
        }
        deleteButton.setDisable(true);
    }

    private void checkAddConditions() {
        String password = passwordField.getText();
        String passwordConfirmation = "";
        if (!passwordConfirmField.isDisabled()) {
            passwordConfirmation = passwordConfirmField.getText();
        }
        String email = emailField.getText();
        Long usersAmount = AuthConfig.userList.stream().filter(user -> user.getEmail().equals(email)).count();
        if (password.length() >= 8 && password.equals(passwordConfirmation) && usersAmount == 0) {
            addButton.setDisable(false);
            return;
        }
        addButton.setDisable(true);
    }

    private void checkSaveConditions() {
        if (userAfter.isEqualTo(userBefore) || !userAfter.getEmail().equals(userBefore.getEmail())) {
            saveButton.setDisable(true);
            return;
        }
        saveButton.setDisable(false);
    }

    private void checkResetConditions() {
        if (userAfter.isEqualTo(AuthConfig.currentUser)) {
            resetButton.setDisable(true);
            return;
        }
        resetButton.setDisable(false);
    }

    private User getUser() {
        User user = new User();
        user.setEmail(emailField.getText());
        user.setName(nameField.getText());
        user.setPassword(passwordField.getText());
        return user;
    }

    public void fillDefaultData() {
        fillUserData(AuthConfig.currentUser);
        activeUser = AuthConfig.currentUser;
        userBefore = getUser();
        userAfter = getUser();

    }

    private void setDefaultState(){
        addButton.setDisable(true);
        saveButton.setDisable(true);
        resetButton.setDisable(true);
        deleteButton.setDisable(true);
        nameField.setText(EMPTY_STRING);
        emailField.setText(EMPTY_STRING);
        passwordField.setText(EMPTY_STRING);
        passwordConfirmField.setDisable(true);
        passwordConfirmField.setText(defaultConfirmPasswordText);
    }

    public UserEditorController setVisible(boolean visibility) {
        editorGridPane.setVisible(visibility);
        return this;
    }

    public void fillUserData(User user) {
        setDefaultState();
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        activeUser = user;
        userBefore = getUser();
        userAfter = getUser();
        checkAddConditions();
        checkSaveConditions();
        checkDeleteConditions();
        checkResetConditions();
    }

    public void fillUserList(List<User> users) {
        UserEditorController editorController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    cleanUserList();
                    for (int i = 0; i < users.size(); i++) {
                        FXMLLoader
                            loader =
                            new FXMLLoader(getClass().getResource(
                                "/com/pkt/betattack/app/desk/fxml/settings/user/userItem.fxml"));
                        Node node = loader.load();
                        UserItemController userItemController = (UserItemController) getController(node);
                        userItemController
                            .user(users.get(i))
                            .editorController(editorController);
                        userItemController.initialize();
                        editorController.userListBox().getChildren().add(node);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cleanUserList() {
        ObservableList<Node> children = userListBox.getChildren();
        children.remove(0, children.size());
    }

    private VBox userListBox() {
        return userListBox;
    }

    private void checkPassword() {
        String password = passwordField.getText();
        if (!password.isEmpty()) {
            if (password.length() < 8) {
                passwordField.setStyle("-fx-border-color: #FB282A");
                passwordWarning.setVisible(true);
                passwordWarning.setText(INCORRECT_PASS_WARNING_MESSAGE);
                passwordConfirmField.setDisable(true);
            } else {
                passwordWarning.setVisible(false);
                passwordWarning.setText(EMPTY_STRING);
                passwordField.setStyle(FX_BORDER_NO_COLOR);
                passwordConfirmField.setDisable(false);
                checkPasswordConfirmation();
            }
        } else {
            passwordWarning.setText(EMPTY_STRING);
            passwordWarning.setVisible(true);
            passwordField.setStyle(FX_BORDER_NO_COLOR);
            passwordConfirmField.setText(defaultConfirmPasswordText);
            checkPasswordConfirmation();
        }
    }

    private void checkPasswordConfirmation() {
        String password = passwordField.getText();
        String passwordConfirmation = passwordConfirmField.getText();
        if (!password.isEmpty() && !password.equals(passwordConfirmation)) {
            passwordConfirmField.setStyle("-fx-border-color: #FB282A");
            passwordConfirmWarning.setText("* - пароли должны совпадать");
            passwordConfirmWarning.setVisible(true);
        } else {
            passwordConfirmField.setStyle(FX_BORDER_COLOR);
            passwordConfirmWarning.setText(EMPTY_STRING);
            passwordConfirmWarning.setVisible(false);
            checkAddConditions();
        }
    }

    public void setUserAfter(User user) {
        this.userAfter = user;
    }
}
