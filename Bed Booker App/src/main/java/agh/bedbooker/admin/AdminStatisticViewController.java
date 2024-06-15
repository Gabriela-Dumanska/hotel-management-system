package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminStatisticViewController extends AdminView {

    @FXML
    private PieChart roomsOccupiedPieChart;
    private int numberOfOccupiedRooms;
    private int numberOfRooms;


    public void initialize() {
        loadDataFromDatabase();

        roomsOccupiedPieChart.getData().add(new PieChart.Data("Zajęte", numberOfOccupiedRooms));
        roomsOccupiedPieChart.getData().add(new PieChart.Data("Wolne",
                                        numberOfRooms - numberOfOccupiedRooms));
    }

    private void loadDataFromDatabase() {
        Connection connection = null;
        Statement statement = null;
        ResultSet occupiedRooms = null;
        ResultSet allRooms = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();

            occupiedRooms = statement.executeQuery("SELECT * FROM NumberOfOccupiedRooms");
            if (occupiedRooms.next()) {
                numberOfOccupiedRooms = occupiedRooms.getInt("OccupiedRoomCount");
            }

            allRooms = statement.executeQuery("SELECT * FROM NumberOfRooms");
            if (allRooms.next()) {
                numberOfRooms = allRooms.getInt("RoomCount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (occupiedRooms != null) occupiedRooms.close();
                if (allRooms != null) allRooms.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
