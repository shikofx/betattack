package com.pkt.betattack.app.desk.controllers.main;

import com.pkt.betattack.app.api.pojo.attack.GameAttack;
import com.pkt.betattack.app.api.pojo.game.Game;
import com.pkt.betattack.app.desk.config.Config;
import com.pkt.betattack.app.desk.controllers.AbstractController;
import com.pkt.betattack.app.desk.utils.Browser;
import com.pkt.betattack.app.desk.utils.GameTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameItemController extends AbstractController implements Comparable<GameItemController> {

    private LiveController liveController;

    public HashMap<String, BetItemController> getBetControllers() {
        return betControllers;
    }

    HashMap<String, BetItemController> betControllers = new HashMap<>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox gameBox;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Hyperlink championship;

    @FXML
    private Text teamFirst;

    @FXML
    private Text teamSecond;

    @FXML
    private Text startGame;

    @FXML
    private Text timer;

    @FXML
    private Hyperlink gameLink;

    @FXML
    private VBox betsBox;

    public Game getGame() {
        return game;
    }

    private Game game;
    private GameTimer gameTimer = new GameTimer(this);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameItemController that = (GameItemController) o;
        return game.equals(that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game);
    }

    @FXML
    void openChampionship(ActionEvent event) {
        String champUrl = game.getChampionship().getUrl();
        Browser.openDefaultBrowser(champUrl);
    }

    @FXML
    void openGame(ActionEvent event) {
        String gameUrl = game.getUrl();
        Browser.openDefaultBrowser(gameUrl);
    }

    @FXML
    void initialize(LiveController liveController) {
        this.liveController = liveController;
        assert gameBox != null : "fx:id=\"gameBox\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert gameGrid != null : "fx:id=\"gameGrid\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert championship != null : "fx:id=\"championship\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert teamFirst != null : "fx:id=\"teamFirst\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert teamSecond != null : "fx:id=\"teamSecond\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert startGame != null : "fx:id=\"startGame\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert timer != null : "fx:id=\"timer\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert gameLink != null : "fx:id=\"gameLink\" was not injected: check your FXML file 'gameItem.fxml'.";
        assert betsBox != null : "fx:id=\"betsBox\" was not injected: check your FXML file 'gameItem.fxml'.";
    }

    private void colorTeams(Integer scoreTeamFirst, Integer scoreTeamSecond) {
        if (scoreTeamFirst > scoreTeamSecond) {
            this.teamFirst.setFill(Paint.valueOf(Config.WINNER_COLOR));
            this.teamSecond.setFill(Paint.valueOf(Config.LOOSER_COLOR));
        } else if (scoreTeamFirst < scoreTeamSecond) {
            this.teamFirst.setFill(Paint.valueOf(Config.LOOSER_COLOR));
            this.teamSecond.setFill(Paint.valueOf(Config.WINNER_COLOR));
        } else {
            this.teamFirst.setFill(Paint.valueOf(Config.NON_WINNER_COLOR));
            this.teamSecond.setFill(Paint.valueOf(Config.NON_WINNER_COLOR));
        }
    }

    public GameItemController setGame(Game gm) throws IOException {
        this.game = gm;
        this.championship.setText(this.game.getChampionship().getName());
        final Integer scoreTeamFirst = this.game.getTeamFirst().getScore();
        final Integer scoreTeamSecond = this.game.getTeamSecond().getScore();
        colorTeams(scoreTeamFirst, scoreTeamSecond);
        this.teamFirst.setText(this.game.getTeamFirst().getName() + "\n" + scoreTeamFirst);
        this.teamSecond.setText(this.game.getTeamSecond().getName() + "\n" + scoreTeamSecond);
        this.startGame.setText(getDateTime(this.game));
//        gameTimer.start(game.getTimer());
        this.setTimer(this.game.getTimer());
        String betName = "";
        String betValue = "";

        for (Map.Entry<String, List<Map<String, String>>> betCase : this.game.getBets().getTotalPenalty().entrySet()) {
            String key = betCase.getKey();
            cleanBetList();
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource(Config.RESOURCE_BET_ITEM_FXML));
            Node node = loader.load();
            BetItemController betItemController = (BetItemController) getController(node);
            if (key.equals("value")) {
                Integer indexOfLast = betCase.getValue().size() - 1;
                try {
                    Collection<String> values = betCase.getValue().get(indexOfLast).values();
                    if (values.size() > 0) {
                        betName =
                            "Пенальти ( " + (String) betCase.getValue().get(indexOfLast).values().toArray()[0] + " )";
                    }
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
                continue;

            } else if (key.equals("less")) {
                Integer indexOfLast = betCase.getValue().size() - 1;
                if(betCase.getValue().get(indexOfLast).values().size() > 0) {
                    betValue = (String) betCase.getValue().get(indexOfLast).values().toArray()[0];
                    betItemController.setBetRateValue(betValue);
                } else {
                    betItemController.setBetRateValue("0");
                }
                this.betControllers.put(key, betItemController);
            }
            betItemController.setGame(this.game);
            betItemController.setName(betName);
            GameAttack gameAttack = new GameAttack()
                .bet("totalPenalty", "0", "less")
                .url(gm.getUrl());
            betItemController.initialize(this.gameTimer, gameAttack);
            this.betsBox.getChildren().add(node);
        }
        return this;
    }

    public GameItemController updateGame(Game gm) throws IOException {
        this.setTimer(gm.getTimer());
        final Integer scoreTeamFirst = gm.getTeamFirst().getScore();
        final Integer scoreTeamSecond = gm.getTeamSecond().getScore();
        if (!this.game.getTeamFirst().getScore().equals(scoreTeamFirst) ||
            !this.game.getTeamSecond().getScore().equals(scoreTeamSecond)) {
            colorTeams(scoreTeamFirst, scoreTeamSecond);
            this.teamFirst.setText(gm.getTeamFirst().getName() + "\n" + scoreTeamFirst);
            this.teamSecond.setText(gm.getTeamSecond().getName() + "\n" + scoreTeamSecond);
        }

        if (!this.game.getBets().equals(gm.getBets()) || !this.game.getTimer().equals(gm.getTimer())) {
            String betName = "";
            String betValue = "";

            for (Map.Entry<String, List<Map<String, String>>> betCase : gm.getBets().getTotalPenalty().entrySet()) {
                String key = betCase.getKey();
                if (key.equals("value")) {
                    Integer indexOfLast = betCase.getValue().size() - 1;
                    betName = "Пенальти ( " + (String) betCase.getValue().get(indexOfLast).values().toArray()[0] + " )";
                    continue;
                }
                BetItemController betItemController;
                if (this.betControllers.size() > 0 && this.betControllers.get(key) != null) {
                    betItemController = this.betControllers.get(key);
                } else {
                    FXMLLoader loader =
                        new FXMLLoader(getClass().getResource(Config.RESOURCE_BET_ITEM_FXML));
                    Node node = loader.load();
                    this.betsBox.getChildren().add(node);
                    betItemController = (BetItemController) getController(node);
                    GameAttack gameAttack = new GameAttack()
                        .bet("totalPenalty", "0", "less")
                        .url(gm.getUrl());
                    betItemController.initialize(this.gameTimer, gameAttack);
                    this.betControllers.put(key, betItemController);
                }
                betItemController.setGame(gm);
                betItemController.setName(betName);
                Integer indexOfLast = betCase.getValue().size() - 1;
                betValue = (String) betCase.getValue().get(indexOfLast).values().toArray()[0];
                betItemController.setBetRateValue(betValue);
            }
        }
        this.game = gm;
        return this;
    }

    public void setTimer(String timerStr) {
        this.timer.setText(timerStr);
    }

    private void cleanBetList() {
        ObservableList<Node> children = betsBox.getChildren();
        children.remove(0, children.size());
    }

    private String getDateTime(Game game) {
        String str = game.getDate().substring(0, 10);
        LocalDate localDate = LocalDate.parse(str);
        return localDate.toString() + "\n" + game.getStartTime();
    }

    @Override
    public int compareTo(GameItemController o) {
        return o.getGame().getTimer().compareTo(this.getGame().getTimer());
    }
}
