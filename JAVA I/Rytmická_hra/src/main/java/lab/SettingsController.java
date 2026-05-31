package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lab.map.MapInfo;

/**
 * Controller pro obrazovku nastavení.
 */
public class SettingsController {
    
    private App app;
    private MapInfo returnToGameMap = null;
    
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Label musicVolumeLabel;
    @FXML
    private Slider sfxVolumeSlider;
    @FXML
    private Label sfxVolumeLabel;
    @FXML
    private Slider noteOffsetSlider;
    @FXML
    private Label noteOffsetLabel;
    
    // TextFieldy pro nastavení kláves
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
        // Načtení aktuálních hodnot nastavení
        musicVolumeSlider.setValue(GameSettings.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(GameSettings.getSfxVolume() * 100);
        noteOffsetSlider.setValue(GameSettings.getNoteOffsetMs());
        
        // Načtení aktuálních kláves
        lane1KeyField.setText(GameSettings.getLaneKey(0).getName());
        lane2KeyField.setText(GameSettings.getLaneKey(1).getName());
        lane3KeyField.setText(GameSettings.getLaneKey(2).getName());
        lane4KeyField.setText(GameSettings.getLaneKey(3).getName());
        lane5KeyField.setText(GameSettings.getLaneKey(4).getName());
        
        // Aktualizace labelů
        updateMusicVolumeLabel();
        updateSfxVolumeLabel();
        updateNoteOffsetLabel();
        
        // Listenery pro změny
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateMusicVolumeLabel());
        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSfxVolumeLabel());
        noteOffsetSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateNoteOffsetLabel());
    }
    
    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("%.0f%%", musicVolumeSlider.getValue()));
    }
    
    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("%.0f%%", sfxVolumeSlider.getValue()));
    }
    
    private void updateNoteOffsetLabel() {
        noteOffsetLabel.setText(String.format("%+.0f ms", noteOffsetSlider.getValue()));
    }
    
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Nastaví návrat do hry místo do menu.
     */
    public void setReturnToGame(GameController pausedController, MapInfo map) {
        this.returnToGameMap = map;
    }
    
    @FXML
    void onSaveClicked() {
        // Uložení hodnot do nastavení
        GameSettings.setMusicVolume(musicVolumeSlider.getValue() / 100.0);
        GameSettings.setSfxVolume(sfxVolumeSlider.getValue() / 100.0);
        GameSettings.setNoteOffsetMs((int) noteOffsetSlider.getValue());
        
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
            // Neplatná klávesa - ignorujeme
        }
    }
    
    @FXML
    void onBackClicked() {
        navigateBack();
    }

    private void navigateBack() {
        try {
            if (returnToGameMap != null) {
                // Návrat do hry - spustí hru znovu s novou hlasitostí
                app.switchToGame(returnToGameMap);
            } else {
                app.switchToMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
