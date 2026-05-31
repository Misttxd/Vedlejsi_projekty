package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.IsDescendatOf;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.lang.reflect.Modifier;
import java.util.List;
import javafx.geometry.Point2D;
import lab.DrawableSimulable;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testWorldStreamGenerate() throws ClassNotFoundException {
        Class<?> world = helper.getClass("World");
        assertThat(world, new SrcContains("Stream.(<.*>)?generate"));
    }

    @Test
    void testFormationExists() {
        assertThat(ClassStructureTest.class, new ClassExist("lab.Formation"));
    }

    @Test
    void testFormationAbstract() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(Modifier.isAbstract(formation.getModifiers()), Matchers.is(true));
    }

    @Test
    void testFormationImplements() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new IsDescendatOf("WorldEntity"));
    }

    @Test
    void testFormationImplements2() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new IsDescendatOf("Collisionable"));
    }

    @Test
    void testFormationGenerics() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        Class<?> drawableSimulable = helper.getClass("DrawableSimulable");
        assertThat("Type parameters should be one", formation.getTypeParameters().length, Matchers.is(1));
        assertThat("Type parameters should T extends VisualEntity", formation.getTypeParameters()[0].getBounds()[0],
            Matchers.is(drawableSimulable));
    }

    @Test
    void testFormationProperties1() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new HasProperty(".*", List.class));
    }

    @Test
    void testFormationProperties2() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new HasProperty(".*", Point2D.class));
    }

    @Test
    void testFormationConstructor() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        Class<?> world = helper.getClass("World");
        Class<?> visualEntity = helper.getClass("DrawableSimulable");
        assertThat(formation, new HasConstructor(world, Point2D.class, visualEntity.arrayType()));
        assertThat(formation, new SrcContains("[A-Z]..."));
    }

    @Test
    void testFormationMethod() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new HasMethod(".*", void.class, double.class).abstractTag(true).useRegExp(true));
    }

    @Test
    void testFormationMethod2() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new HasMethod(".*", List.class).useRegExp(true));
    }

    @Test
    void testFormationBoundingBox() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new SrcContains("\\.reduce\\("));
    }

    @Test
    void testRotatingUfoFormationExists() {
        assertThat(ClassStructureTest.class, new ClassExist("RotatingUfoFormation"));
    }

    @Test
    void testRotatingUfoFormationExtends() throws ClassNotFoundException {
        Class<?> rotatingUfoFormation = helper.getClass("RotatingUfoFormation");
        assertThat(rotatingUfoFormation, new IsDescendatOf("lab.Formation"));
    }

    @Test
    void testRotatingUfoFormationConstuctor() throws ClassNotFoundException {
        Class<?> rotatingUfoFormation = helper.getClass("RotatingUfoFormation");
        Class<?> world = helper.getClass("World");
        Class<?> ufo = helper.getClass("Ufo");
        assertThat(rotatingUfoFormation, new HasConstructor(world, Point2D.class, ufo.arrayType()));
    }

    @Test
    void testRotatingUfoFormationUseOfRotatingUfoFormation() throws ClassNotFoundException {
        Class<?> world = helper.getClass("World");
        assertThat(world, new SrcContains("RotatingUfoFormation"));
    }

    @Test
    void testFormationMethod3() throws ClassNotFoundException {
        Class<?> formation = helper.getClass("lab.Formation");
        assertThat(formation, new HasMethod(".*", boolean.class, DrawableSimulable.class).useRegExp(true));
    }
}
