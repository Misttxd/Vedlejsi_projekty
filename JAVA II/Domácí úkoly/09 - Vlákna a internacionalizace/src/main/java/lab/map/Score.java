package lab.map;

import lombok.Getter;

// jednoduchy record by byl lepsi ale tohle funguje taky
@Getter
public class Score {
    private final int points;
    private final String playerName;

    public Score(int points, String playerName) {
        this.points = points;
        this.playerName = playerName;
    }

    @Override
    public String toString(){
        if (points == 0) {
            return "---";
        }
        // Formát pro zobrazení uživateli (přidá oddělovače tisíců)
        return String.format("%,d (%s)", points, playerName);
    }
}
