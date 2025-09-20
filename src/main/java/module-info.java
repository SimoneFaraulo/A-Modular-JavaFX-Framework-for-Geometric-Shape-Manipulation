module group2128.sadproject.sadproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires org.json;

    opens group2128.sadproject.sadproject to javafx.fxml;
    exports group2128.sadproject.sadproject;
}