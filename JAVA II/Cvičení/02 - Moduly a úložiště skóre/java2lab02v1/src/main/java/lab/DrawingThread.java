package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final World world;

    private long lastFrame = 0;
    private double fpsSum = 0;
    private double fpsCount = 0;
    private int avergeFps = 0;

    public DrawingThread(Canvas canvas, World world) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.world = world;
    }

    /**
     * Draws objects into the canvas. Put you code here.
     */
    @Override
    public void handle(long now) {
        double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
        lastFrame = now;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.world.draw(gc);
        drawFps(delta);
        this.world.simulate(delta);
    }

    private void drawFps(double delta) {
        int fps = calcFps(delta);
        gc.setFont(new Font("Arial", 30));
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("FPS: %04d", fps), canvas.getWidth()-150, canvas.getHeight() - 10);
    }

    private int calcFps(double delta) {
        fpsSum += 1 / delta;
        fpsCount += 1;
        if (fpsCount >= 100) {
            avergeFps = (int) (fpsSum / fpsCount);
            fpsSum = 0;
            fpsCount = 0;
        }
        return avergeFps;
    }
}
