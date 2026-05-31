package lab;

import java.io.Serial;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BulletAnimated extends Bullet {

    @Serial
    private static final long serialVersionUID = -2960910444790574320L;

    private static final double SIZE = 40;
    private static final List<String> names = List.of("fireball-transparent-small.gif", "bullet_10_animated.gif",
        "bullets_balls_blue_033.gif", "bullet_17_animated.gif", "2-orange-spin-t.gif", "3-red-purple.gif",
        "bullet_15_animated.gif", "2-cracks-spin-t.gif");

    private transient Image image;
    private final String imageName;

    public BulletAnimated(World world, MyPoint position, double velocity, double angle, MyPoint acceleration) {
        super(world, position, velocity, angle, World.GRAVITY);
        imageName = ResourceManager.getRandomElement(names);
    }

    private Image getImage() {
        if (image == null) {
            image = ResourceManager.getImage(getClass(), imageName);
        }
        return image;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(getImage(), getPosition().getX(), getPosition().getY(), SIZE, SIZE);
        gc.strokeRect(position.getX(), position.getY(), SIZE, SIZE);
    }
}
