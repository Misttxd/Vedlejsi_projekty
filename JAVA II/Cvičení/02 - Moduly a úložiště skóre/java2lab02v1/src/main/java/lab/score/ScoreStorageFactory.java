package lab.score;

import cz.vsb.fei.java2.lab01.scoreapi.ScoreStorageInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

public class ScoreStorageFactory {
    private static ScoreStorageInterface INSTANCE;
    private static final Random RANDOM = new Random();

    private ScoreStorageFactory() {
    }

    public static ScoreStorageInterface getInstance() {
        if(INSTANCE == null) {
            List<ScoreStorageInterface> implementations = new ArrayList<>();
            ServiceLoader.load(ScoreStorageInterface.class).forEach(implementations::add);
            if (implementations.isEmpty()) {
                throw new RuntimeException("No ScoreStorageInterface implementation found");
            }
            INSTANCE = implementations.get(RANDOM.nextInt(implementations.size()));
            System.out.println("Selected storage: " + INSTANCE.getClass().getName());
        }

        return INSTANCE;
    }
}
