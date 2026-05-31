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
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.ResourceBundle;

@Log4j2
public class GameController {

    @Setter
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
    @FXML
    private ResourceBundle resources;

    private boolean isPaused = false;
    private AnimationTimer progressTimer;


    @FXML
    void initialize() {
        assert canvas != null : "fx:id='canvas' was not injected: check your FXML file 'gameWindow.fxml'.";
        assert scoreLabel != null : "fx:id='scoreLabel' was not injected: check your FXML file 'gameWindow.fxml'.";
        assert overdriveBar != null : "fx:id='overdriveBar' was not injected: check your FXML file 'gameWindow.fxml'.";
    }


    public void startGame(MapInfo map) {
        this.currentMap = map;
        log.info("Spouštím mapu '{}' s obtížností {}.", map.getMapName(), map.getDifficulty());

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
            } else {
                log.warn("Hudební soubor '{}' nebyl nalezen v resources.", musicFile);
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
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle(text("game.finishedTitle"));
                    dialog.setHeaderText(String.format(
                        text("game.finishedHeader"),
                        stats.getRank(),
                        stats.getAccuracy(),
                        stats.getFinalScore(),
                        stats.getPerfectCount(),
                        stats.getGoodCount(),
                        stats.getMissCount(),
                        stats.getMaxCombo()
                    ));
                    dialog.setContentText(text("game.namePrompt"));

                    Optional<String> result = dialog.showAndWait();
                    String name = result.orElse("Anonymous");
                    int finalScore = stats.getFinalScore();

                    if (result.isPresent() && !name.isEmpty()) {
                        String url="https://www.fei.vsb.cz/460/cs/kontakt/lide/";
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))){
                            String fileContent = in.lines().collect(Collectors.joining("\n"));
                            Pattern p = Pattern.compile(name);
                            Matcher m = p.matcher(fileContent);
                            if (m.find()) {
                                finalScore *= 10;
                                log.info("Hráč '{}' nalezen v seznamu katedry, skóre bylo vynásobeno 10x.", name);
                            }
                        } catch (IOException | URISyntaxException e) {
                            log.warn("Nepodařilo se ověřit hráče '{}' proti webu FEI.", name, e);
                        }
                    }

                    try {
                        lab.map.MapRepository.saveScore(currentMap.getId(), name, finalScore);
                    } catch (lab.map.MapException e) {
                        log.error("Nepodařilo se uložit skóre hráče '{}'.", name, e);
                    }
                    try {
                        app.returnToMenu();
                    } catch (Exception e) {
                        log.error("Nepodařilo se vrátit do menu po dokončení hry.", e);
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
        int lane = GameSettings.getLaneForKey(code);
        if (lane >= 0 && lane < level.getLanecount()) {
            level.checkHit(lane);
        }
    }

    public void handleKeyRelease(KeyCode code) {
        if (level == null) return;

        pressedKeys.remove(code);

        // Kontrola dráhových kláves z nastavení
        int lane = GameSettings.getLaneForKey(code);
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
        level.setPaused(true);

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
        level.setPaused(false);

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
        if (level != null) {
            level.stopAllNotes();
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
            mediaPlayer.setVolume(GameSettings.getMusicVolume());
        }
    }

    public void resume() {
        resumeGame();
    }

    private String text(String key) {
        return resources.getString(key);
    }
}
