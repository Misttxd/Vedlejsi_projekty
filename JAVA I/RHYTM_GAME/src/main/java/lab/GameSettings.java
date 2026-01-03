package lab;

/**
 * Třída pro uchování nastavení hry.
 * Používá statické proměnné pro přístup odkudkoliv.
 */
public class GameSettings {
    
    // Statické proměnné pro nastavení
    private static double musicVolume = 1.0;  // 0.0 - 1.0
    private static double sfxVolume = 1.0;    // 0.0 - 1.0
    private static int noteOffsetMs = 0;      // v milisekundách (-200 až +200)
    
    public static double getMusicVolume() {
        return musicVolume;
    }
    
    public static void setMusicVolume(double volume) {
        if (volume < 0.0) {
            musicVolume = 0.0;
        } else if (volume > 1.0) {
            musicVolume = 1.0;
        } else {
            musicVolume = volume;
        }
    }
    
    public static double getSfxVolume() {
        return sfxVolume;
    }
    
    public static void setSfxVolume(double volume) {
        if (volume < 0.0) {
            sfxVolume = 0.0;
        } else if (volume > 1.0) {
            sfxVolume = 1.0;
        } else {
            sfxVolume = volume;
        }
    }
    
    public static int getNoteOffsetMs() {
        return noteOffsetMs;
    }
    
    public static void setNoteOffsetMs(int offset) {
        if (offset < -200) {
            noteOffsetMs = -200;
        } else if (offset > 200) {
            noteOffsetMs = 200;
        } else {
            noteOffsetMs = offset;
        }
    }
}
