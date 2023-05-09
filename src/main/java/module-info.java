module raycastingengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;
    requires mockito.all;
    opens application to javafx.fxml;
    exports application;
}