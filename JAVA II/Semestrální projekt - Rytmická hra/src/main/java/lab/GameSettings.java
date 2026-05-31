package lab;

import javafx.scene.input.KeyCode;
import lombok.Getter;

public class GameSettings {

    @Getter
    private double musicVolume = 0.4;  // 0.0 - 1.0
    @Getter
    private double sfxVolume = 0.4;    // 0.0 - 1.0

    private final KeyCode[] laneKeys = {
        KeyCode.S,
        KeyCode.D,
        KeyCode.J,
        KeyCode.K,
        KeyCode.L
    };

    public void setMusicVolume(double volume) {
        if (volume < 0.0) {
            musicVolume = 0.0;
        } else if (volume > 1.0) {
            musicVolume = 1.0;
        } else {
            musicVolume = volume;
        }
    }

    public void setSfxVolume(double volume) {
        if (volume < 0.0) {
            sfxVolume = 0.0;
        } else if (volume > 1.0) {
            sfxVolume = 1.0;
        } else {
            sfxVolume = volume;
        }
    }

    public KeyCode getLaneKey(int lane) {
        return laneKeys[lane];
    }

    public void setLaneKey(int lane, KeyCode key) {
        laneKeys[lane] = key;
    }

    public int getLaneForKey(KeyCode key) {
        for (int i = 0; i < laneKeys.length; i++) {
            if (key == laneKeys[i]) {
                return i;
            }
        }
        return -1;
    }
}
