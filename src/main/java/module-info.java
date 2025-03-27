module com.example.crittercaresheltermanagementsim {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.crittercaresheltermanagementsim to javafx.fxml;
    exports com.example.crittercaresheltermanagementsim;
}