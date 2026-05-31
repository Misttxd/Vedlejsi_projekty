package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ProjectContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import lab.score.Level;
import lab.score.Score;
import lab.score.ScoreException;
import lab.score.ScoreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void jakartaAndHibernateAsDependencyTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
            "/project/dependencies/dependency/artifactId[text() = 'jakarta.persistence-api']"));
        assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
            "/project/dependencies/dependency/artifactId[text() = 'hibernate-core']"));
    }

    @Test
    void jakartaInModulInfoTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("module-info.java", "jakarta.persistence;"));
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("module-info.java", "opens\\s+lab.score;|opens\\s+lab.score\\s+to\\s+javafx.\\w+\\s*,\\s*org.hibernate.orm.core;"));
    }

    @Test
    void pesistenceXmlTest() throws URISyntaxException {
        ProjectContains projectContains = new ProjectContains("persistence.xml");
        assertThat(TextFileContains.getProjectRoot(getClass()), projectContains);
        assertThat(projectContains.getFoundFiles(), Matchers.not(Matchers.hasSize(0)));
        Path persistenceXml = projectContains.getFoundFiles().getFirst();
        assertThat(persistenceXml.toAbsolutePath().toString(),
            Matchers.endsWith(Paths.get("resources", "META-INF", "persistence.xml").toString()));
    }

    @Test
    void useEnumeratedTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("Score.java", "@Enumerated\\(\\s*EnumType.STRING\\s*\\)"));
    }

    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    class ScoreRepositoryTests {

        private Score template = new Score(null, "Tester", 100, Level.EASY);
        private ScoreRepository repository;

        @BeforeEach
        void init() {
            repository = ScoreRepository.getInstance();
        }

        @AfterEach
        void cleanUp() {
            repository.stop();
        }

        boolean same(Score s1, Score s2) {
            return s1.getLevel() == s2.getLevel() && s1.getScore() == s2.getScore()
                && Objects.equals(s1.getNickName(), s2.getNickName());
        }

        @Test
        @Order(100)
        void jpaScoreInsertTest() throws ScoreException {
            Score savedScore = repository.save(template);
            assertThat(savedScore.getId(), Matchers.notNullValue());
        }

        @Test
        @Order(200)
        void jpaScoreReadTest() throws ScoreException {
            List<Score> savedScores = repository.load().stream().filter(s -> same(s, template)).toList();
            assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
        }

        @Test
        @Order(250)
        void jpaReadSortedTest() throws ScoreException {
            for (int i = 0; i < 20; i++) {
                repository.save(template.toBuilder().score(i).build());
            }
            List<Score> result = repository.loadTopTen();
            assertThat(result, Matchers.hasSize(10));
            for (int i = 0; i < result.size()-1; i++) {
                assertThat(result.get(i).getScore(), Matchers.greaterThanOrEqualTo(result.get(i+1).getScore()));
            }
        }

        @Test
        @Order(300)
        void jpaScoreModifyTest() throws ScoreException {
            List<Score> savedScores = repository.load().stream().filter(s -> same(s, template)).toList();
            List<Long> savedIds = savedScores.stream().map(Score::getId).toList();
            assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
            for (Score score : savedScores) {
                score.setLevel(Level.HARD);
                repository.save(score);
            }
            List<Score> modifiedScores = repository.load().stream().filter(s -> savedIds.contains(s.getId())).toList();
            assertThat(modifiedScores, Matchers.not(Matchers.hasSize(0)));
            List<Score> originalDataScores = repository.load().stream().filter(s -> same(s, template)).toList();
            assertThat(originalDataScores, Matchers.hasSize(0));
        }

        @Test
        @Order(400)
        void jpaScoreDeleteTest() throws ScoreException {
            template.setLevel(Level.HARD);
            List<Score> savedScores = repository.load().stream().filter(s -> same(s, template)).toList();
            List<Long> savedIds = savedScores.stream().map(Score::getId).toList();
            assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
            repository.delete(savedScores.getFirst());
            List<Score> modifiedScores = repository.load().stream().filter(s -> savedIds.contains(s.getId())).toList();
            assertThat(modifiedScores, Matchers.hasSize(0));
            List<Score> originalDataScores = repository.load().stream().filter(s -> same(s, template)).toList();
            assertThat(originalDataScores, Matchers.hasSize(0));
        }

        @Test
        @Order(400)
        void jpaMergeTest() throws ScoreException {
            repository.save(template);
            repository.find(template.getId());
            Score copy = template.toBuilder().score(500).build();
            Score result = repository.save(copy);
            assertThat(result, Matchers.not(Matchers.sameInstance(copy)));
        }

        @Test
        @Order(500)
        void jpaModifyNoPersistNoMergeTest() throws URISyntaxException, IOException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ScoreException {
            template.setNickName("aaa");
            repository.save(template);
            repository.modifyNoPersistNoMerge(template.getId(), score -> score.setNickName("ok"));
            Object em = repository.getEntityManager();
            em.getClass().getMethod("clear").invoke(em);

            assertThat(repository.find(template.getId()).getNickName(), Matchers.equalTo("ok"));
            TextFileContains textFileContains = new TextFileContains("ScoreRepository.java",
                "void\\s+modifyNoPersistNoMerge[\\s\\S]*}").multiline(true);
            assertThat(TextFileContains.getProjectRoot(ClassStructureTest.class), textFileContains);
            Path score = textFileContains.getFoundFiles().getFirst();
            String src = Files.readString(score);
            String method = Pattern.compile("void\\s+modifyNoPersistNoMerge[\\s\\S]*}").matcher(src).results().findFirst().get().group();
            assertThat(method, Matchers.not(Matchers.containsString("persist")));
            assertThat(method, Matchers.not(Matchers.containsString("merge")));
        }
    }
}
