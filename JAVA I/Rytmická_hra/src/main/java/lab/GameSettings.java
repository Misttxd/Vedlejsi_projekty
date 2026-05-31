package lab;

import javafx.scene.input.KeyCode;

public class GameSettings {

    // Statické proměnné pro nastavení
    private static double musicVolume = 0.4;  // 0.0 - 1.0
    private static double sfxVolume = 0.4;    // 0.0 - 1.0
    private static int noteOffsetMs = 0;      // v milisekundách (-200 až +200)

    // Klávesy pro jednotlivé dráhy (výchozí: S, D, J, K, L)
    private static KeyCode lane1Key = KeyCode.S;
    private static KeyCode lane2Key = KeyCode.D;
    private static KeyCode lane3Key = KeyCode.J;
    private static KeyCode lane4Key = KeyCode.K;
    private static KeyCode lane5Key = KeyCode.L;

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

    // Gettery a settery pro klávesy
    public static KeyCode getLaneKey(int lane) {
        if (lane == 0) {
            return lane1Key;
        } else if (lane == 1) {
            return lane2Key;
        } else if (lane == 2) {
            return lane3Key;
        } else if (lane == 3) {
            return lane4Key;
        } else {
            return lane5Key;
        }
    }

    public static void setLaneKey(int lane, KeyCode key) {
        if (lane == 0) {
            lane1Key = key;
        } else if (lane == 1) {
            lane2Key = key;
        } else if (lane == 2) {
            lane3Key = key;
        } else if (lane == 3) {
            lane4Key = key;
        } else {
            lane5Key = key;
        }
    }

    // Pomocná metoda pro získání čísla dráhy podle klávesy
    public static int getLaneForKey(KeyCode key) {
        if (key == lane1Key) {
            return 0;
        } else if (key == lane2Key) {
            return 1;
        } else if (key == lane3Key) {
            return 2;
        } else if (key == lane4Key) {
            return 3;
        } else if (key == lane5Key) {
            return 4;
        } else {
            return -1;  // Klávesa není přiřazena žádné dráze
        }
    }
}
