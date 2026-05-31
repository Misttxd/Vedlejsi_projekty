package lab;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class UfoSpawner implements DrawableSimulable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Random RANDOM = new Random();

    private final transient World  world;

    private long nextSpawn = 0;

    public UfoSpawner(World world) {
        this.world = world;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void simulate(double delta) {
        if(nextSpawn == 0){
            nextSpawn = System.currentTimeMillis() + RANDOM.nextLong(500, 3000);
        }
        if(System.currentTimeMillis() > nextSpawn){
            world.add(new Ufo(world));
            nextSpawn = System.currentTimeMillis() + RANDOM.nextLong(500, 3000);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(0, 0, 0, 0);
    }

}
