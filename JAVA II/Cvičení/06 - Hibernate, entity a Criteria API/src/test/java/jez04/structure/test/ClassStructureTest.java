package jez04.structure.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import cz.vsb.fei.kelvin.unittest.ProjectContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;

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
				new TextFileContains("module-info.java", "opens\\slab.data;"));
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

}
