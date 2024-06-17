package agh.bedbooker.admin;

import agh.bedbooker.DatabaseConnectionManager;
import agh.bedbooker.database.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminClientsViewController extends AdminView {
    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, Integer> columnId;

    @FXML
    private TableColumn<Person, String> columnName;

    @FXML
    private TableColumn<Person, String> columnSurname;

    @FXML
    private TableColumn<Person, String> columnStreetAddress;

    @FXML
    private TableColumn<Person, String> columnCity;

    @FXML
    private TableColumn<Person, String> columnCountry;
    @FXML
    private TableColumn<Person, String> columnEmail;
    @FXML
    private TableColumn<Person, String> columnPhoneNumber;
    @FXML
    private TableColumn<Person, String> columnBanned;
    @FXML
    private TableColumn<Person, String> columnRegular;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField clientsSurname;

    private final ObservableList<Person> persons = FXCollections.observableArrayList();


    public void initialize() {
        loadDataFromDatabase();

        columnId.setCellValueFactory(new PropertyValueFactory<>("personID"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        columnStreetAddress.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        columnBanned.setCellValueFactory(new PropertyValueFactory<>("isBanned"));
        columnRegular.setCellValueFactory(new PropertyValueFactory<>("isRegular"));

        tableView.setItems(persons);

        ObservableList<String> options = FXCollections.observableArrayList(
                "Wszyscy",
                "Nieproszeni",
                "Stali Klienci",
                "Tylko z Polski"
        );
        filterComboBox.setItems(options);
    }

    @FXML
    private void loadDataFromDatabase() {
        persons.clear();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            statement = connection.createStatement();
            String clientsSurnameText = clientsSurname.getText();
            String selectedFilter = filterComboBox.getValue();
            StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");

            if (clientsSurnameText != null && !clientsSurnameText.isEmpty()) {
                whereClause.append("AND Surname LIKE '").append(clientsSurnameText).append("%' ");
            }
            if (selectedFilter != null) {
                switch (selectedFilter) {
                    case "Nieproszeni":
                        whereClause.append("AND IsBanned = 'True' ");
                        break;
                    case "Stali Klienci":
                        whereClause.append("AND IsRegular = 'True' ");
                        break;
                    case "Tylko z Polski":
                        whereClause.append("AND Country = 'Polska' ");
                        break;
                    default:
                        break;
                }
            }

            resultSet = statement.executeQuery("SELECT * FROM CustomerFullInfo" + whereClause);

            while (resultSet.next()) {
                int personId = resultSet.getInt("PersonID");
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");
                String streetAddress = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String email = resultSet.getString("Email");
                String phoneNumber = resultSet.getString("PhoneNumber");
                Boolean isBanned = resultSet.getBoolean("IsBanned");
                Boolean isRegular = resultSet.getBoolean("IsRegular");

                Person person = new Person(personId, name, surname, streetAddress, city, country, email, phoneNumber,
                        isBanned, isRegular);
                persons.add(person);
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
    private void applyFilter() {
        String filter = filterComboBox.getValue();
        if (filter == null || filter.equals("all")) {
            tableView.setItems(persons);
        } else {
            ObservableList<Person> filteredList = FXCollections.observableArrayList();
            switch (filter) {
                case "Nieproszeni":
                    for (Person person : persons) {
                        if (person.isBanned()) {
                            filteredList.add(person);
                        }
                    }
                    break;
                case "Stali Klienci":
                    for (Person person : persons) {
                        if (person.isRegular()) {
                            filteredList.add(person);
                        }
                    }
                    break;
                case "Tylko z Polski":
                    for (Person person : persons) {
                        if ("Polska".equals(person.getCountry())) {
                            filteredList.add(person);
                        }
                    }
                    break;
                case "Wszyscy":
                    filteredList.addAll(persons);
                    break;
            }
            tableView.setItems(filteredList);
        }
    }
}
