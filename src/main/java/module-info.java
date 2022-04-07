module upt.coproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;
    requires lombok;

    opens upt.coproject to javafx.fxml;
    exports upt.coproject;
}