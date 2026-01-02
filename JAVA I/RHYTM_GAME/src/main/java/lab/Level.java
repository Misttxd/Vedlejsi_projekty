package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import lab.map.MapInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import java.util.Comparator;
import java.util.Optional;

public class Level {

    public final Dimension2D dimension;
    private final List<DrawableSimulable> entities = new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();

    private static final double HIT_ZONE_Y_OFFSET = 100;
    private final double hitZoneHeight;
    private final Rectangle2D hitZone;

    private int score = 0;
    private double scoreAccumulator = 0;
    private int combo = 0;
    private double overdrive = 0;
    private boolean overdriveActive = false;

    private final List<GameEventListener> listeners = new ArrayList<>();
    private final Comparator<DrawableSimulable> noteSorter;
    private final NoteSpawner noteSpawner;
    private boolean isGameFinished = false;

    private int qualityMultiplier = 0;

    private int perfectCount = 0;
    private int goodCount = 0;
    private int missCount = 0;
    private int maxCombo = 0;

    private int lanecount;
    private final MediaPlayer mediaPlayer;
    private final Difficulty difficulty;

    private int[] lanePressed;


    public Level(double width, double height, Difficulty difficulty, MapInfo map, MediaPlayer mediaPlayer) {
        this.dimension = new Dimension2D(width, height);
        this.difficulty = difficulty;
        this.lanecount = difficulty.getLaneCount();
        this.hitZoneHeight = difficulty.getHitZoneHeight();
        this.hitZone = new Rectangle2D(0, height - HIT_ZONE_Y_OFFSET, width, hitZoneHeight);
        this.mediaPlayer = mediaPlayer;
        this.lanePressed = new int[lanecount];

        this.noteSpawner = new NoteSpawner(this, map, mediaPlayer);
        entities.add(noteSpawner);

        noteSorter = (o1,o2) -> {
            if (o1 instanceof WorldEntity e1 && o2 instanceof WorldEntity e2) {
                return Double.compare(e1.getPosition().getY(), e2.getPosition().getY());
            }
            return 0;
        };


    }

    public void addPartialScore(double amount) {
        int comboMultiplier = getComboMultiplier();
        int overdriveFactor = getOverdriveFactor();

        scoreAccumulator += amount * comboMultiplier * overdriveFactor;
        if (scoreAccumulator >= 1) {
            score += (int) scoreAccumulator;
            scoreAccumulator -= (int) scoreAccumulator;
            fireScoreChanged();
        }
    }



    public Rectangle2D getHitZone() {
        return hitZone;
    }

    public void checkRelease(int lane) {
        lanePressed[lane] = 0;

        Optional<Note> releaseNote = entities.stream()
            .filter(e -> e instanceof Note)
            .map(e -> (Note) e)
            .filter(n -> n.getLane() == lane && n.isWaitingForRelease())
            .findFirst();

        if (releaseNote.isPresent()) {
            Note note = releaseNote.get();
            double arrowY = note.getHeadY() - 40 - 10; // Šipka NAD notou: gap (40) + střed šipky (10)
            double toleranceUp = 30; // Rezerva směrem nahoru
            if (arrowY >= hitZone.getMinY() - toleranceUp && arrowY <= hitZone.getMaxY()) {
                add(new HitEffect(this, note.getPosition(), "RELEASE!", Color.MAGENTA, 0.5));

                int comboMultiplier = getComboMultiplier();
                int overdriveFactor = getOverdriveFactor();
                score += 2 * comboMultiplier * overdriveFactor;
                fireScoreChanged();

                combo++;
                if (combo > maxCombo) maxCombo = combo;
                fireComboChanged(combo);

                perfectCount++;
            } else {
                add(new HitEffect(this, note.getPosition(), "MISS", Color.RED, 0.5));
                missCount++;
                combo = 0;
                fireComboChanged(combo);
            }
            note.deactivate();
            return;
        }

        Optional<Note> heldNote = entities.stream()
            .filter(e -> e instanceof Note)
            .map(e -> (Note) e)
            .filter(n -> n.getLane() == lane && n.isBeingHeld())
            .findFirst();

        if (heldNote.isPresent()) {
            Note note = heldNote.get();
            note.setBeingHeld(false);
        }
    }

    public int getLanecount() {
        return lanecount;
    }

    public Difficulty getDifficulty() {
        return difficulty;
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

        double laneWidth = dimension.getWidth() / lanecount;

        for (int i = 0; i < lanecount; i++) {
            double x = i * laneWidth;
            Color baseColor = LaneColors.getColor(i);

            if (lanePressed[i] == 1) {
                // Úspěšný zásah - jasná barva dráhy
                gc.setFill(baseColor.deriveColor(0, 1, 1.5, 0.7));
            } else if (lanePressed[i] == 2) {
                // Chybný stisk - červená
                gc.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
            } else {
                // Normální stav - tlumená barva dráhy
                gc.setFill(baseColor.deriveColor(0, 0.3, 0.5, 0.3));
            }
            gc.fillRect(x, hitZone.getMinY(), laneWidth, hitZone.getHeight());

            // Okraj hit zóny
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeRect(x, hitZone.getMinY(), laneWidth, hitZone.getHeight());
        }

        for (int i = 0; i <= lanecount; i++) {
            double x = i * laneWidth;
            gc.strokeLine(x, 0, x, dimension.getHeight());
        }

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

        updateLaneColors();

        if (!isGameFinished && mediaPlayer != null && mediaPlayer.getCurrentTime().toSeconds() >= mediaPlayer.getTotalDuration().toSeconds() - 0.5) {
            isGameFinished = true;
            fireGameFinished();
        }

        if (overdriveActive == true) {
            overdrive = overdrive - (0.1 * delay);
            fireOverdriveChanged();
            if(overdrive <= 0) {
                overdriveActive = false;
                overdrive = 0;
                fireOverdriveChanged();
                fireComboChanged(combo);  // Okamžitě aktualizovat UI - vrátit normální barvu a multiplier
            }
        }
    }

    private void updateLaneColors() {
        for (int i = 0; i < lanecount; i++) {
            final int lane = i;
            boolean hasHeldNote = entities.stream()
                .filter(e -> e instanceof Note)
                .map(e -> (Note) e)
                .anyMatch(n -> n.getLane() == lane && n.isBeingHeld());

            if (hasHeldNote) {
                lanePressed[lane] = 1;
            } else if (lanePressed[lane] == 1) {
                lanePressed[lane] = 0;
            }
        }
    }

    public void checkHit(int lane) {
        boolean alreadyHolding = entities.stream()
            .filter(e -> e instanceof Note)
            .map(e -> (Note) e)
            .anyMatch(n -> n.getLane() == lane && n.isBeingHeld());

        if (alreadyHolding) {
            return;
        }
        Optional<Note> hitNote = entities.stream()
            .filter(e -> e instanceof Note)
            .map(e -> (Note) e)
            .filter(n -> n.getLane() == lane && n.isActive() && !n.isBeingHeld() && !n.wasHit() && n.isAnyPartInHitZone())
            .findFirst();

        if (hitNote.isPresent() && hitNote.get().isAnyPartInHitZone()) {
            lanePressed[lane] = 1;
            Note note = hitNote.get();

            double headOffset = Math.abs(note.getHeadY() - hitZone.getMinY());
            boolean isPerfectTiming = headOffset <= 20;

            if (note.isReleaseNote()) {
                // Release nota - jen označ a čekej na puštění
                note.setWaitingForRelease(true);
                note.markAsHit();
                add(new HitEffect(this, note.getPosition(), "HOLD", Color.YELLOW, 0.3));
                return;  // Žádné body - ty přijdou při puštění
            }

            if (isPerfectTiming) {
                qualityMultiplier = 2;
                perfectCount++;
                add(new HitEffect(this, note.getPosition(), "PERFECT", Color.GREEN, 0.5));
            } else {
                qualityMultiplier = 1;
                goodCount++;
                add(new HitEffect(this, note.getPosition(), "GOOD", Color.BLUE, 0.5));
            }

            if (note.isLongNote()) {
                note.setBeingHeld(true);
            } else {
                note.markAsHit();
                note.deactivate();
            }

            int comboMultiplier = getComboMultiplier();
            int overdriveFactor = getOverdriveFactor();
            score += comboMultiplier * overdriveFactor * qualityMultiplier;
            fireScoreChanged();

            combo++;
            if (combo > maxCombo) {
                maxCombo = combo;
            }
            if (!overdriveActive) {
                overdrive = Math.min(1.0, overdrive + 0.05);
            }

            fireOverdriveChanged();
            fireComboChanged(combo);
        } else {
            lanePressed[lane] = 2;
            combo = 0;
            fireComboChanged(combo);
        }
    }

    public void noteMissed() {
        missCount++;
        combo = 0;
        fireComboChanged(combo);
    }

    public void longNoteCompleted() {
        int comboMultiplier = getComboMultiplier();
        int overdriveFactor = getOverdriveFactor();
        score += 5 * comboMultiplier * overdriveFactor;

        if (!overdriveActive) {
            overdrive = Math.min(1.0, overdrive + 0.03);
        }

        fireScoreChanged();
        fireOverdriveChanged();
    }

    private void fireScoreChanged() {
        for (GameEventListener listener : listeners) {
            listener.onScoreChanged(score);
        }
    }

    private void fireOverdriveChanged() {
        for (GameEventListener listener : listeners) {
            listener.onOverdriveChanged(overdrive, overdriveActive);
        }
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    private void fireGameFinished() {
        GameStats stats = new GameStats(score, perfectCount, goodCount, missCount, maxCombo);
        for (GameEventListener listener : listeners) {
            listener.onGameFinished(stats);
        }
    }

    private void fireComboChanged(int newCombo) {
        int comboMultiplier = getComboMultiplier();

        for (GameEventListener listener : listeners) {
            listener.onComboChanged(newCombo, comboMultiplier, overdriveActive);
        }
    }

    public void activateOverdrive() {
        if (overdrive > 0.5) {
            overdriveActive = true;
            fireOverdriveChanged();
            fireComboChanged(combo);
        }
    }

    private int getComboMultiplier() {
        if (combo <= 9) {
            return 1;
        } else if (combo <= 19) {
            return 2;
        } else if (combo <= 29) {
            return 3;
        } else {
            return 4;
        }
    }

    private int getOverdriveFactor() {
        if (overdriveActive) {
            return 2;
        } else {
            return 1;
        }
    }
}
