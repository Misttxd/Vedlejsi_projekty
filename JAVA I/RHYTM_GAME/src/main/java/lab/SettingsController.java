package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    
    @FXML
    void initialize() {
        GameSettings settings = GameSettings.getInstance();
        
        // Nastavení počátečních hodnot
        musicVolumeSlider.setValue(settings.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(settings.getSfxVolume() * 100);
        noteOffsetSlider.setValue(settings.getNoteOffsetMs());
        
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
        GameSettings settings = GameSettings.getInstance();
        settings.setMusicVolume(musicVolumeSlider.getValue() / 100.0);
        settings.setSfxVolume(sfxVolumeSlider.getValue() / 100.0);
        settings.setNoteOffsetMs((int) noteOffsetSlider.getValue());
        
        navigateBack();
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
