module agh.bedbooker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens agh.bedbooker to javafx.fxml;
    opens agh.bedbooker.database to javafx.base;
    exports agh.bedbooker;
    exports agh.bedbooker.admin;
    exports agh.bedbooker.client;
    opens agh.bedbooker.admin to javafx.fxml;
    opens agh.bedbooker.client to javafx.fxml;
}