package com.pkt.betattack.app.desk.controllers.login;

import com.pkt.betattack.app.api.client.controllers.UserApiController;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.api.pojo.user.LoginResponse;
import com.pkt.betattack.app.api.pojo.user.User;
import com.pkt.betattack.app.desk.animation.Shake;
import com.pkt.betattack.app.desk.controllers.AbstractController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class LoginController extends AbstractController {

    @FXML
    private TextField emailInput;

    @FXML
    private Button submitButton;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label emailWarningLabel;

    @FXML
    private Label passwordWarningLabel;

    @FXML
    private Label loginWarningLabel;

    @FXML
    private CheckBox savePasswordCheckBox;

    @FXML
    private FontAwesomeIconView logoIcon;

    @FXML
    private Label logoText;

    @FXML
    private Label appText;

    @FXML
    void emailTypeText(KeyEvent event) {
        if (emailWarningLabel.isVisible()) {
            emailWarningLabel.setVisible(false);
        }
    }

    @FXML
    void submitButtonClick(ActionEvent event) {
        Shake shakeLogin = new Shake(emailInput);
        Shake shakePassword = new Shake(passwordInput);
        String emailValue = emailInput.getText().trim();
        String passwordValue = passwordInput.getText();
        if (emailValue.isEmpty()) {
            emailWarningLabel.setVisible(true);
        }

        if (passwordValue.isEmpty()) {
            passwordWarningLabel.setVisible(true);
        }
        try {
            if (!passwordValue.isEmpty() && !emailValue.isEmpty()) {
                if (loginUser(emailValue, passwordValue)) {
                    openPageAndHideCurrent(submitButton, APP_PAGE_PATH);
                } else {
                    shakeLogin.play();
                    shakePassword.play();
                    loginWarningLabel.setVisible(true);
                    loginWarningLabel.setText("* - there is no such set of user name and password");
                }
            } else {
                shakeLogin.play();
                shakePassword.play();
            }

        } catch (IOException e) {
            loginWarningLabel.setVisible(true);
            loginWarningLabel.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    void passwordTypeText(KeyEvent event) {
        if (passwordWarningLabel.isVisible()) {
            passwordWarningLabel.setVisible(false);
        }
    }

    @FXML
    void initialize() {

    }

    private boolean loginUser(String emailValue, String passwordValue) throws IOException {
        UserApiController service = new UserApiController();
        User user = new User();
        user.setEmail(emailValue);
        user.setPassword(passwordValue);
        LoginResponse login = service.login(user);
        if (null != login.getToken() && login.getToken().length() > 0) {
            AuthConfig.currentTokenString = new StringBuilder().append("Bearer ").append(login.getToken()).toString();
            AuthConfig.currentToken = login.getToken().toString();
            AuthConfig.currentUser = login.getUser();
            if(savePasswordCheckBox.isSelected())
                AuthConfig.saveToken();
            return true;
        }

        return false;
    }

    public Node getSubmitButton() {
        return submitButton;
    }
}

