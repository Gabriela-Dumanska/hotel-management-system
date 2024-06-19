package agh.bedbooker.client;

import agh.bedbooker.AlertHandler;
import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class ClientNewReservationView extends ClientView{
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public Spinner<Integer> minSpinner;
    @FXML
    public Spinner<Integer> maxSpinner;
    @FXML
    public Spinner<Integer> numberOfPlacesSpinner;
    @FXML
    public TableView<Room> tableView;
    @FXML
    public TableColumn<Room, Integer> columnId;
    @FXML
    public TableColumn<Room, Integer> columnNumberOfPlaces;
    @FXML
    public TableColumn<Room, Integer> columnPricePerNight;

    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private boolean successfulReservation = false;

    public void initialize(){
        columnId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnNumberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("numberOfPlaces"));
        columnPricePerNight.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));

        tableView.setItems(rooms);
    }

    @FXML
    public void onSearchButtonClick() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        int numberOfPlaces = numberOfPlacesSpinner.getValue();
        int min = minSpinner.getValue();
        int max = maxSpinner.getValue();

        if(startDate == null || endDate == null){
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Błędna data", "Data początkowa i końcowa nie może być pusta.", "");
        }else if(startDate.isAfter(endDate)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Błędna data", "Data początkowa musi być mniejsza od końcowej.", "");
        }else{
            loadFromDatabase(Date.valueOf(startDate), Date.valueOf(endDate), numberOfPlaces, min, max);
        }
    }

    @FXML
    public void onAddButtonClick() {
        if(getClientID() == -1){
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Logowanie", "Tylko zalogowani użytkownicy mogą dodawać rezerwacje.", "");
        }else{
            onSearchButtonClick();
            if(!rooms.isEmpty()){
                showAddReservationDialog();
                if(successfulReservation){
                    AlertHandler.showAlert(Alert.AlertType.INFORMATION, "Sukces", "Rezerwacja dokonana pomyślnie.", "");
                    onSearchButtonClick();
                    successfulReservation = false;
                }
            }
        }
    }

    private void loadFromDatabase(Date startDate, Date endDate, int numberOfPlaces, int min, int max){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        rooms.clear();

        try {
            connection = DatabaseConnectionManager.getConnection();
            String sql = "{CALL AvailableRooms(?, ?, ?, ?, ?)}";
            statement = connection.prepareStatement(sql);
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            statement.setInt(3, numberOfPlaces);
            statement.setInt(4, min);
            statement.setInt(5, max);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int roomId = resultSet.getInt("RoomID");
                int numberOfPlacesSpinner = resultSet.getInt("NumberOfPlaces");
                int pricePerNight = resultSet.getInt("Price");

                Room room = new Room(roomId, numberOfPlacesSpinner, pricePerNight);
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

    private void showAddReservationDialog(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setWidth(500);
        dialog.setTitle("Dodaj rezerwację");
        dialog.setHeaderText("Podaj ID pokoju:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(roomIDString->{
            try{
                int roomID = Integer.parseInt(roomIDString);
                addReservation(roomID);
                successfulReservation = true;
            } catch(NumberFormatException ex){
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Niepoprawne dane", "Upewnij się, że wpisujesz poprawny identyfikator pokoju.", "");
            }
        });

    }

    private void addReservation(int roomID){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.prepareCall("{CALL AddReservation(?,?,?,?)}");

            statement.setInt(1, getClientID());
            statement.setInt(2, roomID);
            statement.setDate(3, Date.valueOf(startDatePicker.getValue()));
            statement.setDate(4, Date.valueOf(endDatePicker.getValue()));
            statement.execute();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            if ("45000".equals(sqlState)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd rezerwacji", "Nie jest możliwe dokonanie rezerwacji.", "");
            } else {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Wystąpił błąd podczas dodawania rezerwacji", "SQL State: " + sqlState + "\nError Code: " + errorCode);
            }
        }
    }
}
