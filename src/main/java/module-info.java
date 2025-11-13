module com.j4va.kair.kairprojectmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.j4va.kair to javafx.fxml;
    exports com.j4va.kair;
}