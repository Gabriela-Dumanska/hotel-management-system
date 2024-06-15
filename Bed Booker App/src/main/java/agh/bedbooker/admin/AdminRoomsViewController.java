package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    private void loadDataFromDatabase() {
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
}
