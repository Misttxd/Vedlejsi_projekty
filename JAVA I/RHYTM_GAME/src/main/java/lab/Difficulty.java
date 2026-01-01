package lab;

/**
 * Enum reprezentující obtížnosti hry.
 * Každá obtížnost má svůj zobrazovaný název, počet drah a násobič BPM.
 */
public enum Difficulty {
    EASY("Lehká", 4, 0.25, 60),      // Velká hit zóna, pomalé noty
    MEDIUM("Střední", 4, 0.5, 50),   // Střední hit zóna
    HARD("Těžká", 4, 1.0, 40),       // Menší hit zóna, rychlé noty
    EXPERT("Expert", 5, 2.0, 30);    // Nejmenší hit zóna, 5 drah, nejrychlejší

    private final String displayName;
    private final int laneCount;
    private final double bpmMultiplier;
    private final double hitZoneHeight;

    Difficulty(String displayName, int laneCount, double bpmMultiplier, double hitZoneHeight) {
        this.displayName = displayName;
        this.laneCount = laneCount;
        this.bpmMultiplier = bpmMultiplier;
        this.hitZoneHeight = hitZoneHeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLaneCount() {
        return laneCount;
    }

    public double getBpmMultiplier() {
        return bpmMultiplier;
    }

    public double getHitZoneHeight() {
        return hitZoneHeight;
    }

    /**
     * Najde Difficulty podle zobrazovaného jména.
     * @param displayName zobrazované jméno (např. "Lehká", "Expert")
     * @return odpovídající Difficulty, nebo MEDIUM jako default
     */
    public static Difficulty fromDisplayName(String displayName) {
        for (Difficulty d : values()) {
            if (d.displayName.equalsIgnoreCase(displayName)) {
                return d;
            }
        }
        return MEDIUM; // default
    }
}
