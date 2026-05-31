package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ContainsInnerClasses;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.HasPropertyWithAnnotation;
import cz.vsb.fei.kelvin.unittest.IsDescendatOf;
import cz.vsb.fei.kelvin.unittest.ProjectMatch;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.io.FileDescriptor;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.image.Image;
import lab.Bullet;
import lab.BulletAnimated;
import lab.DrawableSimulable;
import lab.RotatingUfoFormation;
import lab.Ufo;
import lab.UfoSpawner;
import lab.World;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);


    @Test
    void drawalbeSimluableTest() {
        assertThat(DrawableSimulable.class, new IsDescendatOf(Serializable.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bullet", "BulletAnimated", "Cannon", "MyPoint", "RotatingUfoFormation", "Ufo",
        "UfoSpawner"})
    void serialVersionUIDTest(String className) throws ClassNotFoundException {
        Class<?> clazz = helper.getClass(className);
        assertThat(clazz, new HasProperty("serialVersionUID", long.class));
    }

    @Test
    void serialVersionUIDUniqnessTest() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        List<String> classNames = List.of("Bullet", "BulletAnimated", "Cannon", "MyPoint", "RotatingUfoFormation", "Ufo",
            "UfoSpawner");
        Set<Long> uniqueSerialversionUIDs = new HashSet<>();
        for (String className : classNames) {
            Class<?> clazz = helper.getClass(className);
            Field serial = clazz.getDeclaredField("serialVersionUID");
            serial.setAccessible(true);
            uniqueSerialversionUIDs.add((long)serial.get(null));
        }
        assertThat(uniqueSerialversionUIDs, Matchers.hasSize(classNames.size()));
    }

    @ParameterizedTest
    @CsvSource({
        "UfoSpawner,world",
        "WorldEntity,world",
        "Ufo,image",
        "BulletAnimated,image",
        "Bullet,hitListeners"
    })
    void transientTest(String className, String fieldName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = helper.getClass(className);
        Assertions.assertTrue(Modifier.isTransient(clazz.getDeclaredField(fieldName).getModifiers()), String.format("%s.%s has to be transient.", className, fieldName));
    }
    @Test
    void ufoGetImageTest()  {
        assertThat(Ufo.class, new HasMethod("getImage", Image.class));
    }
    @Test
    void bulletAnimatedGetImageTest()  {
        assertThat(BulletAnimated.class, new HasMethod("getImage", Image.class));
    }
    @Test
    void myPointTest()  {
        assertThat(helper, new ProjectMatch(30, new SrcContains("MyPoint")));
    }

    @Test
    void objectStreams1Test()  {
        assertThat(World.class , new SrcContains("ObjectOutputStream"));
    }

    @Test
    void objectStreams2Test()  {
        assertThat(World.class , new SrcContains("ObjectInputStream"));
    }

    @Test
    void socketTest()  {
        assertThat(World.class , new SrcContains("Socket"));
    }

    @Test
    void serverSocketTest()  {
        assertThat(World.class , new SrcContains("ServerSocket"));
    }

    @Test
    void threadTest()  {
        assertThat(World.class , new SrcContains("new Thread\\(").count(3));
    }

    @Test
    void threadInnerClassesOrLambdasTest()  {
        assertThat(World.class , new ContainsInnerClasses(7));
    }

    @Test
    void synchronizedTest()  {
        assertThat(World.class , new SrcContains("synchronized").count(4));
    }

}
