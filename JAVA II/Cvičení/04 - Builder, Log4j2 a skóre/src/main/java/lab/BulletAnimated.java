package lab;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BulletAnimated extends Bullet {

    private static final double SIZE = 40;
    private static final List<String> names = List.of("fireball-transparent-small.gif", "bullet_10_animated.gif",
        "bullets_balls_blue_033.gif", "bullet_17_animated.gif", "2-orange-spin-t.gif", "3-red-purple.gif",
        "bullet_15_animated.gif", "2-cracks-spin-t.gif");

    private final Image image;

    public BulletAnimated(World world, Point2D position, double velocity, double angle, Point2D acceleration) {
        super(world, position, velocity, angle, World.GRAVITY);
        image = ResourceManager.getImage(getClass() , ResourceManager.getRandomElement(names));
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, getPosition().getX(), getPosition().getY(), SIZE, SIZE);
        gc.strokeRect(position.getX(), position.getY(), SIZE, SIZE);
    }
}
