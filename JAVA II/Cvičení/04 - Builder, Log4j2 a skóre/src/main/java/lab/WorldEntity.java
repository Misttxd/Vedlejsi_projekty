package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class WorldEntity implements DrawableSimulable {

    protected final World world;
    protected Point2D position;

    public WorldEntity(World world, Point2D position) {
        this.world = world;
        this.position = position;
    }

    @Override
    public final void draw(GraphicsContext gc) {
        gc.save();
        drawInternal(gc);
        gc.restore();
    }

    public abstract void drawInternal(GraphicsContext gc);

    public Point2D getPosition() {
        return position;
    }
}
