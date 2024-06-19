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

    private static String email = null;

    private static int clientID = -1;

    public void setClientID(int clientID) {
        ClientView.clientID = clientID;
    }

    public int getClientID(){
        return clientID;
    }

    public void setEmail(String email) {
        ClientView.email = email;
    }

    public String getEmail(){
        return email;
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

    @FXML
    public void onDataButtonClick(){loadSceneWithEmail("client-personal-data-view.fxml", email);}

    @FXML
    public void onNewReservationButtonClick(){loadSceneWithEmail("client-new-reservation-view.fxml", email);}
    @FXML
    public void onReservationHistoryButtonClick(){loadSceneWithEmail("client-reservation-history-view.fxml", email);}

    @FXML
    public void onDamagesButtonClick(){loadSceneWithEmail("client-damages-view.fxml", email);}
}
