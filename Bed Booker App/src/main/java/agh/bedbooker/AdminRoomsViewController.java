package agh.bedbooker;

import agh.bedbooker.database.Room;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminRoomsViewController {

    @FXML
    private ListView<Room> listView;

    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7711060";
    private static final String DB_USER = "sql7711060";
    private static final String DB_PASSWORD = "UR4SRQ2k1P";

    public void initialize() {
        ObservableList<Room> items = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Rooms");

            while (resultSet.next()) {
                int roomId = resultSet.getInt("RoomID");
                int numberOfPlaces = resultSet.getInt("NumberOfPlaces");
                int price = resultSet.getInt("Price");

                Room room = new Room(roomId, numberOfPlaces, price);
                items.add(room);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setItems(items);
    }
}
