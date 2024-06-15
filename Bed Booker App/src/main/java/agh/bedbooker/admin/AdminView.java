package agh.bedbooker.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminView {

    @FXML
    private AnchorPane rootAnchorPane;

    public void loadScene(String fxmlFile) {
        try {
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 960, 640);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStatisticButtonClick() {
        loadScene("admin-statistic-view.fxml");
    }

    @FXML
    public void onRoomsButtonClick() {
        loadScene("admin-rooms-view.fxml");
    }

    @FXML
    public void onClientsButtonClick() {
        loadScene("admin-clients-view.fxml");
    }

    @FXML
    public void onReservationsButtonClick() {
        loadScene("admin-reservations-view.fxml");
    }

    @FXML
    public void onDamagesButtonClick() {
        loadScene("admin-damages-view.fxml");
    }
}
