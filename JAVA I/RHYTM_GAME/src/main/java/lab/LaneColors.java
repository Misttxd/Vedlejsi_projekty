package lab;

import javafx.scene.paint.Color;


public final class LaneColors {

    private LaneColors() {
        // Utility class - nelze vytvořit instanci
    }


    public static final Color[] COLORS = {
        Color.rgb(0, 200, 83),     // Zelená - S
        Color.rgb(255, 64, 129),   // Růžová - D
        Color.rgb(255, 255, 0),    // Žlutá - J
        Color.rgb(41, 121, 255),   // Modrá - K
        Color.rgb(255, 172, 28)    // Fialová - L
    };


    public static Color getColor(int lane) {
        return COLORS[lane % COLORS.length];
    }
}
