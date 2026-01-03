package lab;

import javafx.animation.AnimationTimer;
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
import javafx.util.Duration;
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

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private Label songTimeLabel;

    private boolean isPaused = false;
    private AnimationTimer progressTimer;


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
                mediaPlayer.setVolume(GameSettings.getMusicVolume());
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
            public void onComboChanged(int newCombo, int multiplier, boolean overdriveActive) {
                int displayMultiplier;
                if (overdriveActive) {
                    displayMultiplier = multiplier * 2;
                } else {
                    displayMultiplier = multiplier;
                }
                comboLabel.setText(String.format("%d (%dx)", newCombo, displayMultiplier));
                
                if (overdriveActive) {
                    comboLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                } else {
                    comboLabel.setTextFill(javafx.scene.paint.Color.CYAN);
                }
            }

            @Override
            public void onOverdriveChanged(double newProgress, boolean overdriveActive) {
                overdriveBar.setProgress(newProgress);
                
                // Změna barvy combo labelu při změně overdrive stavu
                if (overdriveActive) {
                    comboLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                } else {
                    comboLabel.setTextFill(javafx.scene.paint.Color.CYAN);
                }
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
        
        // Timer pro aktualizaci progress baru
        progressTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSongProgress();
            }
        };
        progressTimer.start();
        
        timer.start();
    }

    private void updateSongProgress() {
        if (mediaPlayer != null && songProgressBar != null) {
            Duration current = mediaPlayer.getCurrentTime();
            Duration total = mediaPlayer.getTotalDuration();
            
            if (total != null && total.toSeconds() > 0) {
                double progress = current.toSeconds() / total.toSeconds();
                songProgressBar.setProgress(progress);
                
                // Formát času jako M:SS
                int currentSec = (int) current.toSeconds();
                int totalSec = (int) total.toSeconds();
                songTimeLabel.setText(String.format("%d:%02d / %d:%02d", 
                    currentSec / 60, currentSec % 60,
                    totalSec / 60, totalSec % 60));
            }
        }
    }

    public void handleKeyPress(KeyCode code) {
        if (level == null) return;
        
        // Ignoruj opakované stisky (key repeat)
        if (pressedKeys.contains(code)) {
            return;
        }
        pressedKeys.add(code);

        switch (code) {
            case S:
                level.checkHit(0);
                break;
            case D:
                level.checkHit(1);
                break;
            case J:
                level.checkHit(2);
                break;
            case K:
                level.checkHit(3);
                break;
            case L:
                level.checkHit(4);
                break;
            case SPACE:
                level.activateOverdrive();
                break;
            case ESCAPE:
                togglePause();
                break;
        }
    }

    public void handleKeyRelease(KeyCode code) {
        if (level == null) return;
        
        pressedKeys.remove(code);

        switch (code) {
            case S:
                level.checkRelease(0);
                break;
            case D:
                level.checkRelease(1);
                break;
            case J:
                level.checkRelease(2);
                break;
            case K:
                level.checkRelease(3);
                break;
            case L:
                level.checkRelease(4);
                break;
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

    @FXML
    public void onSettingsClicked(ActionEvent event) {
        try {
            app.switchToSettingsFromGame(this, currentMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        
        if (progressTimer != null) {
            progressTimer.stop();
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

