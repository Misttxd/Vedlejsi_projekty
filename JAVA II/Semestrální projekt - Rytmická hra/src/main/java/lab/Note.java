package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Note extends WorldEntity {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private static final double NOTE_HEIGHT = 40;
    private static final double HEAD_PADDING = 2;
    private static final double TAIL_PADDING = 5;
    private static final double ROUNDING = 10;
    private static final double ARROW_WIDTH = 24;
    private static final double ARROW_HEIGHT = 20;
    private static final double ARROW_GAP = 40;

    @Getter
    private int lane;
    private double speed;
    @Getter
    private boolean isActive = true;
    private double noteWidth;
    private double noteX;
    private double headX;
    private double headWidth;
    private double centerX;
    private Color tailColor;
    private Color borderColor;

    @Getter
    private double length;
    @Getter
    private boolean isBeingHeld = false;
    private boolean wasHit = false;
    private final Color laneColor;

    private boolean inReleaseNote;
    @Setter
    @Getter
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
        prepareGraphics();
    }

    public Note(Level level, int lane, boolean inReleaseNote) {
        super(level, new Point2D(0, 0));
        this.lane = lane;
        this.speed = 200;
        this.laneColor = LaneColors.getColor(lane);
        this.inReleaseNote = inReleaseNote;
        prepareGraphics();
    }

    private void prepareGraphics() {
        noteWidth = level.getWidth() / level.getLanecount();
        noteX = lane * noteWidth;
        headX = noteX + HEAD_PADDING;
        headWidth = noteWidth - (HEAD_PADDING * 2);
        centerX = noteX + noteWidth / 2;
        tailColor = laneColor.deriveColor(0, 0.5, 1.2, 0.6);
        borderColor = laneColor.darker();
        position = new Point2D(noteX, -NOTE_HEIGHT);
    }

    public boolean isReleaseNote() {
        return inReleaseNote;
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

    public void deactivate() {
        this.isActive = false;
        this.isBeingHeld = false;
        level.remove(this);
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        double y = position.getY();
        if (!isVisibleOnCanvas(y)) {
            return;
        }

        if (length > 0) {
            Color currentTailColor;
            if (isBeingHeld) {
                currentTailColor = Color.WHITE;
            } else {
                currentTailColor = tailColor;
            }
            gc.setFill(currentTailColor);
            gc.fillRect(noteX + TAIL_PADDING, y - length, noteWidth - (TAIL_PADDING * 2), length);
            gc.setStroke(borderColor);
            gc.setLineWidth(1);
            gc.strokeRect(noteX + TAIL_PADDING, y - length, noteWidth - (TAIL_PADDING * 2), length);
        }

        Color headColor;
        if (isBeingHeld) {
            headColor = Color.WHITE;
        } else {
            headColor = laneColor;
        }
        gc.drawImage(getNoteImage(headColor), headX, y);

        if (inReleaseNote) {
            Color arrowColor;
            if (waitingForRelease) {
                arrowColor = Color.WHITE;
            } else {
                arrowColor = laneColor;
            }

            double arrowX = centerX - ARROW_WIDTH / 2;
            double arrowY = y - ARROW_GAP - ARROW_HEIGHT;
            gc.drawImage(getArrowImage(arrowColor), arrowX, arrowY);
        }
    }

    private boolean isVisibleOnCanvas(double y) {
        double topY = y;
        if (isLongNote()) {
            topY = y - length;
        }
        if (isReleaseNote()) {
            topY = Math.min(topY, y - ARROW_GAP - ARROW_HEIGHT);
        }

        double bottomY = y + NOTE_HEIGHT;
        return bottomY >= 0 && topY <= level.getHeight();
    }

    private Image getNoteImage(Color fillColor) {
        String key = "note-" + (int) headWidth + "-" + fillColor + "-" + borderColor;
        Image image = IMAGE_CACHE.get(key);
        if (image == null) {
            Canvas canvas = new Canvas(headWidth, NOTE_HEIGHT);
            GraphicsContext imageGc = canvas.getGraphicsContext2D();
            imageGc.setFill(fillColor);
            imageGc.fillRoundRect(0, 0, headWidth, NOTE_HEIGHT, ROUNDING, ROUNDING);
            imageGc.setStroke(borderColor);
            imageGc.setLineWidth(2);
            imageGc.strokeRoundRect(1, 1, headWidth - 2, NOTE_HEIGHT - 2, ROUNDING, ROUNDING);
            image = createImage(canvas);
            IMAGE_CACHE.put(key, image);
        }
        return image;
    }

    private Image getArrowImage(Color fillColor) {
        String key = "arrow-" + fillColor + "-" + borderColor;
        Image image = IMAGE_CACHE.get(key);
        if (image == null) {
            Canvas canvas = new Canvas(ARROW_WIDTH, ARROW_HEIGHT);
            GraphicsContext imageGc = canvas.getGraphicsContext2D();
            imageGc.setFill(fillColor);
            imageGc.fillPolygon(
                new double[] {0, ARROW_WIDTH, ARROW_WIDTH / 2},
                new double[] {0, 0, ARROW_HEIGHT},
                3
            );
            imageGc.setStroke(borderColor);
            imageGc.strokePolygon(
                new double[] {0, ARROW_WIDTH, ARROW_WIDTH / 2},
                new double[] {0, 0, ARROW_HEIGHT},
                3
            );
            image = createImage(canvas);
            IMAGE_CACHE.put(key, image);
        }
        return image;
    }

    private Image createImage(Canvas canvas) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return canvas.snapshot(parameters, null);
    }

    @Override
    public void simulate(double delta) {
        position = position.add(0, speed * delta);

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
}
