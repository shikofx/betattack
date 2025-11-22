package com.pkt.betattack.app.desk.controllers.main;

import static com.pkt.betattack.app.api.config.AuthConfig.MM_SOCKET;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.api.pojo.account.Account;
import com.pkt.betattack.app.api.pojo.account.AttackResponce;
import com.pkt.betattack.app.api.pojo.attack.AttackRequest;
import com.pkt.betattack.app.api.pojo.attack.GameAttack;
import com.pkt.betattack.app.api.pojo.game.Game;
import com.pkt.betattack.app.desk.config.Config;
import com.pkt.betattack.app.desk.controllers.AbstractController;
import com.pkt.betattack.app.desk.utils.GameTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BetItemController extends AbstractController {

    public static final String INIT_ATTACK = "init-attack";
    private final String ATTACK_RESULT = "attack-result-";
    private final String ATTACK_STOPPED = "attack-stopped-";
    private final String ATTACK_ACCOUNTS = "attacked-accounts-";
    private final String ACCOUNT_CHANGED = "account-changed-";

    private AttackRequest request;

    private Game game;
    @FXML
    public RadioButton addToAttackButton;

    @FXML
    public Hyperlink accountsInAttack;

    @FXML
    public TextField minKoefficient;
    public Label betSign;
    public Tooltip betSignTooltip;
    public Tooltip minKoefficientToolTip;
    public ToggleButton stopAttackButton;
    private int stoppedAttacksCount = 0;
    private int attackedOwnersCount = 0;
    private GameTimer timer;
    private GameAttack gameAttack;
    private GameItemController gameController;

    @FXML
    public void openAccountsInAttack(ActionEvent actionEvent) {
    }

    @FXML
    private AnchorPane betPane;

    @FXML
    private VBox betBox;

    @FXML
    private GridPane betGrid;

    @FXML
    private Text betName;

    @FXML
    private Text betExpectedResult;

    @FXML
    private Text betRateValue;

    @FXML
    private TextField attackSumField;

    @FXML
    private Tooltip betSumFieldTooltip;

    @FXML
    private ToggleButton startAttackButton;

    @FXML
    private Tooltip buttonTooltip;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Tooltip progressBarTooltip;

    public Text getBetName() {
        return betName;
    }

    public Text getBetExpectedResult() {
        return betExpectedResult;
    }

    public Text getBetRateValue() {
        return betRateValue;
    }

    public TextField getAttackSumField() {
        return attackSumField;
    }

    public ToggleButton getStartAttackButton() {
        return startAttackButton;
    }

    public Tooltip getButtonTooltip() {
        return buttonTooltip;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Tooltip getProgressBarTooltip() {
        return progressBarTooltip;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setProgressBarTooltip(Tooltip progressBarTooltip) {
        this.progressBarTooltip = progressBarTooltip;
    }

    public int failBets = 0;

    public int successBets = 0;

    public List<String> messages = new ArrayList<>();

    private Integer _id;

    private JSONArray victims = new JSONArray();

    private JSONArray accounts = new JSONArray();

    @FXML
    void typeBetSum(InputMethodEvent event) {

    }

    @FXML
    public void changeAttackStatus(ActionEvent actionEvent) {
        // Если чекбокс выбран, то нужно добавить контроллер в базу ставок
    }

    @FXML
    public void setMinKoefficient(KeyEvent keyEvent) {
        // Установленный коэффициент долен быть double
    }

    @FXML
    void clickToBetField(MouseEvent event) {
        if (attackSumField.getText().equals("0")) {
            attackSumField.setText("");
        }
    }

    String responseSign;

    @FXML
    void initialize(GameTimer timer, GameAttack gameAttack) {
        this.timer = timer;
        this.gameAttack = gameAttack;
        this.setBetSighn();
        responseSign =
            String.format("%s-%s-%s", game.getUrl(), gameAttack.getBet().getName(), gameAttack.getBet().getDirection());
        startAttackButton.setDisable(true);
        stopAttackButton.setDisable(true);
        addToAttackButton.setSelected(false);
        addToAttackButton.setDisable(true);

        MM_SOCKET.on(ATTACK_RESULT + responseSign, args -> {
            attackedOwnersCount++;
            if (attackedOwnersCount == accounts.length()) {
                startAttackButton.setDisable(false);
                startAttackButton.setSelected(false);
                stopAttackButton.setSelected(false);
                stopAttackButton.setDisable(true);
                progressBar.setProgress(0.0);
                progressBar.setStyle("");
                attackSumField.setDisable(false);
                attackedOwnersCount = 0;
            }
//                String ownerEmail = (String) args[0];
            AttackResponce result = null;
            try {
                result = new AttackResponce((JSONObject) args[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setProgress(result);

        }).on(ATTACK_ACCOUNTS + responseSign, args -> {
            accounts = (JSONArray) args[0];
            stopAttackButton.setDisable(false);
            addToAttackButton.setSelected(false);
            addToAttackButton.setDisable(true);
            accountsInAttack.setVisited(false);
            attackSumField.setDisable(true);

            progressBar.setStyle("-fx-accent: blue");
            messages.clear();
            progressBar.setProgress(1.0);
            progressBar.setStyle("-fx-accent: gray; -fx-background-color: green");

            if (!startAttackButton.isSelected()) {
                startAttackButton.setSelected(true);
                return;
            }

            setProgress("start", "");
        }).on(ACCOUNT_CHANGED + responseSign, args -> {
            System.out.println(args);
            //TODO: изменить счет без доступа к счетам в базе
            try {
                Account account = (Account) args[1];
                int accountIndex = AuthConfig.attackAccounts.indexOf(account);
                AuthConfig.attackAccounts.set(accountIndex, account);

            } catch (Exception e) {

            }
        }).on(ATTACK_STOPPED + responseSign, objects -> {
            stoppedAttacksCount++;
            if (stoppedAttacksCount == accounts.length() || accounts.length() == 0) {
                startAttackButton.setDisable(false);
                startAttackButton.setSelected(false);
                stopAttackButton.setSelected(false);
                stopAttackButton.setDisable(true);
                progressBar.setProgress(0.0);
                progressBar.setStyle("");
                attackSumField.setDisable(false);
                stoppedAttacksCount = 0;
            }
        });

        this.game = game;
    }

    private void setProgress(AttackResponce result) {
        StringBuilder message = new StringBuilder();
        String pass = "failed";
        if (result.getStatus()) {
            pass = "passed";
            message.append("passed").append(" | ");
            if (result.getSum() > 0) {
                message
//                    .append("Баланс = ").append(result.getDeposit()).append(" | ")
                    .append("Размер = ").append(result.getSum()).append(" | ")
                    .append("Таймер = ").append(result.getTimer()).append(" | ")
                    .append("Кэфф = ").append(result.getKoefficient()).append(" | ");
            }
        } else {
            message.append("failed").append(" | ");
        }

        if (result.getMessage().length() > 0) {
            message.append(result.getMessage()).append(" | ");
        }

        message.append(result.getFio());
        setProgress(pass, message.toString());
    }


    public BetItemController setName(String betName) {
        this.betName.setText(betName);
        return this;
    }

    public BetItemController setBetSighn() {
        if (gameAttack.getBet().getDirection().equals("less")) {
            this.betSign.setText("<");
            this.betSignTooltip.setText("Меньше");
        } else if (gameAttack.getBet().getDirection().equals("more")) {
            this.betSign.setText("<");
            this.betSignTooltip.setText("Больше");
        } else if (gameAttack.getBet().getDirection().equals("equal")) {
            this.betSign.setText("==");
            this.betSignTooltip.setText("Равно");
        }
        return this;
    }

    public BetItemController setBetRateValue(String betRateValue) {
        this.betRateValue.setText(betRateValue);
        return this;
    }

    public void setProgress(String passed, String message) {
        Platform.runLater(() -> {
            int ownersCount = accounts.length();

            if (ownersCount > 0) {
                if (messages.size() >= ownersCount) {
                    messages.clear();
                    successBets = 0;
                    failBets = 0;
                }
                if (passed.equals("start")) {
                    accountsInAttack.setText("Accounts: 0" + " / " + ownersCount);
                    return;
                }
                Double currentProgress = progressBar.getProgress();
                if (progressBar.getStyle().contains("gray")) {
                    currentProgress = 0.0;
                }
                if (passed.equals("passed")) {
                    successBets++;
                } else if (passed.equals("failed")) {
                    failBets++;
                }
                messages.add(message);
                String messageRes = "";
                //Сообщение преобразуем в multi-line message
                for (int i = 0; i < messages.size(); i++) {
                    messageRes = messageRes + messages.get(i) + "\n";
                }
                if (failBets > 0 && successBets > 0) {
                    progressBar.setStyle("-fx-accent: yellow; -fx-background-color: red");
                }
                if (failBets == 0 && successBets > 0) {
                    progressBar.setStyle("-fx-accent: green; -fx-background-color: green");
                }
                if (failBets > 0 && successBets == 0) {
                    progressBar.setStyle("-fx-accent: red; -fx-background-color: red");
                }
                accountsInAttack.setText("Accounts: " + (successBets + failBets) + " / " + ownersCount);

                progressBar.setProgress(Double.valueOf(successBets + failBets) / ownersCount);
                progressBarTooltip.setText(messageRes);
                if (progressBar.getProgress() == 1.0) {
                    startAttackButton.setDisable(false);
                    startAttackButton.setSelected(false);
                    stopAttackButton.setDisable(true);
                    attackSumField.setDisable(false);
                }

            }
        });
    }

    @FXML
    public void setBetSum(KeyEvent keyEvent) {
        if (attackSumField.getText().length() > 0) {
            addToAttackButton.setDisable(false);
            startAttackButton.setDisable(false);
        } else {
            addToAttackButton.setDisable(true);
            startAttackButton.setDisable(true);
        }
    }

    @FXML
    void startAttack(ActionEvent event) {
        successBets = 0;
        failBets = 0;
        accountsInAttack.setText("Accounts: 0 / 0");
        this.request = new AttackRequest()
            .game(new GameAttack()
                      .bet("totalPenalty", betRateValue.getText(), "less")
                      .url(this.game.getUrl()))
            .withMinKef(minKoefficient.getText())
            .sum(attackSumField.getText())
            .repeater(Config.REPEATER);

//        AttackRequest req = new AttackRequest()
//                .game(new AttackGame()
//                        .attackBet("asianFora", "1.2", "p125")
//                        .url("http://betcity.by/ru/live/soccer/13849/7350742"))
//                .sum(betSumField.getText());
        Gson gson = new GsonBuilder().create();
        String requestJSON = gson.toJson(this.request);

        List<String> victims = new ArrayList<>();
        //TODO: Выделить сервера в фильтр для атаки
        victims.add("betcity.by");
        MM_SOCKET.emit(INIT_ATTACK, requestJSON);
    }

    @FXML
    public void stopAttack(ActionEvent actionEvent) {
        if (!stopAttackButton.isSelected()) {
            stopAttackButton.setSelected(true);
            return;
        }
        startAttackButton.setDisable(true);
        List<String> victims = new ArrayList<>();
        //TODO: Выделить сервера в фильтр для атаки
        victims.add("betcity.by");
        Gson gson = new GsonBuilder().create();
        String requestJSON = gson.toJson(this.request);
        MM_SOCKET.emit("stop-attack", requestJSON);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;

        if (this.timer != null && progressBar.getProgress() <= 0.0) {
            Double betValue = Double.parseDouble(this.betRateValue.getText());

            if (Config.AUTOPILOT
                && !this.game.getChampionship().getName().toLowerCase(Locale.ROOT).contains("россия")
                && (((this.timer.getMinutes(game.getTimer()) >= 65) && (betValue >= 1.18))
                    || ((this.timer.getMinutes(game.getTimer()) >= 67) && (betValue >= 1.17))
                    || ((this.timer.getMinutes(game.getTimer()) >= 68) && (betValue >= 1.16))
                    || ((this.timer.getMinutes(game.getTimer()) >= 69) && (betValue >= 1.15))
                    || ((this.timer.getMinutes(game.getTimer()) >= 72) && (betValue >= 1.13))
                    || ((this.timer.getMinutes(game.getTimer()) >= 74) && (betValue >= 1.09)))
            ) {
                this.startAttackButton.setDisable(false);
                this.attackSumField.setText("7");
                this.startAttackButton.fire();
            }
        }
    }
}
