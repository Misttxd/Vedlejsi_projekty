package cz.vsb.fei.java2.lab01.fsstore;

import cz.vsb.fei.java2.lab01.scoreapi.Score;
import cz.vsb.fei.java2.lab01.scoreapi.ScoreException;
import cz.vsb.fei.java2.lab01.scoreapi.ScoreStorageInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileScoreStorage implements ScoreStorageInterface {

    private static final String FILE_NAME = "scores.csv";
    private static final String SEPARATOR = ";";
    private final Path filePath;
    private long nextId = 1;

    public FileScoreStorage() {
        this.filePath = Paths.get(FILE_NAME);
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            // determine next ID from existing data
            List<Score> existing = load();
            for (Score s : existing) {
                if (s.getId() >= nextId) {
                    nextId = s.getId() + 1;
                }
            }
        } catch (IOException | ScoreException e) {
            System.out.println("Cannot initialize file storage: " + e.getMessage());
        }
    }

    @Override
    public void save(Score score) throws ScoreException {
        try {
            if (score.getId() == 0) {
                score.setId(nextId++);
            }
            String line = score.getId() + SEPARATOR + score.getNickName() + SEPARATOR + score.getScore();
            Files.writeString(filePath, line + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ScoreException("Cannot save score to file.", e);
        }
    }

    @Override
    public void save(List<Score> scores) throws ScoreException {
        for (Score score : scores) {
            save(score);
        }
    }

    @Override
    public List<Score> load() throws ScoreException {
        List<Score> result = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return result;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(SEPARATOR);
                if (parts.length != 3) {
                    throw new ScoreException(i + 1, "Invalid format in scores file");
                }
                try {
                    long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    int points = Integer.parseInt(parts[2]);
                    result.add(new Score(id, name, points));
                } catch (NumberFormatException e) {
                    throw new ScoreException(i + 1, "Cannot parse number", e);
                }
            }
        } catch (IOException e) {
            throw new ScoreException("Cannot read scores from file.", e);
        }
        return result;
    }

    @Override
    public void delete(Score score) throws ScoreException {
        try {
            List<Score> all = load();
            all.removeIf(s -> s.getId() == score.getId());
            // rewrite file
            Files.writeString(filePath, "");
            for (Score s : all) {
                String line = s.getId() + SEPARATOR + s.getNickName() + SEPARATOR + s.getScore();
                Files.writeString(filePath, line + System.lineSeparator(),
                        java.nio.file.StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new ScoreException("Cannot delete score from file.", e);
        }
    }

    @Override
    public void stop() {
        // nothing to close for file storage
    }
}
