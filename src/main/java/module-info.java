module com.markn.equalizerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires kotlin.stdlib;

    opens ru.markn.equalizerfx.GUI to javafx.fxml;
    exports ru.markn.equalizerfx.GUI;
}