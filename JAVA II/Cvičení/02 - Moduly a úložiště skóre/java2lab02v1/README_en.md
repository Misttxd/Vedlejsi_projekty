# Java 2 - Exercise 2 v1 - Java Modules

In the project  <https://gitlab.vsb.cz/jez04-vyuka/java2/labs/java2lab02v1.git>

### Preparation

Analyze the `cz.vsb.fei.java2.lab01.dbstore.ScoreRepository` class and extract the `ScoreStorageInterface` interface from it,
which will have methods for:
- initializing the repository
- storing one or more scores
- loading all `Score`
- deleting `Score`
- stopping the repository

The `ScoreStorageFactory` class will implement this new interface.

Create a `ScoreStorageFactory` class that uses the `Singleton` design pattern and creates an instance
of the `ScoreStorageFactory` class, but works with it as if it were `ScoreStorageInterface`.

### API

Move the new `ScoreStorageInterface` interface together with the `Score` and `ScoreException` class to a separate
Maven project (ScoreApi, ScoreCommon, ScoreBase, ...). The project must export the package with the interface and the `Score` class in `module-info`.
You must also run
```cmd
 mvn install
 ```
to install the project into your local `.m2` repository.

In the original game project, you must add the new project with the interface to the dependencies in `pom.xml`

### DB Implementation

Create another new Maven project (ScoreDBImpl, DBConnector, DbRepository, ...) and move the `ScoreStorageFactory` class into it
. The project must have a dependency on the project with the
interface (API) and on the db driver for h2 in `pom.xml`. The project must export the package with the `ScoreStorageFactory` class in `module-info`.
In `module-info`, also use the `provides` directive
to offer the class that implements the `ScoreStorageInterface` interface.

### Connection

The original game project will declare the use of the `ScoreStorageInterface` interface in `module-info`.
The dependency on the H2 db driver no longer needs to be in `pom.xml`, but there must be a dependency on the new project
with the `ScoreStorageFactory` class (DB Implementation). Modify the `ScoreStorageFactory` class so that it does not directly create an instance of the
`ScoreStorageFactory` class, but uses the `ServiceLoader` class to obtain an instance of the class that implements the
`ScoreStorageInterface` interface.

Create another project, similar to the project with the `DBConnctor` class, but this time the scores will be
stored in a file and not in a database. Add the project as a dependency in the game project.
Now `ServiceLoader` returns a collection of two implementations and you can choose (e.g. randomly) which one to use.

![module graph](https://swi.cs.vsb.cz/.imaging/stk/pop/content/dam/jezek/images/java2-lab02.drawio0/jcr:content/java2-lab02.drawio.png.2025-02-24-14-10-31.png)
