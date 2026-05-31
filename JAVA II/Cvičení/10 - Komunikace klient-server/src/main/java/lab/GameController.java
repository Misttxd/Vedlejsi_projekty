package lab;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController {

    private static final Logger log = LogManager.getLogger(GameController.class);

    @FXML
    private Slider angle;

    @FXML
    private Slider speed;

    @FXML
    private Canvas canvas;

    @FXML
    private Label hits;

    private DrawingThread timer;
    private transient World world;

    private int hitCount = 0;

    @FXML
    void fire(ActionEvent event) {
        double cannonAngle = world.getCannon().getAngle();
        BulletAnimated bulletAnimated = new BulletAnimated(world, world.getCannon().getPosition(), speed.getValue(),
            cannonAngle, World.GRAVITY);
        world.add(bulletAnimated);
        bulletAnimated.addHitListener(this::increaseHits);
        bulletAnimated.addHitListener(() -> log.info("au!!!!"));
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
        speed.setMin(Setting.getInstance().getBulletMinSpeed());
        speed.setMax(Setting.getInstance().getBulletMaxSpeed());
        world = new World(canvas.getWidth(), canvas.getHeight());
        angle.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                world.getCannon().setAngle(newValue.doubleValue());
            }
        });
        log.info("Screen initialized.");
    }

    public void stop() {
        timer.stop();
        log.info("Screen - stop game.");
        world.getDestroyInfos().forEach(log::info);
    }

    public void startGame(String name, int numberOfMonsters, boolean spectatorMode) {
        world.setSpectatorMode(spectatorMode);
        timer = new DrawingThread(canvas, world);
        timer.start();
    }

}
