package lab;

import java.io.Serial;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import lombok.Getter;

public class Cannon extends WorldEntity {

    @Serial
    private static final long serialVersionUID = 3238999374976016723L;

    private static final double LENGTH = 60;
    private static final double WIDTH = 15;
    @Getter
    private double angle;

    public Cannon(World world, MyPoint position, double angle) {
        super(world, position);
        this.angle = angle;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.transform(new Affine(Transform.rotate(angle, position.getX(), position.getY() + WIDTH / 2)));
        gc.setFill(Color.BROWN);
        gc.fillRect(position.getX(), position.getY(), LENGTH, WIDTH);
    }

    @Override
    public void simulate(double deltaT) {
        /* nothing to do */
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), LENGTH, WIDTH);
    }

    public void setAngle(double angle) {
        this.angle = -angle;
    }

}
