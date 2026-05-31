package lab;

import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ufo extends WorldEntity implements Collisionable {

    private static final Random RANDOM = new Random();

    private static final List<String> names = List.of("Green_UFO.gif", "head-on-ufo.gif", "Purple_UFO.gif",
        "rotating_ufo.gif", "ufo2.gif", "Ufo6.gif", "Yellow_UFO.gif", "ufo-small.gif");

    private final Image image;
    private Point2D velocity;


    public Ufo(World world) {
        this(world, new Point2D(RANDOM.nextDouble(world.getWidth()), RANDOM.nextDouble(0, world.getHeight() * 0.3)),
            new Point2D(RANDOM.nextDouble(70, 150), 0));
    }

    public Ufo(World world, Point2D position, Point2D velocity) {
        super(world, position);
        this.velocity = velocity;
        image = ResourceManager.getImage(getClass(), ResourceManager.getRandomElement(names));
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, getPosition().getX(), getPosition().getY());
    }

    public void changeDirection() {
        velocity = velocity.multiply(-1);
    }

    @Override
    public void simulate(double deltaTime) {
        position = position.add(velocity.multiply(deltaTime));
        position = new Point2D(position.getX() % world.getWidth(), position.getY());
        if (position.getX() < -image.getWidth()) {
            position = new Point2D(world.getWidth(), position.getY());
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Bullet) {
            world.remove(this);
        }
    }

    public double getWidth() {
        return image.getWidth();
    }

    public double getHeight() {
        return image.getHeight();
    }

    public void setPositionOfMiddle(Point2D position) {
        this.position = position.subtract(getWidth() / 2, getHeight() / 2);
    }

}
