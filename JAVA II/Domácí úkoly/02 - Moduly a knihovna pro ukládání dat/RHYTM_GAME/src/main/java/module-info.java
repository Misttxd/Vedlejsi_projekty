module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;
    requires java.sql;
    requires com.h2database;

    requires rhytmGame.mapDataManagement;

    opens lab to javafx.fxml;
    exports lab;
}
