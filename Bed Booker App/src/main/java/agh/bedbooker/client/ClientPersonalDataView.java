package agh.bedbooker.client;

import agh.bedbooker.AlertHandler;
import agh.bedbooker.DatabaseConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ClientPersonalDataView extends ClientView{
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ImageView picture;

    @Override
    public void setEmail(String email){
        super.setEmail(email);
        emailLabel.setText(email);
        loadDataFromDatabase();
    }

    @FXML
    private void loadDataFromDatabase(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DatabaseConnectionManager.getConnection();
            String sql = "SELECT * FROM CustomerFullInfo WHERE Email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, getEmail());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                addButton.setVisible(false);
                refreshButton.setVisible(false);
                setClientID(resultSet.getInt("PersonID"));
                String name = resultSet.getString("Name");
                nameLabel.setText(name);
                surnameLabel.setText(resultSet.getString("Surname"));
                streetLabel.setText(resultSet.getString("Address"));
                cityLabel.setText(resultSet.getString("City"));
                countryLabel.setText(resultSet.getString("Country"));
                phoneLabel.setText(resultSet.getString("PhoneNumber"));

                if (name != null && !name.isEmpty()) {
                    char lastChar = name.charAt(name.length() - 1);
                    if (lastChar == 'a' || lastChar == 'A') {
                        picture.setImage(new Image(getClass().getResource("/women.png").toExternalForm()));
                    } else {
                        picture.setImage(new Image(getClass().getResource("/men.png").toExternalForm()));
                    }
                }
            } else {
                addButton.setVisible(true);
                refreshButton.setVisible(true);
                picture.setImage(new Image(getClass().getResource("/questionmark.png").toExternalForm()));
                AlertHandler.showAlert(Alert.AlertType.INFORMATION, "Informacja", "Zarejestruj się aby w pełni korzytsać systemu", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void showAddCustomerDialog() {
        Stage dialog = new Stage();
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));
        dialog.setTitle("Rejestracja");

        Label info = new Label("Dodaj swoje dane, żeby stać się klientem hotelu!");
        Label name = new Label("Imię:");
        TextField nameField = new TextField();

        Label surname = new Label("Nazwisko:");
        TextField surnameField = new TextField();

        Label street = new Label("Ulica:");
        TextField streetField = new TextField();

        Label city = new Label("Miasto:");
        TextField cityField = new TextField();

        Label country = new Label("Kraj:");
        TextField countryField = new TextField();

        Label phone = new Label("Numer telefonu:");
        TextField phoneField = new TextField();

        Button addButton = new Button("Zarejestruj");
        addButton.setOnAction(e -> {
            String customerEmail = emailLabel.getText();
            String customerName = nameField.getText();
            String customerSurname = surnameField.getText();
            String customerStreet = streetField.getText();
            String customerCity = cityField.getText();
            String customerCountry = countryField.getText();
            String customerPhone = phoneField.getText();
            addCustomer(customerName, customerSurname, customerStreet, customerCity, customerCountry, customerPhone, customerEmail);
            dialog.close();
        });

        dialogVBox.getChildren().addAll(
                info, name, nameField, surname, surnameField, street, streetField, city, cityField, country, countryField,
                phone, phoneField, addButton
        );

        Scene dialogScene = new Scene(dialogVBox, 300, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addCustomer(String name, String surname, String street, String city, String country, String phone, String email) {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            callableStatement = connection.prepareCall("{CALL AddCustomer(?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString(1, name);
            callableStatement.setString(2, surname);
            callableStatement.setString(3, street);
            callableStatement.setString(4, city);
            callableStatement.setString(5, country);
            callableStatement.setString(6, phone);
            callableStatement.setString(7, email);
            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
