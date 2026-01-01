package lab.map;

public class Score {
    private final int points;
    private final String playerName;

    public Score(int points, String playerName) {
        this.points = points;
        this.playerName = playerName;
    }

    public int getPoints() {
        return points;
    }

    public String getPlayerName() {
        return playerName;
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
