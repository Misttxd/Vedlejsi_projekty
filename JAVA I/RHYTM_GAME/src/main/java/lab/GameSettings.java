package lab;

/**
 * Singleton třída pro uchování nastavení hry.
 * Nastavení přetrvává po dobu běhu aplikace.
 */
public class GameSettings {
    
    private static final GameSettings INSTANCE = new GameSettings();
    
    private double musicVolume = 1.0;  // 0.0 - 1.0
    private double sfxVolume = 1.0;    // 0.0 - 1.0
    private int noteOffsetMs = 0;      // v milisekundách (-200 až +200)
    
    private GameSettings() {
        // Singleton - privátní konstruktor
    }
    
    public static GameSettings getInstance() {
        return INSTANCE;
    }
    
    public double getMusicVolume() {
        return musicVolume;
    }
    
    public void setMusicVolume(double musicVolume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, musicVolume));
    }
    
    public double getSfxVolume() {
        return sfxVolume;
    }
    
    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, sfxVolume));
    }
    
    public int getNoteOffsetMs() {
        return noteOffsetMs;
    }
    
    public void setNoteOffsetMs(int noteOffsetMs) {
        this.noteOffsetMs = Math.max(-200, Math.min(200, noteOffsetMs));
    }
}
