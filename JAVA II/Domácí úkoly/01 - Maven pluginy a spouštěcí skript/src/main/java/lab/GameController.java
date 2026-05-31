package lab;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import lab.map.MapInfo;

public class GameController {

    private App app;
    private DrawingThread timer;
    private Level level;
    private MapInfo currentMap;

    @FXML
    private Canvas canvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private ProgressBar overdriveBar;

    @FXML
    void initialize() {

        assert canvas != null : "fx:id='canvas' was not injected: check your FXML file 'gameWindow.fxml'.";
        assert scoreLabel != null : "fx:id='scoreLabel' was not injected: check your FXML file 'gameWindow.fxml'.";
        assert overdriveBar != null : "fx:id='overdriveBar' was not injected: check your FXML file 'gameWindow.fxml'.";
    }


    public void setApp(App app) {
        this.app = app;
    }


    public void startGame(MapInfo map) {
        this.currentMap = map;

        // Vytvoříme level a předáme mu počet not z vybrané mapy
        level = new Level(canvas.getWidth(), canvas.getHeight(), currentMap.getNoteCount());
        timer = new DrawingThread(canvas, level);

        level.addListener(new GameEventListener() {
            @Override
            public void onScoreChanged(int newScore) {
                scoreLabel.setText(String.format("%,06d", newScore));
            }

            @Override
            public void onOverdriveChanged(double newProgress) {
                overdriveBar.setProgress(newProgress);
            }

            @Override
            public void onGameFinished(int finalScore) {
                stop(); // Zastavíme timer

                // Zkontrolujeme a případně aktualizujeme high score
                if (finalScore > currentMap.getHighScore().getPoints()) {
                    // V reálné aplikaci bychom se zeptali na jméno
                    String playerName = "Player1"; 
                    currentMap.setHighScore(new lab.map.Score(finalScore, playerName));
                }

                // Bezpečně se vrátíme do menu (přes hlavní vlákno JavaFX)
                javafx.application.Platform.runLater(() -> {
                    try {
                        app.returnToMenu();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        timer.start();
    }

    public void handleKeyPress(KeyCode code) {
        if (level == null) return;

        switch (code) {
            case S -> level.checkHit(0);
            case D -> level.checkHit(1);
            case J -> level.checkHit(2);
            case K -> level.checkHit(3);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
}
