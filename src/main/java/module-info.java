module com.visionstructure.visionstructure {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.swing;
    requires opencv;


    opens com.visionstructure.visionstructure to javafx.fxml;
    exports com.visionstructure.visionstructure;
}