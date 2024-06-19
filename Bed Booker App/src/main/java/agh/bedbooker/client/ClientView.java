package agh.bedbooker.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientView {
    @FXML
    private AnchorPane rootAnchorPane;

    protected String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void loadSceneWithEmail(String fxmlFile, String email) {
        try {
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            ClientView controller = fxmlLoader.getController();
            controller.setEmail(email);

            Scene scene = new Scene(root, 960, 640);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
