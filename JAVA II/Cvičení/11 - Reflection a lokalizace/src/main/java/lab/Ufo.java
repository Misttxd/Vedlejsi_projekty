package lab;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Ufo extends WorldEntity implements Collisionable {

    @Serial
    private static final long serialVersionUID = 8872680181364400397L;

    private static final Random RANDOM = new Random();

    private static final List<String> names = List.of("Green_UFO.gif", "head-on-ufo.gif", "Purple_UFO.gif",
        "rotating_ufo.gif", "ufo2.gif", "Ufo6.gif", "Yellow_UFO.gif", "ufo-small.gif");

    private final String imageName;
    private transient Image image;
    private MyPoint velocity;


    public Ufo(World world) {
        this(world, new MyPoint(RANDOM.nextDouble(world.getWidth()),
            RANDOM.nextDouble(0, world.getHeight() * Setting.getInstance().getUfoMinPercentageHeight())), new MyPoint(
            RANDOM.nextDouble(Setting.getInstance().getUfoMinSpeed(), Setting.getInstance().getUfoMaxSpeed()), 0));
    }

    public Ufo(World world, MyPoint position, MyPoint velocity) {
        super(world, position);
        this.velocity = velocity;
        imageName = names.get(RANDOM.nextInt(names.size()));
    }

    private Image getImage() {
        if (image == null) {
            image = ResourceManager.getImage(getClass(), imageName);
        }
        return image;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(getImage(), getPosition().getX(), getPosition().getY());
    }

    public void changeDirection() {
        velocity = velocity.multiply(-1);
    }

    @Override
    public void simulate(double deltaTime) {
        position = position.add(velocity.multiply(deltaTime));
        position = new MyPoint(position.getX() % world.getWidth(), position.getY());
        if (position.getX() < -getImage().getWidth()) {
            position = new MyPoint(world.getWidth(), position.getY());
        }
        log.trace("Ufo position: {}", position);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), getImage().getWidth(), getImage().getHeight());
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        log.trace("Ufo hitted by {}.", another);
        if (another instanceof Bullet) {
            world.remove(this);
            world.getDestroyInfos().add(new DestroyInfo(LocalDateTime.now(), position));
        }
    }

    public double getWidth() {
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }

    public void setPositionOfMiddle(MyPoint position) {
        this.position = position.subtract(getWidth() / 2, getHeight() / 2);
    }

    record DestroyInfo(LocalDateTime time, MyPoint position) {
    }


}
