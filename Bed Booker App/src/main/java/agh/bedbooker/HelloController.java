package agh.bedbooker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HelloController {

    @FXML
    private VBox rootVBox;

    @FXML
    protected void onClientButtonClick() {
        try {
            Stage stage = (Stage) rootVBox.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 278, 258);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAdminButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin");
        dialog.setHeaderText("Zaloguj się do panelu admina");
        dialog.setContentText("Hasło:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(password -> {
            if (password.equals("admin")) {
                try {
                    Stage stage = (Stage) rootVBox.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-rooms-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nieprawidłowe hasło", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }
}
