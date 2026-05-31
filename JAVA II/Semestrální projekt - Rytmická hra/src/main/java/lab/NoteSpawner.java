package lab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import lab.map.MapInfo;
import java.util.Random;

public class NoteSpawner implements DrawableSimulable {

    private final Random random = new Random();
    private final Level level;
    private final MapInfo mapInfo;
    private final MediaPlayer mediaPlayer;

    private final double beatInterval;
    private final double fallTime;
    private int nextBeat = -1;
    private double lastNoteLength = 0;
    private int beatsToSkip = 0;

    public NoteSpawner(Level level, MapInfo mapInfo, MediaPlayer mediaPlayer) {
        this.level = level;
        this.mapInfo = mapInfo;
        this.mediaPlayer = mediaPlayer;

        Difficulty difficulty = level.getDifficulty();
        double effectiveBpm = mapInfo.getBpm() * difficulty.getBpmMultiplier();
        this.beatInterval = 60.0 / effectiveBpm;

        double fallDistance = level.getHeight() - 100;
        double noteSpeed = 200;
        this.fallTime = fallDistance / noteSpeed;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void simulate(double deltaT) {
        if (mediaPlayer == null) return;
        
        double currentTime = mediaPlayer.getCurrentTime().toSeconds();
        double targetBeatTime = currentTime + fallTime;
        int targetBeat = (int) (targetBeatTime / beatInterval);

        if (nextBeat == -1) {
            nextBeat = targetBeat;
        }

        if (nextBeat <= targetBeat) {
            if (beatsToSkip > 0) {
                beatsToSkip--;
            } else {
                spawnNotesForBeat();
            }
            nextBeat++;
        }
    }

    private void spawnNotesForBeat() {
        lastNoteLength = 0;

        java.util.List<Integer> availableLanes = new java.util.ArrayList<>();
        for (int i = 0; i < level.getLanecount(); i++) {
            availableLanes.add(i);
        }

        int firstLaneIndex = random.nextInt(availableLanes.size());
        int firstLane = availableLanes.remove(firstLaneIndex);
        spawnNote(firstLane);

        if (!availableLanes.isEmpty() && random.nextDouble() < 0.08) {
            int secondLaneIndex = random.nextInt(availableLanes.size());
            int secondLane = availableLanes.remove(secondLaneIndex);
            spawnNote(secondLane);

            while (!availableLanes.isEmpty() && random.nextDouble() < 0.10) {
                int nextLaneIndex = random.nextInt(availableLanes.size());
                int nextLane = availableLanes.remove(nextLaneIndex);
                spawnNote(nextLane);
            }
        }

        if (lastNoteLength > 0) {
            beatsToSkip = (int) Math.ceil(lastNoteLength / 200.0 / beatInterval);
        }
    }

    private void spawnNote(int lane) {
        if (random.nextDouble() < 0.15) {
            level.add(new Note(level, lane, true));
            lastNoteLength = 60;
        } else {
            if (random.nextDouble() < 0.2) {
                double noteLength = random.nextDouble(150, 400);
                if (noteLength > lastNoteLength) {
                    lastNoteLength = noteLength;
                }
                level.add(new Note(level, lane, noteLength));
            } else {
                level.add(new Note(level, lane));
            }
        }
    }
}
