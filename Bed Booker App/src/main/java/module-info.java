module agh.bedbooker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens agh.bedbooker to javafx.fxml;
    exports agh.bedbooker;
}