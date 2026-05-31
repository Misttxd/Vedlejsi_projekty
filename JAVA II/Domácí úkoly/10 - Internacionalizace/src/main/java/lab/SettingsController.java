package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lab.map.MapInfo;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SettingsController {

    @Setter
    private App app;
    private MapInfo returnToGameMap = null;
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
        musicVolumeSlider.setValue(GameSettings.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(GameSettings.getSfxVolume() * 100);

        lane1KeyField.setText(GameSettings.getLaneKey(0).getName());
        lane2KeyField.setText(GameSettings.getLaneKey(1).getName());
        lane3KeyField.setText(GameSettings.getLaneKey(2).getName());
        lane4KeyField.setText(GameSettings.getLaneKey(3).getName());
        lane5KeyField.setText(GameSettings.getLaneKey(4).getName());

        updateMusicVolumeLabel();
        updateSfxVolumeLabel();

        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateMusicVolumeLabel());
        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSfxVolumeLabel());
    }

    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("%.0f%%", musicVolumeSlider.getValue()));
    }

    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("%.0f%%", sfxVolumeSlider.getValue()));
    }


    public void setReturnToGame(GameController pausedController, MapInfo map) {
        this.pausedController = pausedController;
        this.returnToGameMap = map;
    }

    @FXML
    void onSaveClicked() {
        // Uložení hodnot do nastavení
        GameSettings.setMusicVolume(musicVolumeSlider.getValue() / 100.0);
        GameSettings.setSfxVolume(sfxVolumeSlider.getValue() / 100.0);

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
            GameSettings.setLaneKey(lane, key);
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
                // Návrat do hry bez restartu -> jen aktualizuj hlasitost
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
