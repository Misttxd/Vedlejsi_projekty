package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lab.map.MapInfo;

import java.util.HashSet;
import java.util.Set;

public class GameController {

    private App app;
    private DrawingThread timer;
    private Level level;
    private MapInfo currentMap;

    private MediaPlayer mediaPlayer;
    
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    @FXML
    private Canvas canvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private ProgressBar overdriveBar;

    @FXML
    private Label comboLabel;

    @FXML
    private VBox pauseMenu;

    private boolean isPaused = false;


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

        // Převod String obtížnosti na enum Difficulty
        Difficulty difficulty = Difficulty.fromDisplayName(currentMap.getDifficulty());

        String musicFile = map.getMusicPath();
        if (musicFile != null && !musicFile.isEmpty()) {
            var musicResource = getClass().getResource(musicFile);
            if (musicResource != null) {
                Media media = new Media(musicResource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        }

        level = new Level(canvas.getWidth(), canvas.getHeight(), difficulty, map, mediaPlayer);
        timer = new DrawingThread(canvas, level);

        level.addListener(new GameEventListener() {
            @Override
            public void onScoreChanged(int newScore) {
                scoreLabel.setText(String.format("%,d", newScore));
            }

            @Override
            public void onComboChanged(int newCombo) {
                int multiplier = (newCombo / 10) + 1;
                // Zobrazí combo i multiplier pro debugging
                comboLabel.setText(String.format("%d (%dx)", newCombo, multiplier));
            }

            @Override
            public void onOverdriveChanged(double newProgress) {
                overdriveBar.setProgress(newProgress);
            }

            @Override
            public void onGameFinished(GameStats stats) {
                stop();
                javafx.application.Platform.runLater(() -> {
                    // Zobrazení statistik v dialogu
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Hra dokončena!");
                    dialog.setHeaderText(String.format(
                        "Hodnocení: %s | Přesnost: %.1f%%%n%n" +
                        "Skóre: %,d%n" +
                        "Perfect: %d | Good: %d | Miss: %d%n" +
                        "Nejvyšší combo: %d",
                        stats.getRank(),
                        stats.getAccuracy(),
                        stats.getFinalScore(),
                        stats.getPerfectCount(),
                        stats.getGoodCount(),
                        stats.getMissCount(),
                        stats.getMaxCombo()
                    ));
                    dialog.setContentText("Zadej své jméno pro uložení skóre:");
                    try {
                        lab.map.MapRepository.saveScore(currentMap.getId(), dialog.showAndWait().orElse("Anonymous"), stats.getFinalScore());
                    } catch (lab.map.MapException e) {
                        e.printStackTrace();
                    }
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
        
        // Ignoruj opakované stisky (key repeat)
        if (pressedKeys.contains(code)) {
            return;
        }
        pressedKeys.add(code);

        switch (code) {
            case S -> level.checkHit(0);
            case D -> level.checkHit(1);
            case J -> level.checkHit(2);
            case K -> level.checkHit(3);
            case L -> level.checkHit(4);
            case SPACE -> level.activateOverdrive();
            case ESCAPE -> togglePause();
        }
    }

    public void handleKeyRelease(KeyCode code) {
        if (level == null) return;
        
        pressedKeys.remove(code);

        switch (code) {
            case S -> level.checkRelease(0);
            case D -> level.checkRelease(1);
            case J -> level.checkRelease(2);
            case K -> level.checkRelease(3);
            case L -> level.checkRelease(4);
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);

        if (isPaused) {
            timer.stop();
            if (mediaPlayer != null) mediaPlayer.pause();
        } else {
            timer.start();
            if (mediaPlayer != null) mediaPlayer.play();
        }
    }

    @FXML
    public void onMenuClicked(ActionEvent event) throws Exception {
        stop();
        app.returnToMenu();
    }

    @FXML
    public void onResumeClicked(ActionEvent event) {
        togglePause();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

