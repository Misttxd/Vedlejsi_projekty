module cz.vsb.fei.java2.lab01.dbstore {
	requires static lombok;
	requires org.apache.logging.log4j;
    requires cz.vsb.fei.java2.lab01.scoreapi;
    exports cz.vsb.fei.java2.lab01.dbstore;

    requires java.sql;
    requires com.h2database;

    provides cz.vsb.fei.java2.lab01.scoreapi.ScoreStorageInterface
        with cz.vsb.fei.java2.lab01.dbstore.ScoreRepository;
}