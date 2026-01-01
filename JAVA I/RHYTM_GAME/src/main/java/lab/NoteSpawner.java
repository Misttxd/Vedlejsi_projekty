package lab;

import javafx.scene.canvas.GraphicsContext;
import lab.map.MapInfo;

import java.util.Random;

public class NoteSpawner implements DrawableSimulable {

    private static final Random RANDOM = new Random();
    private final Level level;
    private long nextNoteTime;
    private final MapInfo mapInfo;
    private double lastNoteLength = 0;

    public NoteSpawner(Level level, MapInfo mapInfo) {
        this.level = level;
        this.mapInfo = mapInfo;
        scheduleNextSpawn();
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void simulate(double deltaT) {
        long now = System.currentTimeMillis();
        if (nextNoteTime < now) {
            int randomLane = RANDOM.nextInt(level.getLanecount());
            if (RANDOM.nextDouble() < 0.2) {
                lastNoteLength = RANDOM.nextDouble(150, 400);
                level.add(new Note(level, randomLane, lastNoteLength));
            } else {
                lastNoteLength = 0;
                level.add(new Note(level, randomLane));
            }
            scheduleNextSpawn();
        }
    }

    private void scheduleNextSpawn() {
        Difficulty difficulty = level.getDifficulty();
        double currentBpm = mapInfo.getBpm();

        // Použijeme bpmMultiplier z enumu Difficulty
        currentBpm *= difficulty.getBpmMultiplier();

        double intervalInSeconds = 60.0 / currentBpm;
        long intervalInMillis = (long) (intervalInSeconds * 1000);
        
        long lengthDelay = (long) ((lastNoteLength / 200.0) * 1000);
        long safetyGap = 200; 

        nextNoteTime = System.currentTimeMillis() + intervalInMillis + lengthDelay + safetyGap;
    }
}

