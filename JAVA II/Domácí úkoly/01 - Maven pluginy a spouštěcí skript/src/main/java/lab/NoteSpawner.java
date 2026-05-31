package lab;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class NoteSpawner implements DrawableSimulable {

    private static final Random RANDOM = new Random();
    private final Level level;
    private final int totalNotesToSpawn;
    private int notesSpawned = 0;
    private long nextNoteTime;

    public NoteSpawner(Level level, int totalNotesToSpawn) {
        this.level = level;
        this.totalNotesToSpawn = totalNotesToSpawn;
        scheduleNextSpawn();
    }

    @Override
    public void draw(GraphicsContext gc) {
        // This is an invisible entity
    }

    @Override
    public void simulate(double deltaT) {
        long now = System.currentTimeMillis();
        if (nextNoteTime < now && !isFinished()) {
            int randomLane = RANDOM.nextInt(4); // 0, 1, 2, or 3
            level.add(new Note(level, randomLane));
            notesSpawned++;
            scheduleNextSpawn();
        }
    }

    public boolean isFinished() {
        return notesSpawned >= totalNotesToSpawn;
    }

    private void scheduleNextSpawn() {
        // Zabráníme plánování další noty, pokud jsme již všechny vygenerovali
        if (!isFinished()) {
            nextNoteTime = System.currentTimeMillis() + RANDOM.nextInt(500, 2000);
        }
    }
}

