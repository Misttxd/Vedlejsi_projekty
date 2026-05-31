module cz.vsb.fei.java2.lab01.fsstore {
	requires static lombok;
	requires org.apache.logging.log4j;
    requires cz.vsb.fei.java2.lab01.scoreapi;
    exports cz.vsb.fei.java2.lab01.fsstore;

    provides cz.vsb.fei.java2.lab01.scoreapi.ScoreStorageInterface
        with cz.vsb.fei.java2.lab01.fsstore.FileScoreStorage;
}