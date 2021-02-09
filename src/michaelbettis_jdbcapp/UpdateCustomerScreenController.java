/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author Myque
 */
public class UpdateCustomerScreenController implements Initializable {

    Stage stage;
    Parent scene;

    Date today = new Date();
    Timestamp timestamp = new Timestamp(today.getTime());

    int customerID;
    int addressID;
    int active;

    @FXML
    private AnchorPane addCustomerPane;
    @FXML
    private TextField customerNameBox;
    @FXML
    private TextField phoneBox;
    @FXML
    private TextField addressBox;
    @FXML
    private TextField address2Box;
    @FXML
    private TextField postalCodeBox;
    @FXML
    private ComboBox<String> cityComboBox;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton activeRadioButton;
    @FXML
    private ToggleGroup userStatus;
    @FXML
    private RadioButton inactiveRadioButton;

    public void recieveCIT(Customer customer) {

        //Takes the data passed in from the selected customer on the main screen
        //and populates the fields on the UpdateCustomerScreen
        customerNameBox.setText(String.valueOf(customer.getCustomerName()));
        phoneBox.setText(String.valueOf(customer.getPhone()));
        addressBox.setText(String.valueOf(customer.getAddress()));
        address2Box.setText(String.valueOf(customer.getAddress2()));
        postalCodeBox.setText(String.valueOf(customer.getPostalCode()));
        customerID = customer.getCustomerID();
        addressID = customer.getAddressID();
        active = customer.getActive();
        if (active == 1) {
            activeRadioButton.fire();
        } else {
            inactiveRadioButton.fire();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and sets the city drop down based on the 
            //country that was passed in
            String sql = ("SELECT * FROM U04252.country ORDER BY 2");
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String name = result.getString("country");
                countryComboBox.getItems().addAll(name);

            }

            DBConnection.closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void customerNameBoxHandler(ActionEvent event) {
    }

    @FXML
    private void phoneBoxHandeler(ActionEvent event) {
    }

    @FXML
    private void addressBoxHandeler(ActionEvent event) {
    }

    @FXML
    private void address2BoxHandler(ActionEvent event) {
    }

    @FXML
    private void postalCodeBoxHandler(ActionEvent event) {
    }

    @FXML
    private void cityComboBoxHandler(ActionEvent event) {
    }

    @FXML
    private void activeRadioButtonHandler(ActionEvent event) {

        //sets active = to 1 on click
        active = 1;

    }

    @FXML
    private void inactiveRadioButtonHandler(ActionEvent event) {

        //sets active = to 0 on click
        active = 0;

    }

    @FXML
    private void countryComboBoxHandler(ActionEvent event) {

        //clears the cityComboBox and repopulates the values with the correct 
        //cities for the selected country
        cityComboBox.getItems().removeAll(cityComboBox.getItems());
        getCitys(getCountryID());
        DBConnection.closeConnection();

    }

    @FXML
    private void saveButtonHandler(ActionEvent event) throws IOException {

        //Sets the values of the fields to variables 
        String name = customerNameBox.getText();
        String phone = phoneBox.getText();
        String address = addressBox.getText();
        String address2 = address2Box.getText();
        String postalCode = postalCodeBox.getText();

        //Trys to open a new connection and insert the selected values into the
        //customer table
        try (Connection conn = DBConnection.startConnection()) {

            //Checks the customerNameBox for a value, if one is found updates the 
            //new value into the appointment table
            if (customerNameBox.getText().isEmpty() == false) {

                try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                        + "U04252.customer SET customerName = ?, lastUpdate = ?, "
                        + "lastUpdateBy = ? WHERE customerId = ?")) {

                    statement.setString(1, name);
                    statement.setString(2, timestamp.toString());
                    statement.setString(3, LoginScreenController.getCurrentUser());
                    statement.setInt(4, customerID);
                    statement.executeUpdate();

                }

            }
            if (phoneBox.getText().isEmpty() == false) {

                try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                        + "U04252.address SET phone = ?, lastUpdate = ?,"
                        + " lastUpdateBy = ? WHERE addressId = ?")) {

                    statement.setString(1, phone);
                    statement.setString(2, timestamp.toString());
                    statement.setString(3, LoginScreenController.getCurrentUser());
                    statement.setInt(4, addressID);
                    statement.executeUpdate();

                }
            }
            if (addressBox.getText().isEmpty() == false) {

                try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                        + "U04252.address SET address = ?, lastUpdate = ?,"
                        + " lastUpdateBy = ? WHERE addressId = ?")) {

                    statement.setString(1, address);
                    statement.setString(2, timestamp.toString());
                    statement.setString(3, LoginScreenController.getCurrentUser());
                    statement.setInt(4, addressID);
                    statement.executeUpdate();

                }
            }
            if (address2Box.getText().isEmpty() == false) {

                try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                        + "U04252.address SET address2 = ?, lastUpdate = ?,"
                        + " lastUpdateBy = ? WHERE addressId = ?")) {

                    statement.setString(1, address2);
                    statement.setString(2, timestamp.toString());
                    statement.setString(3, LoginScreenController.getCurrentUser());
                    statement.setInt(4, addressID);
                    statement.executeUpdate();

                }
            }
            if (postalCodeBox.getText().isEmpty() == false) {

                try (PreparedStatement statement = conn.prepareStatement("UPDATE "
                        + "U04252.address SET postalCode = ?, lastUpdate = ?,"
                        + " lastUpdateBy = ? WHERE addressId = ?")) {

                    statement.setString(1, postalCode);
                    statement.setString(2, timestamp.toString());
                    statement.setString(3, LoginScreenController.getCurrentUser());
                    statement.setInt(4, addressID);
                    statement.executeUpdate();

                }
                if (countryComboBox.getValue().isEmpty() == false || cityComboBox.getValue().isEmpty() == false) {

                    try (PreparedStatement statement = conn.prepareStatement("update"
                            + " U04252.address SET cityId = ?, lastUpdate = ?, "
                            + "lastUpdateBy = ? WHERE addressId = ?;")) {

                        statement.setInt(1, getCityID());
                        statement.setString(2, timestamp.toString());
                        statement.setString(3, LoginScreenController.getCurrentUser());
                        statement.setInt(4, addressID);
                        statement.executeUpdate();
                    }

                } else {

                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                }

            }

            try (PreparedStatement statement = conn.prepareStatement("UPDATE U04252.customer "
                    + "SET active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?")) {

                statement.setInt(1, active);
                statement.setString(2, timestamp.toString());
                statement.setString(3, LoginScreenController.getCurrentUser());
                statement.setInt(4, customerID);
                statement.executeUpdate();

            }

        } catch (SQLException ex) {
            Logger.getLogger(UpdateCustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.NullPointerException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please enter a valid value for Country and City");
            alert.showAndWait();
            throw new IllegalArgumentException();
        }

        //switches to the main screen
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

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

    private int getCountryID() {

        int id = 0;

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and returns the country selected in the country combo box
            String sql = ("SELECT countryId FROM U04252.country WHERE country like ?");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, countryComboBox.getSelectionModel().getSelectedItem());
            ResultSet result = statement.executeQuery();

            //returns the id value of the selected country
            while (result.next()) {
                id = result.getInt(1);

            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;

    }

    private int getCityID() {

        int countryid = getCountryID();

        int cityid = 0;

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and returns the country selected in the country combo box
            String sql = ("Select city.cityId from U04252.city where city.countryId"
                    + " = ? and city.city like ?");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, countryid);
            statement.setString(2, cityComboBox.getSelectionModel().getSelectedItem());
            ResultSet result = statement.executeQuery();

            //returns the id value of the selected country
            while (result.next()) {
                cityid = result.getInt(1);

            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cityid;

    }

    private void getCitys(int id) {

        try (Connection conn = DBConnection.startConnection()) {
            //Opens a new connection and checks the city table for the cities with
            //the countryID value that matches the passed in value id

            String sql = ("SELECT * FROM U04252.city WHERE countryId =? ORDER BY 2");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String name = result.getString("city");
                cityComboBox.getItems().addAll(name);
                cityComboBox.setValue(name);

            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
