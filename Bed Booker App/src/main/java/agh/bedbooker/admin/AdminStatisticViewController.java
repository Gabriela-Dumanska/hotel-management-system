package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Line;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminStatisticViewController extends AdminView {

    @FXML
    private PieChart roomsOccupiedPieChart;
    @FXML
    private LineChart earningsChart;
    @FXML
    private PieChart damagesPerReservationsPieChart;
    @FXML
    private LineChart sumOfDamagesChart;
    private int numberOfOccupiedRooms;
    private int numberOfRooms;
    private int numberOfReservationsWithDamages;
    private int numberOfReservations;
    private XYChart.Series<String, Number> earningsSeries;
    private XYChart.Series<String, Number> damagesSeries;


    public void initialize() {
        loadDataFromDatabase();

        addPieChartData(roomsOccupiedPieChart, "Zajęte", numberOfOccupiedRooms);
        addPieChartData(roomsOccupiedPieChart, "Wolne", numberOfRooms - numberOfOccupiedRooms);
        earningsChart.getData().add(earningsSeries);
        addPieChartData(damagesPerReservationsPieChart, "Rezerwacje zakończone szkodą", numberOfReservationsWithDamages);
        addPieChartData(damagesPerReservationsPieChart, "Pozostałe", numberOfReservations - numberOfReservationsWithDamages);
        sumOfDamagesChart.getData().add(damagesSeries);
    }

    private void loadDataFromDatabase() {
        Connection connection = null;
        Statement statement = null;
        ResultSet occupiedRooms = null;
        ResultSet allRooms = null;
        ResultSet earnings = null;
        ResultSet damagesSum = null;
        ResultSet damagesAndReservationsCount = null;

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

            earnings = statement.executeQuery("SELECT * FROM Earnings ORDER BY Miesiąc");
            earningsSeries = new XYChart.Series<>();
            earningsSeries.setName("Suma opłaconych rezerwacji w danym miesiącu");

            while (earnings.next()) {
                String month = getMonthName(earnings.getInt("Miesiąc"));
                Number value = earnings.getInt("Zarobki");
                earningsSeries.getData().add(new XYChart.Data<>(month, value));
            }

            damagesAndReservationsCount = statement.executeQuery("SELECT * FROM DamagesPerReservations");
            if (damagesAndReservationsCount.next()) {
                numberOfReservationsWithDamages = damagesAndReservationsCount.getInt("ReservationsWithDamages");
                numberOfReservations = damagesAndReservationsCount.getInt("UniqueReservations");
            }

            damagesSum = statement.executeQuery("SELECT * FROM SumOfDamages ORDER BY Miesiąc");
            damagesSeries = new XYChart.Series<>();
            damagesSeries.setName("Suma poniesionych szkód w danym miesiącu");

            while (damagesSum.next()) {
                String month = getMonthName(damagesSum.getInt("Miesiąc"));
                Number value = damagesSum.getInt("Szkody");
                damagesSeries.getData().add(new XYChart.Data<>(month, value));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (occupiedRooms != null) occupiedRooms.close();
                if (allRooms != null) allRooms.close();
                if (earnings != null) earnings.close();
                if (damagesAndReservationsCount != null) damagesAndReservationsCount.close();
                if (damagesSum != null) damagesSum.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPieChartData(PieChart chart, String label, int value) {
        chart.getData().add(new PieChart.Data(label + " - " + value, value));
    }

    public String getMonthName(int monthNumber) {
        String[] months = {
                "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec",
                "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"
        };

        if (monthNumber >= 1 && monthNumber <= 12) {
            return months[monthNumber - 1];
        } else {
            return "Nieprawidłowy numer miesiąca";
        }
    }
}
