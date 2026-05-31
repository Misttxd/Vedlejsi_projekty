package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lab.map.MapInfo;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SettingsController {

    private App app;
    private GameSettings gameSettings;
    private GameController pausedController = null;

    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Label musicVolumeLabel;
    @FXML
    private Slider sfxVolumeSlider;
    @FXML
    private Label sfxVolumeLabel;

    
    
    @FXML
    private TextField lane1KeyField;
    @FXML
    private TextField lane2KeyField;
    @FXML
    private TextField lane3KeyField;
    @FXML
    private TextField lane4KeyField;
    @FXML
    private TextField lane5KeyField;

    @FXML
    void initialize() {
        musicVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateMusicVolumeLabel();
            }
        });
        sfxVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateSfxVolumeLabel();
            }
        });
    }

    public void setApp(App app) {
        this.app = app;
        this.gameSettings = app.getGameSettings();
        loadSettings();
    }

    private void loadSettings() {
        musicVolumeSlider.setValue(gameSettings.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(gameSettings.getSfxVolume() * 100);

        lane1KeyField.setText(gameSettings.getLaneKey(0).getName());
        lane2KeyField.setText(gameSettings.getLaneKey(1).getName());
        lane3KeyField.setText(gameSettings.getLaneKey(2).getName());
        lane4KeyField.setText(gameSettings.getLaneKey(3).getName());
        lane5KeyField.setText(gameSettings.getLaneKey(4).getName());

        updateMusicVolumeLabel();
        updateSfxVolumeLabel();
    }

    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("%.0f%%", musicVolumeSlider.getValue()));
    }

    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("%.0f%%", sfxVolumeSlider.getValue()));
    }


    public void setReturnToGame(GameController pausedController, MapInfo map) {
        this.pausedController = pausedController;
    }

    @FXML
    void onSaveClicked() {
        // Uložení hodnot do nastavení
        gameSettings.setMusicVolume(musicVolumeSlider.getValue() / 100.0);
        gameSettings.setSfxVolume(sfxVolumeSlider.getValue() / 100.0);

        // Uložení kláves
        saveKeyBinding(0, lane1KeyField.getText());
        saveKeyBinding(1, lane2KeyField.getText());
        saveKeyBinding(2, lane3KeyField.getText());
        saveKeyBinding(3, lane4KeyField.getText());
        saveKeyBinding(4, lane5KeyField.getText());

        navigateBack();
    }

    private void saveKeyBinding(int lane, String keyName) {
        try {
            KeyCode key = KeyCode.valueOf(keyName.toUpperCase());
            gameSettings.setLaneKey(lane, key);
        } catch (IllegalArgumentException e) {
            log.warn("Neplatná klávesa '{}' pro dráhu {}.", keyName, lane + 1);
        }
    }

    @FXML
    void onBackClicked() {
        navigateBack();
    }

    private void navigateBack() {
        try {
            if (pausedController != null) {

                pausedController.updateVolume();
                app.resumeGame(pausedController);
            } else {
                app.switchToMenu();
            }
        } catch (Exception e) {
            log.error("Nepodařilo se vrátit z nastavení.", e);
        }
    }
}
