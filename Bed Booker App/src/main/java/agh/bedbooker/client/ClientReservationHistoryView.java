package agh.bedbooker.client;

import agh.bedbooker.AlertHandler;
import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Reservation;
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

public class ClientReservationHistoryView extends ClientView {
    @FXML
    public TableView<Reservation> tableView;
    @FXML
    public TableColumn<Reservation, Integer> reservationID;
    @FXML
    public TableColumn<Reservation, Integer> roomID;
    @FXML
    public TableColumn<Reservation, Integer> numberOfPlaces;
    @FXML
    public TableColumn<Reservation, String> startDate;
    @FXML
    public TableColumn<Reservation, String> endDate;
    @FXML
    public TableColumn<Reservation, Integer> reservationPrice;
    @FXML
    public TableColumn<Reservation, Integer> discount;
    @FXML
    public TableColumn<Reservation, String> status;
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    public void initialize() {
        if(getClientID() != -1){
            loadDataFromDatabase();
        }

        reservationID.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        roomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        numberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("numberOfPlaces"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        reservationPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableView.setItems(reservations);
    }

    private void loadDataFromDatabase() {
        reservations.clear();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ReservationDetails WHERE PersonID = " + getClientID());

            while (resultSet.next()) {
                int reservationID = resultSet.getInt("ReservationID");
                int roomID = resultSet.getInt("RoomID");
                int numberOfPlaces = resultSet.getInt("NumberOfPlaces");
                String startDate = resultSet.getString("StartDate");
                String endDate = resultSet.getString("EndDate");
                int price = resultSet.getInt("ReservationPrice");
                int discount = resultSet.getInt("Discount");
                String status = resultSet.getString("Status");

                Reservation reservation = new Reservation(reservationID, null, null, roomID,
                        numberOfPlaces, 0, startDate, endDate, 0, price, discount);
                reservation.setStatus(status);
                reservations.add(reservation);
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
    private void cancelReservation(){
        showDialog("Odwołaj rezerwację");
    }
    @FXML
    private void payForReservation(){
        showDialog("Zapłać za rezerwację");
    }

    private void showDialog(String title) {
        Stage dialog = new Stage();
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));
        dialog.setTitle(title);

        Label reservationLabel = new Label("Podaj ID Rezerwacji:");
        TextField reservationField = new TextField();

        Button addButton = new Button(title);
        addButton.setOnAction(e -> {
            try {
                int selectedReservationID = Integer.parseInt(reservationField.getText());

                boolean found = false;

                for (Reservation reservation : reservations) {
                    if (reservation.getReservationID() == selectedReservationID) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    if (title.equals("Zapłać za rezerwację")) {
                        payForReservation(selectedReservationID);
                    } else {
                        cancelReservation(selectedReservationID);
                    }
                    dialog.close();
                } else {
                    AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd", "Nie można zarządzać nie swoją rezerwacją!", "");
                }
            } catch (NumberFormatException ex) {
                System.err.println("Wprowadzono niepoprawne dane. Upewnij się, że ID rezerwacji jest liczbą.");
            }
        });

        dialogVBox.getChildren().addAll(
                reservationLabel, reservationField, addButton
        );

        Scene dialogScene = new Scene(dialogVBox, 500, 120);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void payForReservation(int reservationID) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseConnectionManager.getConnection();
            callableStatement = connection.prepareCall("{CALL PayForReservation(?)}");

            callableStatement.setInt(1, reservationID);
            callableStatement.execute();

        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            if ("45000".equals(sqlState)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd", "Nie można zapłacić za odwołaną rezerwację!", "");
            } else {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Wystąpił błąd podczas płacenia", "SQL State: " + sqlState + "\nError Code: " + errorCode);
            }
        }

        loadDataFromDatabase();
    }

    private void cancelReservation(int reservationID) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseConnectionManager.getConnection();
            callableStatement = connection.prepareCall("{CALL CancelReservation(?)}");

            callableStatement.setInt(1, reservationID);
            callableStatement.execute();

        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Wystąpił błąd podczas odwoływania", "SQL State: " + sqlState + "\nError Code: " + errorCode);
        }

        loadDataFromDatabase();
    }
}
