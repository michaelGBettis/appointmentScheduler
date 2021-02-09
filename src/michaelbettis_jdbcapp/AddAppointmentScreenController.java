/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

import Model.DataProvider;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author Myque
 */
public class AddAppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;
    int custID = 0;
    int aptID = 0;

    Date today = new Date();
    Timestamp timestamp = new Timestamp(today.getTime());

    ObservableList<String> type = FXCollections.observableArrayList();
    ObservableList<String> location = FXCollections.observableArrayList();
    ObservableList<String> duration = FXCollections.observableArrayList();
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();

    @FXML
    private AnchorPane MainScreenPane;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private ComboBox<String> durationComboBox;
    @FXML
    private DatePicker datePickerBox;
    @FXML
    private ComboBox<String> hourComboBox;
    @FXML
    private ComboBox<String> minuteComboBox;
    @FXML
    private TextField customerNameBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Adds data to the drop down menues
        type.addAll("Scrum", "Meeting", "Presentation", "Interview");
        location.addAll("Phone", "Skype", "Web", "Other");
        duration.addAll("15", "30", "45", "60");
        hours.addAll("09", "10", "11", "12", "13", "14", "15", "16", "17");
        minutes.addAll("00", "15", "30", "45");

        typeComboBox.setItems(type);
        locationComboBox.setItems(location);
        durationComboBox.setItems(duration);
        hourComboBox.setItems(hours);
        minuteComboBox.setItems(minutes);

        //Lambda expression that checks todays date and diables all dates that 
        //are less than the value of todays date 
        datePickerBox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean isEmpty) {
                super.updateItem(date, isEmpty);
                LocalDate today = LocalDate.now();

                setDisable(isEmpty || date.compareTo(today) < 0);
            }
        });

    }

    @FXML
    private void typeComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void locationComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void durationComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void datePickerBoxHandler(ActionEvent event) {
    }

    @FXML
    private void hourComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void minuteComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void saveButtonHandler(ActionEvent event) throws IOException, SQLException {
        //Calls the isEmpty function and if values are present in all boxes continues 
        //if not throws an error
        if (isEmpty() == true || isCustomer(customerNameBox.getText()) < 0) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Excption");
            alert.setContentText("Please enter a valid value in each of the text fields.");
            alert.showAndWait();

        } else {

            //If no error is found get the date and time and sets them to local variables, 
            //converts them to to appropraite timezone and formats the output
            LocalDate date = datePickerBox.getValue();
            String hour = hourComboBox.getValue();
            String minute = minuteComboBox.getValue();
            LocalDateTime ldt = DataProvider.createDate(date, hour, minute);
            ZonedDateTime locZdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            ZonedDateTime utcZdt = locZdt.withZoneSameInstant(ZoneOffset.UTC);
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //The isOverlapping function is called to check if the LocalDateTime is 
            //overlapping with any other appointment, if so it throws an error if not it moves on
            if (DataProvider.isOverlapping(ldt,
                    ldt.plusMinutes(parseInt(durationComboBox.getValue()))) == true) {

                throw new IllegalArgumentException();

            } else {

                try (Connection conn = DBConnection.startConnection()) {

                    //Trys to open a new connection and insert the selected values into the
                    //appointment table
                    String sql = ("INSERT INTO U04252.appointment(customerId, userId, "
                            + "location, type, start, end, createDate, createdBy, "
                            + "lastUpdate, lastUpdateBy, title, description, contact, url) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, isCustomer(customerNameBox.getText()));
                    statement.setInt(2, LoginScreenController.getCurrentUserID());
                    statement.setString(3, locationComboBox.getValue());
                    statement.setString(4, typeComboBox.getValue());
                    statement.setString(5, customFormatter.format(locZdt));
                    statement.setString(6, customFormatter.format(locZdt.plusMinutes(parseInt(durationComboBox.getValue()))));
                    statement.setString(7, timestamp.toString());
                    statement.setString(8, LoginScreenController.getCurrentUser());
                    statement.setString(9, timestamp.toString());
                    statement.setString(10, LoginScreenController.getCurrentUser());
                    statement.setString(11, "not needed");
                    statement.setString(12, "not needed");
                    statement.setString(13, "not needed");
                    statement.setString(14, "not needed");
                    statement.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(AddAppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (java.lang.NullPointerException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Null Pointer Exception");
                    alert.setContentText("Please enter a value for each box.");
                    alert.showAndWait();

                }

                //moves back to the main screen after successful insert
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) throws IOException {

        //Sends an alert before switching back to the mainScreen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "This will clear the text fields and take you back to the main "
                + "screen, do you want to continue? ");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }

    public boolean isEmpty() {
        //Checks to see if values are entered in each of the boxes
        return locationComboBox.getValue() == null || typeComboBox.getValue() == null
                || durationComboBox.getValue() == null || minuteComboBox.getValue() == null
                || hourComboBox.getValue() == null || datePickerBox.getValue() == null;
    }

    public int isCustomer(String Name) throws SQLException {

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and checks the customer table for the value 
            //entered in the customer name textbox
            String sql = ("SELECT UPPER(REPLACE(customerName,' ','')) AS custName, customerId FROM U04252.customer HAVING custName = UPPER(REPLACE(?,' ',''))");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, Name);
            ResultSet result = statement.executeQuery();

            //If a match between the value in the customer table and the value entered
            //in the customer name textbox is found then the method returns true and 
            //gives an error 
            if (result.next()) {

                custID = result.getInt(2);

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Non-Exsiting Customer Error");
                alert.setContentText("The Customer " + Name + " does not exisit, "
                        + "please enter an existing customer or check spelling.");
                alert.showAndWait();
            }

        }
        return custID;
    }
}
