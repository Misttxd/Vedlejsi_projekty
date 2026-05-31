package lab;

import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class UfoSpawner implements DrawableSimulable {

    private static final Random RANDOM = new Random();

    private World  world;
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
