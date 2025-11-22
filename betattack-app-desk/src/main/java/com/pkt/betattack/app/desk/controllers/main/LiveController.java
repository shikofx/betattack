package com.pkt.betattack.app.desk.controllers.main;


import static com.pkt.betattack.app.desk.config.Config.AUTOPILOT;
import static com.pkt.betattack.app.desk.config.Config.REQUEST_DELAY;

import com.pkt.betattack.app.api.client.controllers.AccountApiController;
import com.pkt.betattack.app.api.client.controllers.GameApiController;
import com.pkt.betattack.app.api.pojo.game.Game;
import com.pkt.betattack.app.desk.config.Config;
import com.pkt.betattack.app.desk.controllers.AbstractController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

public class LiveController extends AbstractController {

    private final List<GameItemController> gameControllers = Collections.synchronizedList(new ArrayList<>());


    @FXML
    public VBox contentBox;
    @FXML
    public Label appHeaderLabel;
    @FXML
    public Button settingsButton;
    @FXML
    public Button fxmlButtonCollapseApplication;
    @FXML
    public Button fxmlButtonMinimizeApplication;
    @FXML
    public Button fxmlButtonCloseApplication;
    @FXML
    public Tab liveTab;
    @FXML
    public ScrollPane eventsScrollPane;
    @FXML
    public VBox gamesBox;
    @FXML
    public VBox serverBox;
    @FXML
    public WebView webViewer;
    @FXML
    public SVGPath autopilotSwitcherIcon;
    @FXML
    public SVGPath accountsStatusIcon;
    @FXML
    public MenuBar accountsStatusMenu;
    @FXML
    public SVGPath datacentersStatusSVG;
    @FXML
    public MenuBar dataServersStatusMenu;
    @FXML
    public SVGPath massegeCenterConnectionStatus;
    @FXML
    public ToggleButton fxmlButtonAutopilotSwitcher;
    @FXML
    public Label waitLabel;

    //Автопилот

    @FXML
    public void collapseApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) fxmlButtonCollapseApplication.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void minimizeDashboard(ActionEvent actionEvent) {
        Stage stage = (Stage) fxmlButtonMinimizeApplication.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) fxmlButtonCloseApplication.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) {

    }

    @FXML
    public void liveTabActivation(Event event) {
    }

    @FXML
    public void autopilotSwitcherClick(MouseEvent mouseEvent) {
        if (AUTOPILOT) {
            AUTOPILOT = false;
            autopilotSwitcherIcon.setFill(getNotActiveIconColor());
        } else {
            AUTOPILOT = true;
            autopilotSwitcherIcon.setFill(getActiveIconColor());
        }
    }

    @FXML
    public void fxmlAutopilotActionHandler(ActionEvent actionEvent) {
        if (AUTOPILOT) {
            AUTOPILOT = false;
            autopilotSwitcherIcon.setFill(getNotActiveIconColor());
        } else {
            AUTOPILOT = true;
            autopilotSwitcherIcon.setFill(getActiveIconColor());
        }
    }

    @FXML
    public void openSettingsView(MouseEvent mouseEvent) {
    }

    public LiveController getInstance() {
        return this;
    }

    public void initialize() {
        getGamesInSeparateThread();
        getAccountsInSeparateThread();
        waitLabel.setVisible(true);
    }

    public void setHeaderText(String headerText) {
        appHeaderLabel.setText(headerText);
    }

    private void getGamesInSeparateThread() {
        Runnable r = () -> {
            while (liveTab.isSelected()) {
                new GameApiController().getAll(this, 3);
                try {
                    Thread.sleep(REQUEST_DELAY);
                } catch (InterruptedException e) {
                }
            }
        };
        Thread requestThread = new Thread(r, "request");
        requestThread.start();
    }

    private void getAccountsInSeparateThread() {
        Runnable r = () -> {
            new AccountApiController().getAll(this);
        };
        Thread requestThread = new Thread(r, "request");
        requestThread.start();
    }

    public void updateGames(List<Game> games) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Game game : games) {
                        FXMLLoader
                            loader =
                            new FXMLLoader(getClass().getResource(Config.GAME_ITEM_FXML));

                        List<GameItemController> controllerByGameUrl = getControllerForGame(game);

                        if (controllerByGameUrl.isEmpty()) {
                            Node node = loader.load();
                            GameItemController gameItemController = (GameItemController) getController(node);
                            gameItemController
                                .setGame(game);
                            gameItemController.initialize(getInstance());
                            boolean isAdded = gameControllers.add(gameItemController);
                            Collections.sort(gameControllers);

                            if (isAdded) {
                                Integer index = gameControllers.indexOf(gameItemController);
                                gamesBox.getChildren().add(index, node);
                            }

                        } else {
                            GameItemController currentController = controllerByGameUrl.get(0);
                            currentController.updateGame(game);
                        }
                    }
                    try {
                        for (GameItemController controller : gameControllers) {
                            List<Game> gameList;
                            if (!gameControllers.isEmpty()) {
                                waitLabel.setVisible(false);
                                gameList =
                                    games.stream()
                                        .filter(game -> game.getUrl().equals(controller.getGame().getUrl()))
                                        .collect(Collectors.toList());

                                if (gameList.isEmpty()) {
                                    Node node = getNode(controller);
                                    gamesBox.getChildren().remove(node);
                                    gameControllers.remove(controller);
                                    if (gameControllers.isEmpty())
                                        waitLabel.setVisible(true);
                                }
                            }
                        }
//                        try {
//                            gamesBox.getChildren().sort((o1, o2) -> {
//                                if (null != o1.getUserData() && null != o2.getUserData() ) {
//                                    Game game1 = ((GameItemController) o1.getUserData()).getGame();
//                                    Game game2 = ((GameItemController) o2.getUserData()).getGame();
//                                    return game2.getStartTime().compareTo(game1.getStartTime());
//                                }
//                                return 0;
//                            });
//                        } catch (IllegalArgumentException e) {
//
//                        }

                    } catch (ConcurrentModificationException e) {

                    }


                } catch (IOException e) {
                    appHeaderLabel.setText(e.getMessage());
                }

            }
        });
    }

    private List<GameItemController> getControllerForGame(Game game) {
        return gameControllers.stream()
            .filter(controller -> controller.getGame().equals(game)).collect(
                Collectors.toList());
    }

    private Node getNode(GameItemController controller) {
        for (Node nd : gamesBox.getChildren()) {
            if (nd.getUserData() != null && nd.getUserData().equals(controller)) {
                return nd;
            }
        }
        return null;
    }

    private void cleanGameList() {
        ObservableList<Node> children = gamesBox.getChildren();
        children.remove(0, children.size());
    }

    private void openUrl(final String url) {
        WebEngine browser = webViewer.getEngine();
        browser.setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        browser.load(url);
    }

    private Color getNotActiveIconColor() {
        return Color.DARKGRAY;
    }

    private Color getActiveIconColor() {
        return Color.CHOCOLATE;
    }
}
