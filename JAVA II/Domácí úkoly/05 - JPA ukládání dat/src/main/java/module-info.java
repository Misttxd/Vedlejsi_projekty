module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires com.h2database;

    requires static lombok;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jakarta.transaction;
    requires jakarta.cdi;

    opens lab;
    opens lab.map;
    opens lab.score;
    exports lab;
    exports lab.score;
}
