package lab;

import javafx.scene.paint.Color;


public final class LaneColors {

    private LaneColors() {
        // Utility class -> nelze vytvorit instanci
    }

    // barvy vybrane podle Guitar Hero
    public static final Color[] COLORS = {
        Color.rgb(0, 200, 83),     // Zelená - S
        Color.rgb(255, 64, 129),   // Růžová - D
        Color.rgb(255, 255, 0),    // Žlutá - J
        Color.rgb(41, 121, 255),   // Modrá - K
        Color.rgb(255, 172, 28)    // Orange - L
    };


    public static Color getColor(int lane) {
        int index = lane % COLORS.length;
        if (index == 0) {
            return COLORS[0];
        } else if (index == 1) {
            return COLORS[1];
        } else if (index == 2) {
            return COLORS[2];
        } else if (index == 3) {
            return COLORS[3];
        } else {
            return COLORS[4];
        }
    }
}
