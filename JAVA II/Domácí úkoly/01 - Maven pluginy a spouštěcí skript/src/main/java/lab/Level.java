package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import java.util.Comparator;

public class Level {

    public final Dimension2D dimension;
    private final List<DrawableSimulable> entities = new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();

    private static final double HIT_ZONE_HEIGHT = 50;
    private static final double HIT_ZONE_Y_OFFSET = 100;
    private final Rectangle2D hitZone;

    private int score = 0;
    private int combo = 0;
    private double overdrive = 0;

    private final List<GameEventListener> listeners = new ArrayList<>();
    private final Comparator<DrawableSimulable> noteSorter;
    private final NoteSpawner noteSpawner;
    private boolean isGameFinished = false;

    public Level(double width, double height, int noteCount) {
        this.dimension = new Dimension2D(width, height);
        this.hitZone = new Rectangle2D(0, height - HIT_ZONE_Y_OFFSET, width, HIT_ZONE_HEIGHT);
        
        this.noteSpawner = new NoteSpawner(this, noteCount);
        entities.add(noteSpawner);

        noteSorter = (o1,o2) -> {
            if (o1 instanceof WorldEntity e1 && o2 instanceof WorldEntity e2) {
                return Double.compare(e1.getPosition().getY(), e2.getPosition().getY());
            }
            return 0;
        };
    }

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void add(DrawableSimulable entity) {
        entitiesToAdd.add(entity);
    }

    public void remove(DrawableSimulable entity) {
        entitiesToRemove.add(entity);
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, dimension.getWidth(), dimension.getHeight());

        double laneWidth = dimension.getWidth() / 4.0;
        for (int i = 0; i <= 4; i++) {
            double x = i * laneWidth;
            gc.strokeLine(x, 0, x, dimension.getHeight());
        }

        gc.setFill(Color.gray(0.5, 0.5));
        gc.fillRect(hitZone.getMinX(), hitZone.getMinY(), hitZone.getWidth(), hitZone.getHeight());

        for (DrawableSimulable entity : entities) {
            entity.draw(gc);
        }
    }

    public void simulate(double delay) {
        for (DrawableSimulable entity : entities) {
            entity.simulate(delay);
        }
        entities.addAll(entitiesToAdd);
        entities.removeAll(entitiesToRemove);
        entitiesToAdd.clear();
        entitiesToRemove.clear();

        entities.sort(noteSorter);

        // Detekce konce hry
        if (!isGameFinished && noteSpawner.isFinished() && !hasActiveNotes()) {
            isGameFinished = true;
            fireGameFinished();
        }
    }

    public void checkHit(int lane) {
        boolean hit = false;
        for (DrawableSimulable entity : entities) {
            if (entity instanceof Note note) {
                if (note.getLane() == lane && note.isActive() && note.getBoundingBox().intersects(hitZone)) {
                    note.deactivate();
                    score++;
                    combo++;
                    overdrive += 0.05;
                    if (overdrive > 1.0) overdrive = 1.0;

                    fireScoreChanged();
                    fireOverdriveChanged();
                    hit = true;
                    break;
                }
            }
        }
        if (!hit) {
            combo = 0;
        }
    }

    public void noteMissed() {
        combo = 0;
    }

    private void fireScoreChanged() {
        for (GameEventListener listener : listeners) {
            listener.onScoreChanged(score);
        }
    }

    private void fireOverdriveChanged() {
        for (GameEventListener listener : listeners) {
            listener.onOverdriveChanged(overdrive);
        }
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    private boolean hasActiveNotes() {
        return entities.stream().anyMatch(e -> e instanceof Note);
    }

    private void fireGameFinished() {
        for (GameEventListener listener : listeners) {
            listener.onGameFinished(this.score);
        }
    }
}
