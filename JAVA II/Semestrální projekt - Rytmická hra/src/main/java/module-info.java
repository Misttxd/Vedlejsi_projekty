module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires java.net.http;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.databind;

    requires static lombok;

    opens lab to javafx.fxml;
    opens lab.map to javafx.base;
    exports lab;
}
