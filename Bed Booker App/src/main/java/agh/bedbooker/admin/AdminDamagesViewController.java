package agh.bedbooker.admin;

import agh.bedbooker.AlertHandler;
import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Damage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class AdminDamagesViewController extends AdminView {

    @FXML
    private TableView<Damage> tableView;

    @FXML
    private TableColumn<Damage, Integer> damageID;

    @FXML
    private TableColumn<Damage, Integer> reservationID;

    @FXML
    private TableColumn<Damage, String> columnDate;
    @FXML
    private TableColumn<Damage, Integer> columnPrice;

    private final ObservableList<Damage> damages = FXCollections.observableArrayList();


    public void initialize() {
        loadDataFromDatabase();

        damageID.setCellValueFactory(new PropertyValueFactory<>("damageID"));
        reservationID.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(damages);
    }

    @FXML
    private void loadDataFromDatabase() {
        damages.clear();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Damages");

            while (resultSet.next()) {
                int damageID = resultSet.getInt("DamageID");
                int reservationID = resultSet.getInt("ReservationID");
                String date = resultSet.getString("Date");
                int price = resultSet.getInt("Price");

                Damage damage = new Damage(damageID, reservationID, date, price);
                damages.add(damage);
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
    private void showAddDamageDialog() {
        Stage dialog = new Stage();
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));


        Label reservationLabel = new Label("ID Rezerwacji:");
        TextField reservationField = new TextField();

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Wybierz datę szkody:");

        Label priceLabel = new Label("Wartość:");
        TextField priceField = new TextField();

        Button addButton = new Button("Dodaj szkodę");
        addButton.setOnAction(e -> {
            try {
                int selectedReservationID = Integer.parseInt(reservationField.getText());
                LocalDate selectedDate = datePicker.getValue();
                int selectedPrice = Integer.parseInt(priceField.getText());

                addDamage(selectedReservationID, selectedDate, selectedPrice);
                dialog.close();
            } catch (NumberFormatException ex) {
                System.err.println("Wprowadzono niepoprawne dane. Upewnij się, że ID rezerwacji i cena są liczbami.");
            }
        });

        dialogVBox.getChildren().addAll(
                reservationLabel, reservationField, datePicker, priceLabel, priceField, addButton
        );

        Scene dialogScene = new Scene(dialogVBox, 500, 250);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addDamage(int reservationID, LocalDate date, int price) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseConnectionManager.getConnection();
            callableStatement = connection.prepareCall("{CALL AddDamage(?, ?, ?)}");

            callableStatement.setInt(1, reservationID);
            callableStatement.setDate(2, Date.valueOf(date));
            callableStatement.setInt(3, price);
            callableStatement.execute();

        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            if ("45000".equals(sqlState)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd rezerwacji", "Nie istnieje rezerwacja o podanym ID", "");
            } else if ("45001".equals(sqlState)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd daty", "Data szkody musi być pomiędzy początkiem i końcem pobytu", "");
            } else {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Wystąpił błąd podczas dodawania szkody", "SQL State: " + sqlState + "\nError Code: " + errorCode);
            }
        }
    }
}
