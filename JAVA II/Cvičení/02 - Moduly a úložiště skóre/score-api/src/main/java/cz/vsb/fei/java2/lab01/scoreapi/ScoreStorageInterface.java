package cz.vsb.fei.java2.lab01.scoreapi;

import java.util.List;

public interface ScoreStorageInterface {
    void init();

    void save(Score score) throws ScoreException;

    void save(List<Score> scores) throws ScoreException;

    List<Score> load() throws ScoreException;

    void delete(Score score) throws ScoreException;

    void stop();
}
