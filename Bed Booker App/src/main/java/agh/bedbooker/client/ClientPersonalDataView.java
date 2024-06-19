package agh.bedbooker.client;

import agh.bedbooker.DatabaseConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.*;

public class ClientPersonalDataView extends ClientView{
    @FXML
    private Label emailLabel;

    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label phoneLabel;

    @Override
    public void setEmail(String email){
        super.setEmail(email);
        emailLabel.setText(email);
        loadFromDatabase();
    }

    private void loadFromDatabase(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DatabaseConnectionManager.getConnection();
            String sql = "SELECT * FROM Persons WHERE Email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                nameLabel.setText(resultSet.getString("Name"));
                surnameLabel.setText(resultSet.getString("Surname"));
                streetLabel.setText(resultSet.getString("StreetAddress"));
                phoneLabel.setText(resultSet.getString("PhoneNumber"));
            }else{
                nameLabel.setText("-");
                surnameLabel.setText("-");
                streetLabel.setText("-");
                phoneLabel.setText("-");
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
