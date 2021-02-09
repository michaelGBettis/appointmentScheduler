/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

import Model.Appointment;
import Model.DataProvider;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class UpdateAppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;

    Date today = new Date();
    Timestamp timestamp = new Timestamp(today.getTime());
    int appointmentID;

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

    public void recieveAIT(Appointment appointment) {

        //Takes the data passed in from the selected appointment on the main screen,
        //breaks down the date and populates the fields on the UpdateAppointmentScreen
        String startDate = appointment.getCustStart().toString();
        String endDate = appointment.getEnd().toString();
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(5, 7));
        int startDay = Integer.parseInt(startDate.substring(8, 10));
        int startMinutes = (Integer.parseInt(startDate.substring(11, 13)) * 60)
                + Integer.parseInt(startDate.substring(14, 16));
        int endMinutes = (Integer.parseInt(endDate.substring(11, 13)) * 60)
                + Integer.parseInt(endDate.substring(14, 16));
        
        appointmentID = appointment.getAppointmentId();
        datePickerBox.setValue(LocalDate.of(startYear, startMonth, startDay));
        hourComboBox.setValue(String.valueOf(startDate.substring(11, 13)));
        minuteComboBox.setValue(String.valueOf(startDate.substring(14, 16)));
        typeComboBox.setValue(String.valueOf(appointment.getType()));
        locationComboBox.setValue(String.valueOf(appointment.getLocation()));
        durationComboBox.setValue(String.valueOf(endMinutes - startMinutes));
        customerNameBox.setText(String.valueOf(appointment.getCustomerName()));

        

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Populates the drop down menues 
        type.addAll("Forgien Language Speaking", "Friendly Chat", "Presentation Prep",
                "Public Speaking", "Singing Practice");
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
    private void saveButtonHandler(ActionEvent event) throws IOException {

        //Gets the date and time and sets them to local variables, then 
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

            //Trys to open a new connection and insert the selected values into the
            //appointment table
            try (Connection conn = DBConnection.startConnection()) {

                //Checks the typeComboBox for a value, if one is found inserts the 
                //new value into the appointment table 
                if (typeComboBox.getValue().isEmpty() == false) {

                    try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                            + "U04252.appointment SET type = ?, lastUpdate = ?, lastUpdateBy"
                            + " = ? WHERE appointmentId = ?")) {

                        statement.setString(1, typeComboBox.getValue());
                        statement.setString(2, timestamp.toString());
                        statement.setString(3, LoginScreenController.getCurrentUser());
                        statement.setInt(4, appointmentID);
                        statement.executeUpdate();

                    }
                }

                //Checks the locationComboBox for a value, if one is found inserts the 
                //new value into the appointment table 
                if (locationComboBox.getValue().isEmpty() == false) {

                    try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                            + "U04252.appointment SET location = ?, lastUpdate = ?, lastUpdateBy"
                            + " = ? WHERE appointmentId = ?")) {

                        statement.setString(1, locationComboBox.getValue());
                        statement.setString(2, timestamp.toString());
                        statement.setString(3, LoginScreenController.getCurrentUser());
                        statement.setInt(4, appointmentID);
                        statement.executeUpdate();

                    }
                }

                //Checks to see if the location or date have values entered into them, 
                //if so inserts the new values into the appointment table 
                if (null != datePickerBox.getValue() || durationComboBox.getValue().isEmpty()
                        == false || hourComboBox.getValue().isEmpty() == false
                        || minuteComboBox.getValue().isEmpty() == false) {

                    try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                            + "U04252.appointment SET start = ?, end = ?, lastUpdate = ?, lastUpdateBy"
                            + " = ? WHERE appointmentId = ?")) {

                        statement.setString(1, customFormatter.format(locZdt));
                        statement.setString(2, customFormatter.format(locZdt.plusMinutes(parseInt(durationComboBox.getValue()))));
                        statement.setString(3, timestamp.toString());
                        statement.setString(4, LoginScreenController.getCurrentUser());
                        statement.setInt(5, appointmentID);
                        statement.executeUpdate();

                    }
                }

                //catches SQL exceptions
            } catch (SQLException ex) {
                Logger.getLogger(UpdateAppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //returns to the main screen
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

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
}
