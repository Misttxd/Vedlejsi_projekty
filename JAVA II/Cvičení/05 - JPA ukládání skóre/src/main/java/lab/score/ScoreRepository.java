package lab.score;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transaction;
import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;

@Log4j2
public class ScoreRepository {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private Server server = null;
    private Connection connection;

    private static ScoreRepository instance;

    public static ScoreRepository getInstance() {
        if (instance == null) {
            instance = new ScoreRepository();
        }
        return instance;
    }

    public ScoreRepository() {
        emf = Persistence.createEntityManagerFactory("java2"); // java2 podle toho co je v tom xml souboru
        em = emf.createEntityManager();
    }

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:h2:file:./score-db");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void init() {
        try (Statement stm = getConnection().createStatement()) {
            stm.executeUpdate("""
                    CREATE TABLE Scores (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        points integer,
                        level VARCHAR(255)
                    );
                    """);
        } catch (SQLException e) {
            log.warn("Table already exists.", e);
        }
        startDBWebServer();
    }

    public Score save(Score score) throws ScoreException {
        // try (PreparedStatement preparedStatement = getConnection().prepareStatement(
        // "INSERT INTO Scores (name, points, level) VALUES (?, ?, ?)")) {
        // preparedStatement.setString(1, score.getNickName());
        // preparedStatement.setInt(2, score.getScore());
        // preparedStatement.setString(3, score.getLevel().toString());
        // preparedStatement.executeUpdate();
        // return null;
        // } catch (SQLException e) {
        // throw new ScoreException("SQL insert error.", e);
        // }
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (score.getId() == null || score.getId() == 0) {
                em.persist(score);
            } else {
                score = em.merge(score);
            }
            tx.commit();
            return score;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void save(List<Score> scores) throws ScoreException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(
                "INSERT INTO Scores (name, points, level) VALUES (?, ?, ?)")) {
            for (Score score : scores) {
                preparedStatement.setString(1, score.getNickName());
                preparedStatement.setInt(2, score.getScore());
                preparedStatement.setString(3, score.getLevel().toString());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new ScoreException("SQL insert error.", e);
        }

    }

    public List<Score> load() throws ScoreException {
        // try (Statement statement = getConnection().createStatement()) {
        // List<Score> result = new ArrayList<>();
        // try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Scores")) {
        // while (resultSet.next()) {
        // result.add(
        // new Score(resultSet.getLong("id"), resultSet.getString("name"),
        // resultSet.getInt("points"), Level.valueOf(resultSet.getString("level"))));
        // }
        // }
        // return result;
        // } catch (SQLException e) {
        // throw new ScoreException("SQL insert error.", e);
        // }

        return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();

    }

    public List<Score> loadTopTen() throws ScoreException {
//        try (Statement statement = getConnection().createStatement()) {
//            List<Score> result = new ArrayList<>();
//            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Scores ORDER BY points DESC LIMIT 10")) {
//                while (resultSet.next()) {
//                    result.add(
//                            new Score(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("points"),
//                                    Level.valueOf(resultSet.getString("level"))));
//                }
//            }
//            return result;
//        } catch (SQLException e) {
//            throw new ScoreException("SQL insert error.", e);
//        }

        return em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC", Score.class).setMaxResults(10).getResultList();
    }

    public void delete(Score score) throws ScoreException {
        em.getTransaction().begin();
        em.remove(score);
        em.getTransaction().commit();
    }

    public void modifyNoPersistNoMerge(Long id, Consumer<Score> motificator) throws ScoreException {
        em.getTransaction().begin();
        motificator.accept(em.find(Score.class, id));
        em.getTransaction().commit();
    }

    public Score find(Long id) throws ScoreException {
        return em.find(Score.class, id);
    }

    public Object getEntityManager() {

        return em;
    }

    private void startDBWebServer() {
        // Start HTTP server for access H2 DB for look inside
        Path h2ServerProperties = Paths.get(System.getProperty("user.home"), ".h2.server.properties");
        try {
            Files.writeString(h2ServerProperties,
                    "0=Generic H2 (Embedded)|org.h2.Driver|jdbc\\:h2\\:file\\:./score-db|",
                    StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            log.warn("File {} probably exists.", h2ServerProperties);
        }
        stop();
        try {
            server = Server.createWebServer();
            log.info(server.getURL());
            server.start();
            log.info("DB Web server started!");
        } catch (SQLException e) {
            log.error("Cannot create DB web server.", e);
        }
    }

    public void stop() {
        // Stop HTTP server for access H2 DB
        if (server != null) {
            log.info("Ending DB web server BYE.");
            server.stop();
        }
    }

}
