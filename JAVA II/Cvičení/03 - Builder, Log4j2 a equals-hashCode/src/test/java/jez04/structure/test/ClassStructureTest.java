package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.InnerClassExists;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.Arrays;
import lab.Setting;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void builderExistTest() {
        assertThat(Setting.class, new InnerClassExists("Buil.*"));
    }

    @Test
    void settingNonPublicConstructorTest() {
        assertTrue(Arrays.stream(Setting.class.getDeclaredConstructors())
            .allMatch(constructor -> !Modifier.isPublic(constructor.getModifiers())), "No all constructors of Setting are private or protected");
    }

    @Test
    void builderMethodTest() throws ClassNotFoundException {
        Class<?> builder = helper.getClassRegexp(".*Setting.Buil.*");
        assertThat(Setting.class, new HasMethod("buil.*", builder).useRegExp(true).staticTag(true));
    }

    @Test
    void useLoggerTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains(".*\\.java", "LogManager\\.getLogger").useRegExpForName(true).count(2));
    }
    @Test
    void useTraceTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains(".*\\.java", "\\.trace").useRegExpForName(true));
    }
    @Test
    void logAppenderTest() throws URISyntaxException{
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("log4j2.xml", "AppenderRef").caseInsensitive(true));
    }
    @Test
    void logTraceTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("log4j2.xml", "trace").caseInsensitive(true));
    }
    @Test
    void logStartupTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("log4j2.xml", "OnStartupTriggeringPolicy").caseInsensitive(true));
    }

    @Test
    void logSizeTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("log4j2.xml", "SizeBasedTriggeringPolicy").caseInsensitive(true));
    }

    @Test
    void logTimeTest() throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()),
            new TextFileContains("log4j2.xml", "TimeBasedTriggeringPolicy").caseInsensitive(true));
    }

}
