module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires cz.vsb.fei.java2.lab01.scoreapi;
    requires cz.vsb.fei.java2.lab01.dbstore;
    requires cz.vsb.fei.java2.lab01.fsstore;

    uses cz.vsb.fei.java2.lab01.scoreapi.ScoreStorageInterface;

    opens lab to javafx.fxml;
    exports lab;
    exports lab.score;
    opens lab.score to javafx.fxml;
}
