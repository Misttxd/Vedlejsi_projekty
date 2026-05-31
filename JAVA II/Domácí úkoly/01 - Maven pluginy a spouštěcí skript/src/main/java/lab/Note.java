package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Note extends WorldEntity implements Collisionable {
    private int lane;
    private double speed;
    private boolean isActive = true;
    private static final double NOTE_WIDTH = 150;
    private static final double NOTE_HEIGHT = 40;

    public Note(Level level, int lane) {
        super(level, new Point2D(0, 0));
        this.lane = lane;
        this.speed = 200;

        double laneWidth = level.getWidth() / 4.0;
        double x = (lane * laneWidth) + (laneWidth / 2.0) - (NOTE_WIDTH / 2.0);
        this.position = new Point2D(x, -NOTE_HEIGHT);
    }

    public int getLane() {
        return lane;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
        level.remove(this);
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(position.getX(), position.getY(), NOTE_WIDTH, NOTE_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(position.getX(), position.getY(), NOTE_WIDTH, NOTE_HEIGHT);
    }

    @Override
    public void simulate(double delta) {
        position = position.add(0, speed * delta);
        if (position.getY() > level.getHeight()) {
            if(isActive) {
                level.noteMissed();
            }
            level.remove(this);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), NOTE_WIDTH, NOTE_HEIGHT);
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
