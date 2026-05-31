package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class GameController {

    @FXML
    private Slider angle;

    @FXML
    private Slider speed;

    @FXML
    private Canvas canvas;

    @FXML
    private Label hits;

    private DrawingThread timer;
    private World world;

    private int hitCount = 0;

    @FXML
    void fire(ActionEvent event) {
        double cannonAngle = world.getCannon().getAngle();
        BulletAnimated bulletAnimated = new BulletAnimated(world, world.getCannon().getPosition(), speed.getValue(),
            cannonAngle, World.GRAVITY);
        world.add(bulletAnimated);
        bulletAnimated.addHitListener(this::increaseHits);
        bulletAnimated.addHitListener(() -> System.out.println("au!!!!"));
    }

    private void updateHits() {
        hits.setText(String.format("%03d", hitCount));
    }

    private void increaseHits() {
        hitCount++;
        updateHits();
    }

    @FXML
    void initialize() {
        assert angle != null : "fx:id=\"angle\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'gameWindow.fxml'.";
        world = new World(canvas.getWidth(), canvas.getHeight());
//        timer = new DrawingThread(canvas, world);
//        timer.start();
        angle.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                world.getCannon().setAngle(newValue.doubleValue());
            }
        });
    }

    public void stop() {
        timer.stop();
    }

    public void startGame(String name, int numberOfMonsters) {
//        playerName.setText(name);
//        level = new Level(canvas.getWidth(), canvas.getHeight(), numberOfMonsters);
        timer = new DrawingThread(canvas, world);
        timer.start();

    }

}
