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
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;

@Log4j2
public class GameController {

    private App app;
    private DrawingThread timer;
    private Level level;
    private MapInfo currentMap;
    private GameSettings gameSettings;

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
        this.gameSettings = app.getGameSettings();
    }

    public void startGame(MapInfo map) {
        this.currentMap = map;
        log.info("Spouštím mapu '{}' s obtížností {}.", map.getMapName(), map.getDifficulty());

        // Převod String obtížnosti na enum Difficulty
        Difficulty difficulty = Difficulty.fromDisplayName(currentMap.getDifficulty());

        String musicFile = map.getMusicPath();
        if (musicFile != null && !musicFile.isEmpty()) {
            java.net.URL musicResource = getClass().getResource(musicFile);
            if (musicResource != null) {
                Media media = new Media(musicResource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(gameSettings.getMusicVolume());
                mediaPlayer.play();
            } else {
                log.warn("Hudební soubor '{}' nebyl nalezen v resources.", musicFile);
            }
        }

        level = new Level(canvas.getWidth(), canvas.getHeight(), difficulty, map, mediaPlayer, gameSettings);
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
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        finishGame(stats);
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

    private void finishGame(GameStats stats) {
        TextInputDialog dialog = new TextInputDialog("Anonymous");
        dialog.setTitle(I18n.text("dialog.gameFinished"));
        dialog.setHeaderText(String.format(
            "%s: %s | %s: %.1f%%%n%n" +
            "%s: %,d%n" +
            "Perfect: %d | Good: %d | Miss: %d%n" +
            "%s: %d",
            I18n.text("dialog.rating"),
            stats.getRank(),
            I18n.text("dialog.accuracy"),
            stats.getAccuracy(),
            I18n.text("game.score"),
            stats.getFinalScore(),
            stats.getPerfectCount(),
            stats.getGoodCount(),
            stats.getMissCount(),
            I18n.text("dialog.bestCombo"),
            stats.getMaxCombo()
        ));
        dialog.setContentText(I18n.text("dialog.enterName"));

        dialog.showAndWait();
        String name = dialog.getEditor().getText();
        if (name == null || name.isBlank()) {
            name = "Anonymous";
        }
        int finalScore = stats.getFinalScore();

        if (!name.isEmpty()) {
            finalScore = verifyPlayerAndUpdateScore(name, finalScore);
        }

        try {
            app.getGameApiClient().saveScore(currentMap.getId(), name, finalScore, stats);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Ukládání skóre hráče '{}' bylo přerušeno.", name, e);
        } catch (IOException e) {
            log.error("Nepodařilo se uložit skóre hráče '{}' na server.", name, e);
        }

        try {
            app.returnToMenu();
        } catch (Exception e) {
            log.error("Nepodařilo se vrátit do menu po dokončení hry.", e);
        }
    }

    private int verifyPlayerAndUpdateScore(String name, int score) {
        String url = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))) {
            StringBuilder content = new StringBuilder();
            String line = in.readLine();
            while (line != null) {
                content.append(line);
                content.append('\n');
                line = in.readLine();
            }

            if (content.toString().contains(name)) {
                log.info("Hráč '{}' nalezen v seznamu katedry, skóre bylo vynásobeno 10x.", name);
                return score * 10;
            }
        } catch (IOException | URISyntaxException e) {
            log.warn("Nepodařilo se ověřit hráče '{}' proti webu FEI.", name, e);
        }
        return score;
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

        // Speciální klávesy
        if (code == KeyCode.SPACE) {
            level.activateOverdrive();
            return;
        }
        if (code == KeyCode.ESCAPE) {
            togglePause();
            return;
        }

        // Kontrola dráhových kláves z nastavení
        int lane = gameSettings.getLaneForKey(code);
        if (lane >= 0 && lane < level.getLanecount()) {
            level.checkHit(lane);
        }
    }

    public void handleKeyRelease(KeyCode code) {
        if (level == null) return;

        pressedKeys.remove(code);

        // Kontrola dráhových kláves z nastavení
        int lane = gameSettings.getLaneForKey(code);
        if (lane >= 0 && lane < level.getLanecount()) {
            level.checkRelease(lane);
        }
    }

    private void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        if (isPaused) {
            return;
        }

        isPaused = true;
        pressedKeys.clear();
        pauseMenu.setVisible(true);

        if (timer != null) {
            timer.stop();
        }
        if (progressTimer != null) {
            progressTimer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void resumeGame() {
        if (!isPaused) {
            return;
        }

        isPaused = false;
        pressedKeys.clear();
        pauseMenu.setVisible(false);

        if (timer != null) {
            timer.start();
        }
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
        if (progressTimer != null) {
            progressTimer.start();
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
            pauseGame();
            app.switchToSettingsFromGame(this, currentMap);
        } catch (Exception e) {
            log.error("Nepodařilo se otevřít nastavení z pauzy.", e);
        }
    }

    public void stop() {
        log.debug("Zastavuji aktuální herní běh.");
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

    public void updateVolume() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(gameSettings.getMusicVolume());
        }
    }

    public void resume() {
        resumeGame();
    }
}
