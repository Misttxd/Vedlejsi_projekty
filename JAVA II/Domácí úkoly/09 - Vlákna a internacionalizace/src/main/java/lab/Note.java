package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class Note extends WorldEntity {
    @Getter
    private int lane;
    private double speed;
    private boolean isActive = true;
    private double noteWidth;
    private static final double NOTE_HEIGHT = 40; // puvodne 30 ale vypadalo to divne



    @Getter
    private double length;
    private boolean isBeingHeld = false;
    private boolean wasHit = false;
    private final Color laneColor;

    private boolean inReleaseNote;
    private boolean waitingForRelease = false;

    public Note(Level level, int lane) {
        this(level, lane, false);
    }

    public Note(Level level, int lane, double length) {
        super(level, new Point2D(0, 0));
        this.lane = lane;
        this.speed = 200;
        this.length = length;
        this.laneColor = LaneColors.getColor(lane);

        double laneWidth = level.getWidth() / level.getLanecount();
        this.noteWidth = laneWidth;
        double x = lane * laneWidth;
        this.position = new Point2D(x, -NOTE_HEIGHT);
        startNoteThread();
    }
    public Note(Level level, int lane, boolean inReleaseNote) {
        super(level, new Point2D(0, 0));
        this.lane = lane;
        this.speed = 200;
        this.laneColor = LaneColors.getColor(lane);

        double laneWidth = level.getWidth() / level.getLanecount();
        this.noteWidth = laneWidth;
        double x = lane * laneWidth;
        this.position = new Point2D(x, -NOTE_HEIGHT);
        this.inReleaseNote = inReleaseNote;
        startNoteThread();
    }

    public boolean isReleaseNote() {
        return inReleaseNote;
    }

    public boolean isLongNote() {
        return length > 0;
    }

    public synchronized boolean isActive() {
        return isActive;
    }

    public synchronized boolean isBeingHeld() {
        return isBeingHeld;
    }

    public synchronized boolean isWaitingForRelease() {
        return waitingForRelease;
    }

    public synchronized void setWaitingForRelease(boolean waitingForRelease) {
        this.waitingForRelease = waitingForRelease;
    }

    public synchronized void setBeingHeld(boolean held) {
        if (isActive) {
            this.isBeingHeld = held;
            if (held) {
                this.wasHit = true;
            }
        }
    }

    public synchronized boolean wasHit() {
        return wasHit;
    }

    public synchronized void markAsHit() {
        this.wasHit = true;
    }

    public synchronized double getTailEndY() {
        return position.getY() - length;
    }

    public synchronized double getHeadY() {
        return position.getY();
    }

    public synchronized double getBottomY() {
        return position.getY() + NOTE_HEIGHT;
    }

    public synchronized boolean isAnyPartInHitZone() {
        Rectangle2D hitZone = level.getHitZone();
        return getBottomY() >= hitZone.getMinY() && getTailEndY() <= hitZone.getMaxY();
    }

    public synchronized void deactivate() {
        this.isActive = false;
        this.isBeingHeld = false;
        level.remove(this);
    }

    @Override
    public synchronized void drawInternal(GraphicsContext gc) {
        if (length > 0) {
            Color tailColor;
            if (isBeingHeld) {
                tailColor = Color.WHITE;
            } else {
                tailColor = laneColor.deriveColor(0, 0.5, 1.2, 0.6);
            }
            gc.setFill(tailColor);
            gc.fillRect(position.getX() + 5, position.getY() - length, noteWidth - 10, length);
            gc.setStroke(laneColor.darker());
            gc.setLineWidth(1);
            gc.strokeRect(position.getX() + 5, position.getY() - length, noteWidth - 10, length);
        }

        Color headColor;
        if (isBeingHeld) {
            headColor = Color.WHITE;
        } else {
            headColor = laneColor;
        }
        gc.setFill(headColor);

        double zaobleni = 10;
        gc.fillRoundRect(position.getX() + 2, position.getY(), noteWidth - 4, NOTE_HEIGHT, zaobleni, zaobleni);
        gc.setStroke(laneColor.darker());
        gc.setLineWidth(2);
        gc.strokeRoundRect(position.getX() + 2, position.getY(), noteWidth - 4, NOTE_HEIGHT, zaobleni, zaobleni);
        if (inReleaseNote) {
            double centerX = position.getX() + noteWidth / 2;
            double gap = 40;  // Mezera mezi šipkou a notou
            double arrowHeight = 20;
            double arrowBottom = position.getY() - gap;  // Šipka NAD notou
            double arrowTop = arrowBottom - arrowHeight;
            Color arrowColor;
            if (waitingForRelease) {
                arrowColor = Color.WHITE;
            } else {
                arrowColor = laneColor;
            }
            gc.setFill(arrowColor);
            gc.fillPolygon(
                new double[] {centerX - 12, centerX + 12, centerX},
                new double[] {arrowTop, arrowTop, arrowBottom},  // Špička dolů
                3
            );
            gc.setStroke(laneColor.darker());
            gc.strokePolygon(
                new double[] {centerX - 12, centerX + 12, centerX},
                new double[] {arrowTop, arrowTop, arrowBottom},
                3
            );
        }
    }

    @Override
    public synchronized void simulate(double delta) {
        if (isLongNote()) {
            if (isBeingHeld && isAnyPartInHitZone()) {
                level.addPartialScore(delta * 10);
            }

            if (getTailEndY() > level.getHeight()) { //tady muze byt problem ze to nepusobi vycentrovane
                if (wasHit) {
                    level.longNoteCompleted(); //momentalne nedela nic 🙀
                } else {
                    level.noteMissed();
                }
                deactivate();
            }
        } else {
            // Pro release noty musime pockat az projede cela (vcetne sipky nahore)
            double disappearY;
            if (isReleaseNote()) {
                disappearY = position.getY() - 60;
            } else {
                disappearY = position.getY();
            }
            if (disappearY > level.getHeight() && isActive) {
                if (!wasHit) {
                    level.noteMissed();
                }
                deactivate();
            }
        }
    }

    private void startNoteThread() {
        Thread thread = new Thread(new NoteMover(), "NoteMover-" + lane);
        thread.setDaemon(true);
        thread.start();
    }

    private class NoteMover implements Runnable {
        @Override
        public void run() {
            long lastTime = System.nanoTime();
            while (isActive()) {
                if (level.isPaused()) {
                    lastTime = System.nanoTime();
                    sleep();
                    continue;
                }

                long now = System.nanoTime();
                double delta = (now - lastTime) / 1_000_000_000D;
                lastTime = now;

                synchronized (Note.this) {
                    position = position.add(0, speed * delta);
                }

                if (!sleep()) {
                    break;
                }
            }
        }

        private boolean sleep() {
            try {
                Thread.sleep(16);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }
    }
}
