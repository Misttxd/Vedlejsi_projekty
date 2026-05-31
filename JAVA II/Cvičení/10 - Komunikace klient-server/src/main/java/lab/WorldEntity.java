package lab;

import java.io.Serial;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public abstract class WorldEntity implements DrawableSimulable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected transient final World world;
    @Getter
    protected MyPoint position;

    public WorldEntity(World world, MyPoint position) {
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

}
