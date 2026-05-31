package lab.score;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ScoreControllerApi;

@Log4j2
public class ScoreRepository {

    private Server server = null;

    private EntityManager em;
    private EntityManagerFactory entityManagerFactory;

    private ScoreControllerApi api;

    private static ScoreRepository instance;

    public static ScoreRepository getInstance() {
        if (instance == null) {
            instance = new ScoreRepository();
        }
        return instance;
    }

    public ScoreRepository() {
        getEntityManager();
        api = new ScoreControllerApi();
        api.setCustomBaseUrl("http://localhost:8080");
    }

    public void init() {
        startDBWebServer();
        getEntityManager();
    }

    public Score save(Score score) throws ScoreException {
        try {
            org.openapitools.client.model.Score apiScore = new org.openapitools.client.model.Score();
            if (score.getId() != null) apiScore.setId(score.getId());
            apiScore.setNickName(score.getNickName());
            apiScore.setScore(score.getScore());
            apiScore.setLevel(org.openapitools.client.model.Score.LevelEnum.fromValue(score.getLevel().name()));
            org.openapitools.client.model.Score saved = api.save(apiScore);
            score.setId(saved.getId());
        } catch (Exception e) {
            log.error("API Save Failed", e);
        }

        Score result;
        em.getTransaction().begin();
        if (score.getId() == null || score.getId() == 0) {
            em.persist(score);
            result = score;
        } else {
            result = em.merge(score);
        }
        em.getTransaction().commit();
        return result;
    }

    public void save(List<Score> scores) throws ScoreException {
        for (Score score : scores) {
            save(score);
        }
    }

    public List<Score> load() throws ScoreException {
        try {
            return api.getAll().stream().map(s -> 
                new Score(s.getNickName(), s.getScore(), Level.valueOf(s.getLevel().name()))
            ).toList();
        } catch (Exception e) {
            log.error("API Load Failed", e);
            return em.createQuery("select s from Score s", Score.class).getResultList();
        }
    }

    public List<Score> loadTopTen() throws ScoreException {
        // since our API does not have getTop() mapped yet or we don't know the exact name generated, we'll try API if generated or fallback
        try {
            return api.getAll().stream()
                    .sorted((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()))
                    .limit(10)
                    .map(s -> new Score(s.getNickName(), s.getScore(), Level.valueOf(s.getLevel().name())))
                    .toList();
        } catch (Exception e) {
            log.error("API LoadTopTen Failed", e);
            return em.createQuery("select s from Score s order by s.score desc", Score.class).setMaxResults(10).getResultList();
        }
    }

    public void delete(Score score) throws ScoreException {
        em.getTransaction().begin();
        em.remove(score);
        em.getTransaction().commit();
    }

    public void modifyNoPersistNoMerge(Long id, Consumer<Score> motificator) throws ScoreException {
        em.getTransaction().begin();
        Score score = em.find(Score.class, id);
        motificator.accept(score);
        em.getTransaction().commit();
    }

    public Score find(Long id) throws ScoreException {
        return em.find(Score.class, id);
    }

    public Object getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("java2");
        }
        if (em == null) {
            em = entityManagerFactory.createEntityManager();
        }
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
