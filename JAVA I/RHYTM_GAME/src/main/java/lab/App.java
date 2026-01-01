package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.map.MapInfo;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;
    private GameController gameController;
    private java.util.List<lab.map.MapInfo> allMaps; // Hlavní seznam map

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Rhythm Game");

        lab.map.MapRepository.init();
        lab.map.MapRepository.startDBWebServer();

        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            e.printStackTrace();
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

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> gameController.handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> gameController.handleKeyRelease(event.getCode()));

        primaryStage.setScene(scene);
    }

    public void returnToMenu() throws Exception {
        // Načteme čerstvá data z databáze (včetně nových skóre)
        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            e.printStackTrace();
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

    public void resumeGame(GameController pausedGameController, MapInfo map) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();

        gameController = gameLoader.getController();
        gameController.setApp(this);
        gameController.startGame(map);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> gameController.handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> gameController.handleKeyRelease(event.getCode()));

        primaryStage.setScene(scene);
    }

    @Override
    public void stop() throws Exception {
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
