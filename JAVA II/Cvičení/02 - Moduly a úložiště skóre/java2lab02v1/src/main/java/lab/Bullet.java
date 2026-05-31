package lab;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends WorldEntity implements Collisionable {
    private static final double SIZE = 20;

    protected final Point2D acceleration;
    protected Point2D velocity;
    private final List<HitListener> hitListeners = new ArrayList<>();

    public Bullet(World world, Point2D position, double velocity, double angle, Point2D acceleration) {
        super(world, position);
        setVelocity(velocity, angle);
        this.acceleration = acceleration;
    }

    public void setVelocity(double velocity, double angle) {
        this.velocity = new Point2D(Math.cos(Math.toRadians(angle)) * velocity,
            Math.sin(Math.toRadians(angle)) * velocity);
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.setFill(Color.SILVER);
        gc.fillOval(position.getX(), position.getY(), SIZE, SIZE);
    }

    @Override
    public void simulate(double deltaTime) {
        position = position.add(velocity.multiply(deltaTime));
        velocity = velocity.add(acceleration.multiply(deltaTime));
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), SIZE, SIZE);
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Ufo) {
            world.remove(this);
        } else if(another instanceof Formation<?> formation) {
            if(formation.getCollisionableEntities().stream().anyMatch(this::intersect)){
                world.remove(this);
            }
        }
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public boolean addHitListener(HitListener e) {
        return hitListeners.add(e);
    }

    public boolean removeHitListener(HitListener o) {
        return hitListeners.remove(o);
    }

    private void fireUfoDestroyed() {
        for (HitListener hitListener : hitListeners) {
            hitListener.ufoDestroyed();
        }
    }

}
