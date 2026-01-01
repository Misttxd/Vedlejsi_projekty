package lab.map;

public class MapInfo {
    private final long id;
    private final String mapName;
    private final String difficulty;
    private final String filePath;
    private final String musicPath;
    private final int bpm;
    private final int durationSeconds;

    private Score highScore;

    public MapInfo(long id, String mapName, String difficulty, String filePath, String musicPath, int bpm, int durationSeconds) {
        this.id = id;
        this.mapName = mapName;
        this.difficulty = difficulty;
        this.filePath = filePath;
        this.musicPath = musicPath;
        this.bpm = bpm;
        this.durationSeconds = durationSeconds;
        this.highScore = new Score(0, "---"); // Výchozí prázdné skóre
    }

    public int getEstimatedNoteCount() {
        double calcBpm = bpm;
        if (difficulty.equals("Lehká")) calcBpm /= 4.0;
        else if (difficulty.equals("Střední")) calcBpm /= 2.0;
        else if (difficulty.equals("Expert")) calcBpm *= 2.0;
        
        return (int) (durationSeconds * (calcBpm / 60.0));
    }

    public String getInfo() {
        return String.format("%d BPM, %d:%02d", bpm, durationSeconds / 60, durationSeconds % 60);
    }

    public long getId() {
        return id;
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

    public String getMusicPath() {
        return musicPath;
    }

    public int getBpm() {
        return bpm;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Score getHighScore() {
        return highScore;
    }

    public void setHighScore(Score highScore) {
        this.highScore = highScore;
    }
}
