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
import org.h2.tools.Server;

public class ScoreRepository {

    private Server server = null;
    private Connection connection;

    private static ScoreRepository instance;

    public static ScoreRepository getInstance() {
        if (instance == null) {
            instance = new ScoreRepository();
        }
        return instance;
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
                    points integer
                );
                """);
        } catch (SQLException e) {
            System.out.println("Table already exists.");
            e.printStackTrace();
        }
        startDBWebServer();
    }

    public void save(Score score) throws ScoreException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(
            "INSERT INTO Scores (name, points) VALUES (?, ?)")) {
            preparedStatement.setString(1, score.getNickName());
            preparedStatement.setInt(2, score.getScore());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("SQL insert error.", e);
        }
    }

    public void save(List<Score> scores) throws ScoreException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(
            "INSERT INTO Scores (name, points) VALUES (?, ?)")) {
            for (Score score : scores) {
                preparedStatement.setString(1, score.getNickName());
                preparedStatement.setInt(2, score.getScore());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new ScoreException("SQL insert error.", e);
        }
    }

    public List<Score> load() throws ScoreException {
        try (Statement statement = getConnection().createStatement()) {
            List<Score> result = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Scores")) {
                while (resultSet.next()) {
                    result.add(
                        new Score(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("points")));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new ScoreException("SQL insert error.", e);
        }
    }

    public void delete(Score score) throws ScoreException {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM Scores where id=" + score.getId());
        } catch (SQLException e) {
            throw new ScoreException("DB delete problem", e);
        }
    }

    private void startDBWebServer() {
        // Start HTTP server for access H2 DB for look inside
        Path h2ServerProperties = Paths.get(System.getProperty("user.home"), ".h2.server.properties");
        try {
            Files.writeString(h2ServerProperties,
                "0=Generic H2 (Embedded)|org.h2.Driver|jdbc\\:h2\\:file\\:./score-db|", StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            System.out.println("File " + h2ServerProperties + " probably exists.");
        }
        stop();
        try {
            server = Server.createWebServer();
            System.out.println(server.getURL());
            server.start();
            System.out.println("DB Web server started!");
        } catch (SQLException e) {
            System.out.println("Cannot create DB web server.");
            e.printStackTrace();
        }
    }

    public void stop() {
        // Stop HTTP server for access H2 DB
        if (server != null) {
            System.out.println("Ending DB web server BYE.");
            server.stop();
        }
    }

    private void waitForKeyPress() {
        System.out.println("Waitnig for Key press (ENTER)");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Cannot read input from keyboard.");
            e.printStackTrace();
        }
    }
}
