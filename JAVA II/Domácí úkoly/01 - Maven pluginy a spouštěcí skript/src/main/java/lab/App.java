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

        // Načteme mapy pouze jednou při startu aplikace
        try {
            this.allMaps = lab.map.MapRepository.load();
        } catch (lab.map.MapException e) {
            e.printStackTrace();
            // Zde by bylo vhodné zobrazit chybu uživateli
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

        primaryStage.setScene(scene);
    }

    public void returnToMenu() throws Exception {
        // Uložíme všechny změny (nové high score) do souboru
        try {
            lab.map.MapRepository.save(allMaps);
        } catch (lab.map.MapException e) {
            e.printStackTrace();
            // Zobrazit chybu uživateli
        }
        // Až po uložení přepneme zpět do menu
        switchToMenu();
    }

    @Override
    public void stop() throws Exception {
        // Zajistíme, že se snažíme zastavit timer, jen pokud existuje
        if (gameController != null) {
            gameController.stop();
        }
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
