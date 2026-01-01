package lab;

import javafx.scene.paint.Color;

/**
 * Centrální definice barev pro jednotlivé dráhy.
 * Používá se v Level.java i Note.java.
 */
public final class LaneColors {
    
    private LaneColors() {
        // Utility class - nelze vytvořit instanci
    }

    /**
     * Barvy pro jednotlivé dráhy (indexované od 0).
     * 0 = S (zelená), 1 = D (růžová), 2 = J (modrá), 3 = K (žlutá), 4 = L (fialová)
     */
    public static final Color[] COLORS = {
        Color.rgb(0, 200, 83),     // Zelená - S
        Color.rgb(255, 64, 129),   // Růžová - D
        Color.rgb(41, 121, 255),   // Modrá - J
        Color.rgb(255, 193, 7),    // Žlutá - K
        Color.rgb(156, 39, 176)    // Fialová - L
    };

    /**
     * Vrátí barvu pro danou dráhu.
     * @param lane index dráhy (0-4)
     * @return barva dráhy
     */
    public static Color getColor(int lane) {
        return COLORS[lane % COLORS.length];
    }
}
