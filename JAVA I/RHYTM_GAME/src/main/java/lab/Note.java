package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Note extends WorldEntity implements Collisionable {
    private int lane;
    private double speed;
    private boolean isActive = true;
    private double noteWidth;
    private static final double NOTE_HEIGHT = 40;



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
    }

    public boolean isReleaseNote() {
        return inReleaseNote;
    }

    public boolean isWaitingForRelease() {
        return waitingForRelease;
    }

    public void setWaitingForRelease(boolean waiting) {
        this.waitingForRelease = waiting;
    }

    public int getLane() {
        return lane;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isLongNote() {
        return length > 0;
    }

    public void setBeingHeld(boolean held) {
        if (isActive) {
            this.isBeingHeld = held;
            if (held) {
                this.wasHit = true;
            }
        }
    }

    public boolean wasHit() {
        return wasHit;
    }

    public void markAsHit() {
        this.wasHit = true;
    }

    public double getLength() {
        return length;
    }

    public double getTailEndY() {
        return position.getY() - length;
    }

    public double getHeadY() {
        return position.getY();
    }

    public double getBottomY() {
        return position.getY() + NOTE_HEIGHT;
    }

    public boolean isAnyPartInHitZone() {
        Rectangle2D hitZone = level.getHitZone();
        return getBottomY() >= hitZone.getMinY() && getTailEndY() <= hitZone.getMaxY();
    }

    public boolean isBeingHeld() {
        return isBeingHeld;
    }

    public void deactivate() {
        this.isActive = false;
        this.isBeingHeld = false;
        level.remove(this);
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        if (length > 0) {
            Color tailColor = isBeingHeld ? Color.WHITE : laneColor.deriveColor(0, 0.5, 1.2, 0.6);
            gc.setFill(tailColor);
            gc.fillRect(position.getX() + 5, position.getY() - length, noteWidth - 10, length);
            gc.setStroke(laneColor.darker());
            gc.setLineWidth(1);
            gc.strokeRect(position.getX() + 5, position.getY() - length, noteWidth - 10, length);
        }

        Color headColor = isBeingHeld ? Color.WHITE : laneColor;
        gc.setFill(headColor);

        double arcSize = 10;
        gc.fillRoundRect(position.getX() + 2, position.getY(), noteWidth - 4, NOTE_HEIGHT, arcSize, arcSize);
        gc.setStroke(laneColor.darker());
        gc.setLineWidth(2);
        gc.strokeRoundRect(position.getX() + 2, position.getY(), noteWidth - 4, NOTE_HEIGHT, arcSize, arcSize);
        if (inReleaseNote) {
            double centerX = position.getX() + noteWidth / 2;
            double gap = 60;  // Mezera mezi notou a šipkou
            double arrowTop = position.getY() + NOTE_HEIGHT + gap;
            double arrowBottom = arrowTop + 25;  // Výška šipky
            gc.setFill(waitingForRelease ? Color.WHITE : laneColor);
            gc.fillPolygon(
                new double[] {centerX - 15, centerX + 15, centerX},
                new double[] {arrowTop, arrowTop, arrowBottom},
                3
            );
            gc.setStroke(laneColor.darker());
            gc.strokePolygon(
                new double[] {centerX - 15, centerX + 15, centerX},
                new double[] {arrowTop, arrowTop, arrowBottom},
                3
            );
        }
    }
    
    @Override
    public void simulate(double delta) {
        position = position.add(0, speed * delta);

        Rectangle2D hitZone = level.getHitZone();

        if (isLongNote()) {
            if (isBeingHeld && isAnyPartInHitZone()) {
                level.addPartialScore(delta * 10);
            }

            if (getTailEndY() > level.getHeight()) {
                if (wasHit) {
                    level.longNoteCompleted();
                } else {
                    level.noteMissed();
                }
                deactivate();
            }
        } else {
            if (position.getY() > level.getHeight() && isActive) {
                if (!wasHit) {
                    level.noteMissed();
                }
                deactivate();
            }
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        if (isLongNote()) {
            return new Rectangle2D(position.getX(), getTailEndY(), noteWidth, length + NOTE_HEIGHT);
        }
        return new Rectangle2D(position.getX(), position.getY(), noteWidth, NOTE_HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        // Not needed for this game logic
    }
}
