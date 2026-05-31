package lab.map;

public class MapInfo {
    private final String mapName;
    private final String difficulty;
    private final String filePath;
    private final int noteCount; // Počet not v mapě

    private Score highScore;

    public MapInfo(String mapName, String difficulty, String filePath, int noteCount) {
        this.mapName = mapName;
        this.difficulty = difficulty;
        this.filePath = filePath;
        this.noteCount = noteCount;
        this.highScore = new Score(0, "---"); // Výchozí prázdné skóre
    }

    public String getMapName() {
        return mapName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public Score getHighScore() {
        return highScore;
    }

    public void setHighScore(Score highScore) {
        this.highScore = highScore;
    }
}
