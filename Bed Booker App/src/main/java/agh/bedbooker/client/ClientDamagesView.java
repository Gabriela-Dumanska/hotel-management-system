package agh.bedbooker.client;

import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Damage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ClientDamagesView extends ClientView {
    @FXML
    public TableView<Damage> tableView;
    @FXML
    public TableColumn<Damage, Integer> damageID;
    @FXML
    public TableColumn<Damage, Integer> reservationID;
    @FXML
    public TableColumn<Damage, String> columnDate;
    @FXML
    public TableColumn<Damage, Integer> columnPrice;
    private final ObservableList<Damage> damages = FXCollections.observableArrayList();

    public void initialize() {
        if(getClientID() != -1){
            loadDataFromDatabase();
        }

        damageID.setCellValueFactory(new PropertyValueFactory<>("damageID"));
        reservationID.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(damages);
    }

    private void loadDataFromDatabase() {
        damages.clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            String sql = "{CALL GetDamagesForPerson(?)}";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, getClientID());
            resultSet = statement.executeQuery();

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
}
