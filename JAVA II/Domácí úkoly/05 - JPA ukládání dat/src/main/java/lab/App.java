package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.map.MapInfo;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class App extends Application {

    private Stage primaryStage;
    private GameController gameController;
    private Scene gameScene = null;
    private java.util.List<lab.map.MapInfo> allMaps;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting up Rhythm Game Application.");
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Rhythm Game");

        lab.map.MapRepository.init();
        lab.map.MapRepository.startDBWebServer();

        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            log.error("Failed to load maps from repository", e);
            this.allMaps = new java.util.ArrayList<>();
        }

        switchToMenu();

        primaryStage.show();
        primaryStage.setOnCloseRequest(this::exitProgram);
    }

    public void switchToMenu() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/lab/menu.fxml"));
        Parent root = menuLoader.load();

        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        menuController.displayMaps(allMaps); // Předáme controlleru seznam map k zobrazení

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToGame(MapInfo map) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();

        gameController = gameLoader.getController();
        gameController.setApp(this);
        gameController.startGame(map);

        gameScene = new Scene(root);
        gameScene.setOnKeyPressed(event -> gameController.handleKeyPress(event.getCode()));
        gameScene.setOnKeyReleased(event -> gameController.handleKeyRelease(event.getCode()));

        primaryStage.setScene(gameScene);
    }

    public void returnToMenu() throws Exception {
        // Načteme čerstvá data z databáze (včetně nových skóre)
        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            log.error("Failed to reload maps from repository", e);
        }
        switchToMenu();
    }

    public void switchToSettings() throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/lab/settings.fxml"));
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setApp(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToSettingsFromGame(GameController pausedGameController, MapInfo currentMap) throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/lab/settings.fxml"));
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setApp(this);
        settingsController.setReturnToGame(pausedGameController, currentMap);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void resumeGame(GameController controller) { // resume bez resetu - pouzij ulozenou scenu
        primaryStage.setScene(gameScene);
        controller.resume();
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping Rhythm Game Application.");
        if (gameController != null) {
            gameController.stop();
        }
        lab.map.MapRepository.stopDBWebServer();
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
