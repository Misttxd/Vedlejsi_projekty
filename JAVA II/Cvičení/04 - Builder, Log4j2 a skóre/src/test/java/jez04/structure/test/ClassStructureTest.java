package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ProjectMatch;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void lombokAsDependencyTest() throws URISyntaxException {
        XmlFileContains xmlFileContains = new XmlFileContains("pom.xml", "/project/dependencies/dependency/artifactId[text() = 'lombok']");
        Path root = TextFileContains.getProjectRoot(getClass());
        assertThat(root, xmlFileContains);
    }

    @Test
    void lombokAsAnnotationProcessorTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml", "/project/build/plugins/plugin/artifactId[text() = 'maven-compiler-plugin']"));
        assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml", "/project/build/plugins/plugin/configuration/annotationProcessorPaths/path/artifactId[text() = 'lombok']"));
    }

    @Test
    void moduleInfoTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains("module-info.java", "lombok;"));
    }

    @CsvSource({
        "@Getter,4",
        "@.*Builder,1",
        "@.*Default,3",
        "@.*Log4j2,2"
    })
    @ParameterizedTest(name = "useLombokTest - annotation {0}")
    void useLombokTest(String text, int count) {
        assertThat(helper, new ProjectMatch(count, new SrcContains(text)));
    }

    @CsvSource({
        "Score,@Setter,1",
        "Score,@Getter,1",
        "Score,@AllArgsConstructor,1",
        "Score,@EqualsAndHashCode,1",
        "Score,@ToString,1"
    })
    @ParameterizedTest(name = "useLombokTest in {1} annotation {2}")
    void useLombokOrDataTest(String className, String text, int count) throws URISyntaxException, ClassNotFoundException {
        Class<?> config = helper.getClass(className);
        assertThat(config, Matchers.anyOf(new SrcContains(text).count(count),
            new SrcContains("@Data").count(1)));
    }

    @Test
    void recordTest() {
        long recordCount = helper.getAllClazzes().stream().filter(Class::isRecord).count();
        assertThat(recordCount, Matchers.greaterThanOrEqualTo(1L));

    }


}
