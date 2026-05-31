package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.map.MapInfo;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Log4j2
public class App extends Application {

    private Stage primaryStage;
    private GameController gameController;
    private Scene gameScene = null;
    private java.util.List<lab.map.MapInfo> allMaps;
    private ResourceBundle texts;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        texts = ResourceBundle.getBundle("lab.messages", Locale.getDefault());
        primaryStage.setTitle(texts.getString("app.title"));
        log.info("Spouštím Rhythm Game.");

        lab.map.MapRepository.init();
        lab.map.MapRepository.startDBWebServer();

        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            log.error("Nepodařilo se načíst mapy z databáze.", e);
            this.allMaps = new java.util.ArrayList<>();
        }

        switchToMenu();

        primaryStage.show();
        primaryStage.setOnCloseRequest(this::exitProgram);
    }

    public void switchToMenu() throws IOException {
        FXMLLoader menuLoader = createLoader("/lab/menu.fxml");
        Parent root = menuLoader.load();

        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        menuController.displayMaps(allMaps); // Předáme controlleru seznam map k zobrazení

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToGame(MapInfo map) throws IOException {
        FXMLLoader gameLoader = createLoader("/lab/gameWindow.fxml");
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
            log.error("Nepodařilo se znovu načíst mapy při návratu do menu.", e);
        }
        switchToMenu();
    }

    public void switchToSettings() throws IOException {
        FXMLLoader settingsLoader = createLoader("/lab/settings.fxml");
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setApp(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToSettingsFromGame(GameController pausedGameController, MapInfo currentMap) throws IOException {
        FXMLLoader settingsLoader = createLoader("/lab/settings.fxml");
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setApp(this);
        settingsController.setReturnToGame(pausedGameController, currentMap);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void resumeGame(GameController controller) { //resume bez resetu - pouzij ulozenou scenu
        primaryStage.setScene(gameScene);
        controller.resume();
    }


    @Override
    public void stop() throws Exception {
        log.info("Ukončuji Rhythm Game.");
        if (gameController != null) {
            gameController.stop();
        }
        lab.map.MapRepository.stopDBWebServer();
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }

    private FXMLLoader createLoader(String fxmlPath) {
        return new FXMLLoader(getClass().getResource(fxmlPath), texts);
    }
}
