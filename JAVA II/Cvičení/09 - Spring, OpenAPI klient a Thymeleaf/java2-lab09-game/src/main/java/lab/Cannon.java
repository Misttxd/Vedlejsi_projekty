package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class Cannon extends WorldEntity {

    private static final double LENGTH = 60;
    private static final double WIDTH = 15;
    private double angle = -45;

    public Cannon(World world, Point2D position, double angle) {
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
	}

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), LENGTH, WIDTH);
    }

    public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = -angle;
	}

}
