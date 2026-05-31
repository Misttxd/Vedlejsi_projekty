package lab;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.api.GameApiClient;
import lab.map.MapInfo;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class App extends Application {

    private Stage primaryStage;
    private GameController gameController;
    private Scene gameScene = null;
    private List<MapInfo> allMaps = new ArrayList<>();
    private final GameApiClient gameApiClient = new GameApiClient();
    private final GameSettings gameSettings = new GameSettings();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(I18n.text("app.title"));
        log.info("Spouštím Rhythm Game.");

        loadMapsFromServer();

        switchToMenu();

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                exitProgram(event);
            }
        });
    }

    public void switchToMenu() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/lab/menu.fxml"), I18n.getBundle());
        Parent root = menuLoader.load();

        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        menuController.displayMaps(allMaps); // Předáme controlleru seznam map k zobrazení

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToGame(MapInfo map) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"), I18n.getBundle());
        Parent root = gameLoader.load();

        gameController = gameLoader.getController();
        gameController.setApp(this);
        gameController.startGame(map);

        gameScene = new Scene(root);
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                gameController.handleKeyPress(event.getCode());
            }
        });
        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                gameController.handleKeyRelease(event.getCode());
            }
        });

        primaryStage.setScene(gameScene);
    }

    public void returnToMenu() throws Exception {
        loadMapsFromServer();
        switchToMenu();
    }

    public void switchToSettings() throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/lab/settings.fxml"), I18n.getBundle());
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setApp(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void switchToSettingsFromGame(GameController pausedGameController, MapInfo currentMap) throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/lab/settings.fxml"), I18n.getBundle());
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

    public List<MapInfo> loadMapsFromServer() {
        try {
            this.allMaps = gameApiClient.loadMaps();
        } catch (IOException e) {
            log.error("Nepodařilo se načíst mapy ze serveru.", e);
            this.allMaps = new ArrayList<>();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Načítání map ze serveru bylo přerušeno.", e);
            this.allMaps = new ArrayList<>();
        }
        return allMaps;
    }

    public GameApiClient getGameApiClient() {
        return gameApiClient;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }


    @Override
    public void stop() throws Exception {
        log.info("Ukončuji Rhythm Game.");
        if (gameController != null) {
            gameController.stop();
        }
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
