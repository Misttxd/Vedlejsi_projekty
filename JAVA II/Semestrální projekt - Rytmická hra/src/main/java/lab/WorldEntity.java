package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public abstract class WorldEntity implements DrawableSimulable {

    protected final Level level;
    @Getter
    protected Point2D position;

    protected WorldEntity(Level level, Point2D position) {
        this.level = level;
        this.position = position;
    }

    @Override
    public final void draw(GraphicsContext gc) {
        gc.save();
        drawInternal(gc);
        gc.restore();
    }

    public abstract void drawInternal(GraphicsContext gc);

}
