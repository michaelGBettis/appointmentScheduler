/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author Myque
 */
public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;

    LocalDate now = (LocalDate.now().minusDays(1));
    LocalDate eow = now.plusDays(9);
    LocalDate eom = now.plusMonths(1).plusDays(2);
    Date today = new Date();
    Timestamp timestamp = new Timestamp(today.getTime());

    @FXML
    private Button customerSearchButton;
    @FXML
    private Button customerAddButton;
    @FXML
    private Button customerUpdateButton;
    @FXML
    private Button customerDeleteButton;
    @FXML
    private TextField customerSerchBar;
    @FXML
    private TableView<Customer> customerInfoTable;
    @FXML
    private Button appointmentSearchButton;
    @FXML
    private Button appointmentDeleteButton;
    @FXML
    private Button appointmentUpdateButton;
    @FXML
    private Button appointmentAddButton;
    @FXML
    private TextField appointmentSearchBar;
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane mainScreenPane;
    @FXML
    private AnchorPane customerPane;
    @FXML
    private TableColumn<Customer, String> active;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerAddress2;
    @FXML
    private TableColumn<Customer, String> customerPostalCode;
    @FXML
    private TableColumn<Customer, String> customerPhone;
    @FXML
    private TableColumn<Customer, String> customerCity;
    @FXML
    private TableColumn<Customer, String> customerCountry;
    @FXML
    private AnchorPane appointmentPane;
    @FXML
    private TableView<Appointment> appointmentInfoTable;
    @FXML
    private TableColumn<Appointment, String> customerName1;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, String> appointmentId;
    @FXML
    private TableColumn<Appointment, String> appointmentStart;
    @FXML
    private TableColumn<Appointment, String> appointmentEnd;
    @FXML
    private TableColumn<Appointment, String> createdBy;
    @FXML
    private Button monthViewButton;
    @FXML
    private Button weekViewButton;
    @FXML
    private Button yourView;
    @FXML
    private Button allView;
    @FXML
    private Button typeByMonButton;
    @FXML
    private Button typeByWeekButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Fininsh with the appointment table 
        DataProvider.getAllCustomer().clear();
        DataProvider.getAllAppointment().clear();

        DataProvider.upcomingApt();
        getCustomerInfo();
        getAppointmentInfo();

        //adds the data from the TableRow object to the tableData ObservableList
        customerInfoTable.setItems(DataProvider.getAllCustomer());

        //Tells the columns in customerInfoTable table where to populate its cell values from 
        active.setCellValueFactory(new PropertyValueFactory<>("active"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));

        //adds the data from the TableRow object to the tableData ObservableList
        appointmentInfoTable.setItems(DataProvider.getAllAppointment());

        //Tells the columns in appointmentInfoTable table where to populate its cell values from 
        customerName1.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("custStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

    }

    @FXML
    private void customerSearchButtonHandler(ActionEvent event) {

        if (DataProvider.isStringInteger(customerSerchBar.getText()) == true) {

            int id = Integer.parseInt(customerSerchBar.getText());

            DataProvider.lookupCustomer(id);
            customerInfoTable.getSelectionModel().select(DataProvider.selectCustomer(id));

        } else {

            customerInfoTable.setItems(DataProvider.filterCustomer(customerSerchBar.getText()));

        }

        customerSerchBar.setText("");

    }

    @FXML
    private void customerAddButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("addCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    private void customerUpdateButtonHandler(ActionEvent event) throws IOException {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("updateCustomerScreen.fxml"));
            loader.load();

            UpdateCustomerScreenController UCSController = loader.getController();
            UCSController.recieveCIT(customerInfoTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scenee = loader.getRoot();
            stage.setScene(new Scene(scenee));
            stage.show();

        } catch (java.lang.NullPointerException e) {

            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please select a Customer to update from the Customer Table.");
            alert.showAndWait();

        }

    }

    @FXML
    private void customerDeleteButtonHandler(ActionEvent event) throws IOException {

        if (customerInfoTable.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please select a Customer to delete from the Customer Table.");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "This will delete the selected customer and all appointments "
                    + "associated with the customer, do you want to continue? ");

            Optional<ButtonType> selectedResult = alert.showAndWait();

            if (selectedResult.isPresent() && selectedResult.get() == ButtonType.OK) {

                Customer delete = customerInfoTable.getSelectionModel().getSelectedItem();
                int id = delete.getCustomerID();

                try (Connection conn = DBConnection.startConnection()) {
                    appointmentInfoTable.setItems(DataProvider.filterAppointment(delete.getCustomerName()));
                    Appointment aptDelete = appointmentInfoTable.getSelectionModel().getSelectedItem();
                    int aptID = id;

                    String sql = ("DELETE FROM U04252.appointment WHERE customerId = ?");
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, aptID);
                    statement.executeUpdate();

                    if (statement.getUpdateCount() > 0) {

                        DataProvider.deleteAppointment(aptDelete);

                    }

                } catch (java.lang.NullPointerException e) {

                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Null Pointer Exception");
                    alert.setContentText("Please select an Item to delete from the Appointment Table.");
                    alert.showAndWait();

                } catch (java.util.ConcurrentModificationException e2) {
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                try (Connection conn = DBConnection.startConnection()) {

                    String sql = ("DELETE FROM U04252.customer WHERE customerId = ?");
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, id);
                    statement.executeUpdate();

                    if (statement.getUpdateCount() > 0) {

                        DataProvider.deleteCustomer(delete);

                    }

                } catch (java.lang.NullPointerException e) {

                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Null Pointer Exception");
                    alert.setContentText("Please select an Item to delete from the Customer Table.");
                    alert.showAndWait();

                } catch (java.util.ConcurrentModificationException e2) {
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }

    }

    @FXML
    private void appointmentSearchButtonHandler(ActionEvent event) {

        if (DataProvider.isStringInteger(appointmentSearchBar.getText()) == true) {

            int id = Integer.parseInt(appointmentSearchBar.getText());

            DataProvider.lookupAppointment(id);
            appointmentInfoTable.getSelectionModel().select(DataProvider.selectAppointment(id));

        } else {

            appointmentInfoTable.setItems(DataProvider.filterAppointment(appointmentSearchBar.getText()));

        }

        appointmentSearchBar.setText("");

    }

    @FXML
    private void appointmentDeleteButtonHandler(ActionEvent event) throws IOException {

        if (appointmentInfoTable.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please select an Appointment to delete from the Customer Table.");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "This will delete the selected appointment, do you want to continue? ");

            Optional<ButtonType> selectedResult = alert.showAndWait();

            if (selectedResult.isPresent() && selectedResult.get() == ButtonType.OK) {

                Appointment aptDelete = appointmentInfoTable.getSelectionModel().getSelectedItem();
                int id = aptDelete.getAppointmentId();

                try (Connection conn = DBConnection.startConnection()) {

                    String sql = ("DELETE FROM U04252.appointment WHERE appointmentId = ?");
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, id);
                    statement.executeUpdate();

                    if (statement.getUpdateCount() > 0) {

                        DataProvider.deleteAppointment(aptDelete);

                    }

                } catch (java.lang.NullPointerException e) {

                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Null Pointer Exception");
                    alert.setContentText("Please select an Item to delete from the Customer Table.");
                    alert.showAndWait();

                } catch (java.util.ConcurrentModificationException e2) {
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }

    }

    @FXML
    private void appointmentAddButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("addAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    private void appointmentUpdateButtonHandler(ActionEvent event) throws IOException {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("updateAppointmentScreen.fxml"));
            loader.load();

            UpdateAppointmentScreenController UASController = loader.getController();
            UASController.recieveAIT(appointmentInfoTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scenee = loader.getRoot();
            stage.setScene(new Scene(scenee));
            stage.show();

        } catch (java.lang.NullPointerException e) {

            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please select an Appointment to update from the Appointment Table.");
            alert.showAndWait();

        }

    }

    @FXML
    private void monthViewButtonHandler(ActionEvent event) {

        DataProvider.nextMonthApts.clear();
        setMonth();
        appointmentInfoTable.setItems(DataProvider.nextMonthApts);

    }

    @FXML
    private void weekViewButtonHandler(ActionEvent event) {

        DataProvider.nextWeekApts.clear();
        setWeek();
        appointmentInfoTable.setItems(DataProvider.nextWeekApts);

    }

    @FXML
    private void yourViewButtonHandler(ActionEvent event) {

        DataProvider.yourApts.clear();
        setYourApts();
        appointmentInfoTable.setItems(DataProvider.yourApts);

    }

    @FXML
    private void allViewButtonHandler(ActionEvent event) {

        appointmentInfoTable.setItems(DataProvider.getAllAppointment());

    }

    @FXML
    private void typeByMonButtonHandler(ActionEvent event) {

        ArrayList<String> Info = new ArrayList();

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and inserts the selected values into the
            //appointment table
            String sql = ("SELECT type, Count(*) FROM U04252.appointment WHERE (start BETWEEN ? AND ?) GROUP BY type");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, now.toString());
            statement.setString(2, eom.toString());
            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Info.add(result.getString(1) + ": " + result.getString(2));

            }

        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String list = Arrays.toString(Info.toArray()).replace(",", "\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Appoinment types by month");
        alert.setContentText("Type:  Amount\n" + list.substring(1, list.length() - 1));
        alert.showAndWait();

    }

    @FXML
    private void typeByWeekButtonHandler(ActionEvent event) {

        ArrayList<String> Info = new ArrayList();

        try (Connection conn = DBConnection.startConnection()) {

            //Opens a new connection and inserts the selected values into the
            //appointment table
            String sql = ("SELECT type, Count(*) FROM U04252.appointment WHERE (start BETWEEN ? AND ?) GROUP BY type");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, now.toString());
            statement.setString(2, eow.toString());
            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Info.add(result.getString(1) + ": " + result.getString(2));

            }

        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String list = Arrays.toString(Info.toArray()).replace(",", "\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Appoinment types by week");
        alert.setContentText("Type:  Amount\n" + list.substring(1, list.length() - 1));
        alert.showAndWait();

    }

    @FXML
    private void exitButtonHandler(ActionEvent event
    ) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "This will exit the program, do you want to continue? ");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            DBConnection.closeConnection();
            System.exit(0);

        }

    }

    private void getCustomerInfo() {

        //Opens a new connection
        try (Connection conn = DBConnection.startConnection()) {

            //creates a join on the customer, address, city and country tables to get 
            //all the data on all customers in the database 
            String sql = ("SELECT cu.customerId, cu.customerName, cu.active, ad.addressId, ad.address, ad.address2, "
                    + "ad.postalCode, ad.phone, ci.city, co.country FROM U04252.customer cu\n"
                    + "JOIN U04252.address ad ON cu.addressId = ad.addressId JOIN U04252.city "
                    + "ci ON ad.cityId = ci.cityId JOIN U04252.country co ON ci.countryId = co.countryId");
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            try {

                //gets the values from the select and sets them to variables to be
                //added to the customer object
                while (result.next()) {
                    int custID = result.getInt("customerID");
                    String custName = result.getString("customerName");
                    int addressId = result.getInt("addressId");
                    int isActive = result.getInt("active");
                    String address = result.getString("address");
                    String address2 = result.getString("address2");
                    String postalCode = result.getString("postalCode");
                    String phone = result.getString("phone");
                    String city = result.getString("city");
                    String country = result.getString("country");
                    Customer customer = new Customer(custID, custName, address,
                            address2, postalCode, phone, city, country, isActive,
                            addressId, timestamp, LoginScreenController.getCurrentUser(),
                            timestamp, LoginScreenController.getCurrentUser());
                    DataProvider.addCustomer(customer);
                }

            } catch (SQLException ex) {
                Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
        }

    }

    private void getAppointmentInfo() {

        try (Connection conn = DBConnection.startConnection()) {

            //creates a join on the customer and appointment tables to get all 
            //the data on all appointments in the database 
            String sql = ("SELECT cu.customerName, ap.appointmentId, ap.location, "
                    + "ap.type, ap.location, ap.start, ap.end, ap.createdBy, ci.city, "
                    + "co.country FROM U04252.appointment ap JOIN U04252.customer cu "
                    + "ON ap.customerId = cu.customerId JOIN U04252.address ad ON "
                    + "cu.addressId = ad.addressId JOIN U04252.city ci ON ad.cityId = "
                    + "ci.cityId JOIN U04252.country co ON ci.countryId = co.countryId");
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            try {

                //gets the values from the select and sets them to variables to be
                //added to the appointment object
                while (result.next()) {
                    int apptID = result.getInt("appointmentId");
                    String custName = result.getString("customerName");
                    String custCity = result.getString("city");
                    String custCountry = result.getString("country");
                    String apptType = result.getString("type");
                    String apptLocation = result.getString("location");
                    LocalDate apptStartDate = result.getDate("Start").toLocalDate();
                    LocalTime apptStartTime = result.getTime("Start").toLocalTime();
                    LocalDateTime cutsAptTime = DataProvider.getTZ(apptStartDate, apptStartTime, custCity, custCountry);
                    LocalDateTime yourAptTime = LocalDateTime.of(apptStartDate, apptStartTime);
                    LocalDate apptEndDate = result.getDate("end").toLocalDate();
                    LocalTime apptEndTime = result.getTime("end").toLocalTime();
                    LocalDateTime aptEnd = DataProvider.getTZ(apptEndDate, apptEndTime, custCity, custCountry);
                    String createdByy = result.getString("createdBy");
                    Appointment appointment = new Appointment(timestamp,
                            createdByy, timestamp, LoginScreenController.getCurrentUser(),
                            apptID, custName, apptType, apptLocation, yourAptTime, cutsAptTime, aptEnd);
                    DataProvider.addAppointment(appointment);

                }

            } catch (SQLException ex) {
                Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
        }
    }

    public void setMonth() {

        for (Appointment appointment : DataProvider.getAllAppointment()) {

            if (appointment.getCustStart().toLocalDate().isAfter(now) && appointment.getCustStart().toLocalDate().isBefore(eom)) {

                DataProvider.nextMonthApts.add(appointment);

            }
        }

    }

    public void setWeek() {

        for (Appointment appointment : DataProvider.getAllAppointment()) {

            if (appointment.getCustStart().toLocalDate().isAfter(now) && appointment.getCustStart().toLocalDate().isBefore(eow)) {

                DataProvider.nextWeekApts.add(appointment);

            }
        }

    }

    public void setYourApts() {

        for (Appointment appointment : DataProvider.getAllAppointment()) {

            if (appointment.getCreatedBy().equals(LoginScreenController.getCurrentUser())) {

                DataProvider.yourApts.add(appointment);

            }
        }

    }
}
