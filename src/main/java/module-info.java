module com.j4va.kair {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;


    opens com.j4va.kair to javafx.fxml;
    exports com.j4va.kair;
}
