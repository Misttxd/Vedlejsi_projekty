package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HitEffect implements DrawableSimulable {
    private Point2D cords;
    private String quality;
    private Color color;
    private double lifeTime;
    private Level level;

    public HitEffect(Level level, Point2D cords, String quality, Color color, double lifeTime) {
        this.level = level;
        this.cords = cords;
        this.quality = quality;
        this.color = color;
        this.lifeTime = lifeTime;

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillText(quality, cords.getX(), cords.getY());
    }

    @Override
    public void simulate(double deltaT) {
        double rychlost = 20;
        cords = new Point2D(cords.getX(), cords.getY() - (rychlost * deltaT));
        lifeTime -= deltaT;
        if (lifeTime <= 0) {
            level.remove(this);
        }
    }
}
