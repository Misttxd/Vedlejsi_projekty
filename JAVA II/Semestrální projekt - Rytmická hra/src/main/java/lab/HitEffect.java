package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HitEffect implements DrawableSimulable, Runnable {

    private static final long SLEEP_TIME = 25;
    private static final double SLEEP_SECONDS = SLEEP_TIME / 1000.0;
    private static final double MOVE_SPEED = 20;

    private final Level level;
    private final String quality;
    private final Color color;
    private final double startLifeTime;
    private boolean finished = false;
    private Point2D cords;
    private double lifeTime;

    public HitEffect(Level level, Point2D cords, String quality, Color color, double lifeTime) {
        this.level = level;
        this.cords = cords;
        this.quality = quality;
        this.color = color;
        this.lifeTime = lifeTime;
        this.startLifeTime = lifeTime;

        Thread thread = new Thread(this);
        thread.setName("HitEffectThread");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        while (!isFinished()) {
            moveEffect();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                finish();
                Thread.currentThread().interrupt();
            }
        }
    }

    private synchronized void moveEffect() {
        cords = new Point2D(cords.getX(), cords.getY() - (MOVE_SPEED * SLEEP_SECONDS));
        lifeTime -= SLEEP_SECONDS;
        if (lifeTime <= 0) {
            finished = true;
        }
    }

    private synchronized boolean isFinished() {
        return finished;
    }

    private synchronized void finish() {
        finished = true;
    }

    @Override
    public synchronized void draw(GraphicsContext gc) {
        double opacity = lifeTime / startLifeTime;
        if (opacity < 0) {
            opacity = 0;
        }
        gc.setFill(color.deriveColor(0, 1, 1, opacity));
        gc.fillText(quality, cords.getX(), cords.getY());
    }

    @Override
    public void simulate(double deltaT) {
        if (isFinished()) {
            level.remove(this);
        }
    }
}
