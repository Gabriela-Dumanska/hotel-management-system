package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Room;
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

public class AdminRoomsViewController extends AdminView {

    @FXML
    private TableView<Room> tableView;

    @FXML
    private TableColumn<Room, Integer> columnId;

    @FXML
    private TableColumn<Room, Integer> columnNumberOfPlaces;

    @FXML
    private TableColumn<Room, Integer> columnPricePerNight;

    private final ObservableList<Room> rooms = FXCollections.observableArrayList();


    public void initialize() {
        loadDataFromDatabase();

        columnId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnNumberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("numberOfPlaces"));
        columnPricePerNight.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));

        tableView.setItems(rooms);
    }

    @FXML
    private void loadDataFromDatabase() {
        rooms.clear();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Rooms");

            while (resultSet.next()) {
                int roomId = resultSet.getInt("RoomID");
                int numberOfPlaces = resultSet.getInt("NumberOfPlaces");
                int pricePerNight = resultSet.getInt("Price");

                Room room = new Room(roomId, numberOfPlaces, pricePerNight);
                rooms.add(room);
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
    private void showAddRoomDialog() {
        Stage dialog = new Stage();
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));

        Label numberOfPlacesLabel = new Label("Liczba miejsc:");
        TextField numberOfPlacesField = new TextField();

        Label pricePerNightLabel = new Label("Cena za noc:");
        TextField pricePerNightField = new TextField();

        Button addButton = new Button("Dodaj pokój");
        addButton.setOnAction(e -> {
            int numberOfPlaces = Integer.parseInt(numberOfPlacesField.getText());
            int pricePerNight = Integer.parseInt(pricePerNightField.getText());
            addRoom(numberOfPlaces, pricePerNight);
            dialog.close();
        });

        dialogVBox.getChildren().addAll(
                numberOfPlacesLabel, numberOfPlacesField,
                pricePerNightLabel, pricePerNightField,
                addButton
        );

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addRoom(int numberOfPlaces, int pricePerNight) {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            callableStatement = connection.prepareCall("{CALL AddRoom(?, ?)}");
            callableStatement.setInt(1, numberOfPlaces);
            callableStatement.setInt(2, pricePerNight);
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
