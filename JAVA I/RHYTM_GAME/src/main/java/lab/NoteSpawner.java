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
            lastNoteLength = 0;
            
            // Seznam volných drah
            java.util.List<Integer> availableLanes = new java.util.ArrayList<>();
            for (int i = 0; i < level.getLanecount(); i++) {
                availableLanes.add(i);
            }
            
            // Vždy spawneme alespoň jednu notu
            int firstLaneIndex = RANDOM.nextInt(availableLanes.size());
            int firstLane = availableLanes.remove(firstLaneIndex);
            spawnNote(firstLane);
            
            // 8% šance na druhou notu
            if (!availableLanes.isEmpty() && RANDOM.nextDouble() < 0.08) {
                int secondLaneIndex = RANDOM.nextInt(availableLanes.size());
                int secondLane = availableLanes.remove(secondLaneIndex);
                spawnNote(secondLane);
                
                // 10% šance na třetí a každou další
                while (!availableLanes.isEmpty() && RANDOM.nextDouble() < 0.10) {
                    int nextLaneIndex = RANDOM.nextInt(availableLanes.size());
                    int nextLane = availableLanes.remove(nextLaneIndex);
                    spawnNote(nextLane);
                }
            }
            
            scheduleNextSpawn();
        }
    }
    
    private void spawnNote(int lane) {
        // 20% šance na dlouhou notu
        if (RANDOM.nextDouble() < 0.15) {
            level.add(new Note(level, lane, true)); // true = release note
        } else {
            // normální nota
            if (RANDOM.nextDouble() < 0.2) {
                double noteLength = RANDOM.nextDouble(150, 400);
                if (noteLength > lastNoteLength) {
                    lastNoteLength = noteLength;
                }
                level.add(new Note(level, lane, noteLength));
            } else {
                level.add(new Note(level, lane));
            }
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

