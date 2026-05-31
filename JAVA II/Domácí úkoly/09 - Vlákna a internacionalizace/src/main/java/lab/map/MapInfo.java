package lab.map;

import lab.Difficulty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MapInfo {
    private final long id;
    private final String mapName;
    private final String difficulty;
    private final String filePath;
    private final String musicPath;
    private final int bpm;
    private final int durationSeconds;

    @Setter
    private Score highScore;

    public MapInfo(long id, String mapName, String difficulty, String filePath, String musicPath, int bpm, int durationSeconds) {
        this.id = id;
        this.mapName = mapName;
        this.difficulty = difficulty;
        this.filePath = filePath;
        this.musicPath = musicPath;
        this.bpm = bpm;
        this.durationSeconds = durationSeconds;
        this.highScore = new Score(0, "---"); // default prazdne
    }

    public int getEstimatedNoteCount() {
        Difficulty diff = Difficulty.fromDisplayName(difficulty);
        double calcBpm = bpm * diff.getBpmMultiplier();
        return (int) (durationSeconds * (calcBpm / 60.0));
    }

    public String getInfo() {
        return String.format("%d BPM, %d:%02d", bpm, durationSeconds / 60, durationSeconds % 60);
    }

}
