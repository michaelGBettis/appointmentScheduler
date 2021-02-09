/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author Myque
 */
public class AddCustomerScreenController implements Initializable {

    Stage stage;
    Parent scene;

    Date today = new Date();
    Timestamp timestamp = new Timestamp(today.getTime());

    @FXML
    private AnchorPane addCustomerPane;
    @FXML
    private TextField customerNameBox;
    @FXML
    private TextField addressBox;
    @FXML
    private TextField address2Box;
    @FXML
    private TextField postalCodeBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField phoneBox;
    @FXML
    private ComboBox<String> cityComboBox;
    @FXML
    private ComboBox<String> countryComboBox;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and checks the User table for the values 
            //entered in the username and password textboxes
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
    private void addressBoxHandeler(ActionEvent event) {
    }

    @FXML
    private void address2BoxHandler(ActionEvent event) {
    }

    @FXML
    private void postalCodeBoxHandler(ActionEvent event) {
    }

    @FXML
    private void saveButtonHandler(ActionEvent event) throws Exception {

        //Converting Textbox text to strings and setting them to values
        int addressID = 0;
        String name = customerNameBox.getText();
        String address = addressBox.getText();
        String phone = phoneBox.getText();
        String address2 = address2Box.getText();
        String postalCode = postalCodeBox.getText();

        if (isEmpty() == true) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Excption");
            alert.setContentText("Please enter a valid value in each of the text fields.");
            alert.showAndWait();

        } else {

            //opnes a new connection
            try (Connection conn = DBConnection.startConnection()) {

                //Inserts into the address table values from the textBoxes 
                try (PreparedStatement statement = conn.prepareStatement("INSERT INTO "
                        + "U04252.address(ADDRESS, ADDRESS2, CITYID, POSTALCODE, PHONE, "
                        + "CREATEDATE, CREATEDBY, LASTUPDATE, LASTUPDATEBY) "
                        + "VALUES(?,?,?,?,?,?,?,?,?)")) {

                    //sets the values of the wildcards characters to the entered values 
                    statement.setString(1, address);
                    statement.setString(2, address2);
                    statement.setInt(3, getCityID());
                    statement.setString(4, postalCode);
                    statement.setString(5, phone);
                    statement.setString(6, timestamp.toString());
                    statement.setString(7, LoginScreenController.getCurrentUser());
                    statement.setString(8, timestamp.toString());
                    statement.setString(9, LoginScreenController.getCurrentUser());
                    statement.execute();

                    //get the addressID of the newly inserted address 
                    ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID()");
                    result.next();
                    addressID = result.getInt(1);

                } catch (SQLException ex) {

                    Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);

                }

                //calls the checkCustomer moethod and if the value returns true deletes
                //the last inserted address from the address table
                if (isCustomer(name) == true) {

                    try (PreparedStatement statement = conn.prepareStatement("DELETE FROM U04252.address WHERE ADDRESSID = ?")) {

                        statement.setInt(1, addressID);
                        statement.execute();

                    } catch (SQLException ex) {
                        Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    throw new IllegalArgumentException();

                } else {

                    //Inserts into the customer table values from the textBoxes and the created value for addressID 
                    try (PreparedStatement statement = conn.prepareStatement("INSERT INTO "
                            + "U04252.customer(CUSTOMERNAME, ADDRESSID, ACTIVE, CREATEDATE, CREATEDBY, LASTUPDATE,"
                            + " LASTUPDATEBY) VALUES(?,?,?,?,?,?,?)")) {

                        statement.setString(1, name);
                        statement.setInt(2, addressID);
                        statement.setInt(3, 1);
                        statement.setString(4, timestamp.toString());
                        statement.setString(5, LoginScreenController.getCurrentUser());
                        statement.setString(6, timestamp.toString());
                        statement.setString(7, LoginScreenController.getCurrentUser());
                        statement.executeUpdate();

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();

                    } catch (SQLException ex) {
                        Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

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

    @FXML
    private void phoneBoxHandeler(ActionEvent event
    ) {
    }

    @FXML
    private void cityComboBoxHandler(ActionEvent event
    ) {
    }

    @FXML
    private void countryComboBoxHandler(ActionEvent event
    ) {

        //clears the cityComboBox and repopulates the values with the correct 
        //cities for the selected country
        cityComboBox.getItems().removeAll(cityComboBox.getItems());
        getCitys(getCountryID());
        DBConnection.closeConnection();

    }

    public boolean isCustomer(String Name) throws SQLException {

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and checks the customer table for the value 
            //entered in the customer name textbox
            String sql = ("SELECT UPPER(REPLACE(customerName,' ','')) AS custName FROM U04252.customer HAVING custName = UPPER(REPLACE(?,' ',''))");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, Name);
            ResultSet result = statement.executeQuery();

            //If a match between the value in the customer table and the value entered
            //in the customer name textbox is found then the method returns true and 
            //gives an error 
            if (result.next()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Exsiting Customer Error");
                alert.setContentText("The Customer " + Name + " already exisit, "
                        + "please enter a different name.");
                alert.showAndWait();
                return true;

            } else {

                return false;
            }

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
            String sql = ("Select city.cityId from U04252.city where city.countryId = ? and city.city like ?");
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

    public boolean isEmpty() {

        return customerNameBox.getText().equals("") || addressBox.getText().equals("")
                || phoneBox.getText().equals("") || postalCodeBox.getText().equals("");

    }

}
