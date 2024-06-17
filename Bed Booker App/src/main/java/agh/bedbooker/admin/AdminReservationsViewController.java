package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AdminReservationsViewController extends AdminView {
    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, Integer> columnReservationID;

    @FXML
    private TableColumn<Reservation, String> columnName;

    @FXML
    private TableColumn<Reservation, String> columnSurname;
    @FXML
    private TableColumn<Reservation, Integer> columnRoomID;
    @FXML
    private TableColumn<Reservation, Integer> columnNumberOfPlaces;
    @FXML
    private TableColumn<Reservation, Integer> columnRoomPricePerNight;
    @FXML
    private TableColumn<Reservation, String> columnStartDate;
    @FXML
    private TableColumn<Reservation, String> columnEndDate;
    @FXML
    private TableColumn<Reservation, Integer> columnNumberOfDays;
    @FXML
    private TableColumn<Reservation, Integer> columnPrice;
    @FXML
    private TableColumn<Reservation, Integer> columnDiscount;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField clientsSurname;

    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();


    public void initialize() {
        loadDataFromDatabase();

        columnReservationID.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        columnRoomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        columnNumberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("numberOfPlaces"));
        columnRoomPricePerNight.setCellValueFactory(new PropertyValueFactory<>("roomPricePerNight"));
        columnStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        columnEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        columnNumberOfDays.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tableView.setItems(reservations);
    }

    @FXML
    private void loadDataFromDatabase() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String clientsSurnameText = null;
        if(clientsSurname!=null) {
            clientsSurnameText = clientsSurname.getText();
        }
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");

        if (startDate != null) {
            whereClause.append("AND StartDate >= '").append(startDate).append("' ");
        }
        if (endDate != null) {
            whereClause.append("AND EndDate <= '").append(endDate).append("' ");
        }
        if (clientsSurnameText != null && !clientsSurnameText.isEmpty()) {
            whereClause.append("AND PersonSurname LIKE '").append(clientsSurnameText).append("%' ");
        }

        reservations.clear();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();

            String query = "SELECT * FROM ReservationDetails" + whereClause;
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int reservationID = resultSet.getInt("ReservationID");
                String name = resultSet.getString("PersonName");
                String surname = resultSet.getString("PersonSurname");
                int roomID = resultSet.getInt("RoomID");
                int numberOfPlaces = resultSet.getInt("NumberOfPlaces");
                int roomPricePerNight = resultSet.getInt("RoomPrice");
                String dbStartDate = resultSet.getString("StartDate");
                String dbEndDate = resultSet.getString("EndDate");
                int numberOfDays = resultSet.getInt("NumberOfDays");
                int price = resultSet.getInt("ReservationPrice");
                int discount = resultSet.getInt("Discount");

                Reservation reservation = new Reservation(reservationID, name, surname, roomID, numberOfPlaces,
                        roomPricePerNight, dbStartDate, dbEndDate, numberOfDays,
                        price, discount);
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

}
