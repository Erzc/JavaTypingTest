module com.erzc.typingtestapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;


    opens com.erzc.typingtestapp to javafx.fxml;
    exports com.erzc.typingtestapp;
}