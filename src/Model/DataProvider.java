/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static java.lang.Math.abs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import michaelbettis_jdbcapp.LoginScreenController;
import utils.DBConnection;

/**
 *
 * @author Myque
 */
public class DataProvider {

    private static ObservableList<User> allUser = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomer = FXCollections.observableArrayList();
    private static ObservableList<Address> allAddress = FXCollections.observableArrayList();
    private static ObservableList<City> allCity = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountry = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointment = FXCollections.observableArrayList();
    private static ObservableList<User> filteredUser = FXCollections.observableArrayList();
    private static ObservableList<Customer> filteredCustomer = FXCollections.observableArrayList();
    private static ObservableList<Appointment> filteredAppointment = FXCollections.observableArrayList();
    public static ObservableList<Appointment> nextMonthApts = FXCollections.observableArrayList();
    public static ObservableList<Appointment> nextWeekApts = FXCollections.observableArrayList();
    public static ObservableList<Appointment> yourApts = FXCollections.observableArrayList();

    public static void addUser(User user) {

        allUser.add(user);

    }

    public static void addCustomer(Customer customer) {

        allCustomer.add(customer);

    }

    public static void addAddress(Address address) {

        allAddress.add(address);

    }

    public static void addCity(City city) {

        allCity.add(city);

    }

    public static void addCountry(Country country) {

        allCountry.add(country);

    }

    public static void addAppointment(Appointment appointment) {

        allAppointment.add(appointment);

    }

    public static Customer lookupCustomer(int customerID) {

        if (customerID > allCustomer.size() || customerID < 1) {
            return null;
        } else {
            return allCustomer.get(customerID);
        }

    }

    public static Appointment lookupAppointment(int AppointmentID) {

        if (AppointmentID > allAppointment.size() || AppointmentID < 1) {
            return null;
        } else {
            return allAppointment.get(AppointmentID);
        }

    }

    public static ObservableList<Customer> lookupCustomer(String customerName) {

        if (!(DataProvider.filteredCustomer().isEmpty())) {
            DataProvider.filteredCustomer().clear();
        }

        DataProvider.filteredCustomer().stream().filter((customer)
                -> (customer.getCustomerName().contains(customerName))).forEachOrdered((customer) -> {
            DataProvider.filteredCustomer().add(customer);
        });

        if (DataProvider.filteredCustomer().isEmpty()) {
            return DataProvider.filteredCustomer();
        } else {
            return DataProvider.filteredCustomer();
        }

    }

    public static ObservableList<Appointment> lookupAppointment(String appointmentName) {

        if (!(DataProvider.filteredAppointment().isEmpty())) {
            DataProvider.filteredAppointment().clear();
        }

        DataProvider.filteredAppointment().stream().filter((appointment) -> (appointment.getTitle().contains(appointmentName))).forEachOrdered((appointment) -> {
            DataProvider.filteredAppointment().add(appointment);
        });

        if (DataProvider.filteredAppointment().isEmpty()) {
            return DataProvider.filteredAppointment();
        } else {
            return DataProvider.filteredAppointment();
        }

    }

    public static void deleteCustomer(Customer selectedCustomer) {

        //Deletes a Customer for an ID
        DataProvider.getAllCustomer().stream().filter((customer) -> (customer.getCustomerID() == selectedCustomer.getCustomerID())).forEachOrdered((customer) -> {
            DataProvider.getAllCustomer().remove(customer);
        });
    }

    public static void deleteAppointment(Appointment selectedAppointment) {

        //Deletes an Appointment for an ID
        allAppointment.remove(selectedAppointment);
    }

    public static ObservableList<User> getAllUser() {

        return allUser;

    }

    public static ObservableList<Customer> getAllCustomer() {

        return allCustomer;

    }

    public static ObservableList<Address> getAllAddress() {

        return allAddress;

    }

    public static ObservableList<City> getAllCity() {

        return allCity;

    }

    public static ObservableList<Country> getAllCountry() {

        return allCountry;

    }

    public static ObservableList<Appointment> getAllAppointment() {

        return allAppointment;

    }

    public static ObservableList<User> filteredUser() {

        return filteredUser;

    }

    public static ObservableList<Customer> filteredCustomer() {

        return filteredCustomer;

    }

    public static ObservableList<Appointment> filteredAppointment() {

        return filteredAppointment;

    }

    public static Customer selectCustomer(int id) {

        for (Customer customer : getAllCustomer()) {

            if (customer.getCustomerID() == id) {
                return customer;
            }
        }
        return null;
    }

    public static Appointment selectAppointment(int id) {

        for (Appointment appointment : getAllAppointment()) {

            if (appointment.getAppointmentId() == id) {
                return appointment;
            }
        }
        return null;
    }

    public static ObservableList<Customer> filterCustomer(String Name) {

        if (!(DataProvider.filteredCustomer().isEmpty())) {
            DataProvider.filteredCustomer().clear();
        }

        DataProvider.getAllCustomer().stream().filter((customer) -> (customer.getCustomerName().contains(Name))).forEachOrdered((customer) -> {
            DataProvider.filteredCustomer().add(customer);
        });

        if (DataProvider.filteredCustomer().isEmpty()) {
            return DataProvider.getAllCustomer();
        } else {
            return DataProvider.filteredCustomer();
        }
    }

    public static ObservableList<Appointment> filterAppointment(String Name) {

        if (!(DataProvider.filteredAppointment().isEmpty())) {
            DataProvider.filteredAppointment().clear();
        }

        DataProvider.getAllAppointment().stream().filter((appointment) -> (appointment.getCustomerName().contains(Name))).forEachOrdered((appointment) -> {
            DataProvider.filteredAppointment().add(appointment);
        });

        if (DataProvider.filteredAppointment().isEmpty()) {
            return DataProvider.filteredAppointment();
        } else {
            return DataProvider.filteredAppointment();
        }
    }

    public static boolean isStringInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static LocalDateTime createDate(LocalDate date, String hour, String minute) {

        //Attempts to create a LocalDateTime with the passed in values or throws an error
        LocalDateTime localDT = null;

        try {

            localDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));

        } catch (java.lang.NullPointerException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Please enter a Date, Time and Duration for your appointment.");
            alert.showAndWait();

        }
        return localDT;

    }

    public static boolean isOverlapping(LocalDateTime start, LocalDateTime end) {

        try (Connection conn = DBConnection.startConnection()) {

            String sql = ("SELECT * FROM U04252.appointment ap JOIN U04252.customer "
                    + "cu ON ap.customerId = cu.customerId WHERE (ap.start BETWEEN ? AND ?) AND ap.userId = ?");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, start.toString());
            statement.setString(2, end.toString());
            statement.setInt(3, LoginScreenController.getCurrentUserID());
            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Overlapping Appointment");
                alert.setContentText("The selected start time is already taken please"
                        + "select another start time.");
                alert.showAndWait();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static boolean upcomingApt() {

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime next15 = currentTime.plusMinutes(15);
        String name = null;

        try (Connection conn = DBConnection.startConnection()) {

            String sql = ("SELECT cu.customerName, ap.start, ap.end FROM U04252.appointment"
                    + " ap JOIN U04252.customer cu ON ap.customerId = cu.customerId WHERE "
                    + "(ap.start BETWEEN ? AND ?) AND ap.userId = ?");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, currentTime.toString());
            statement.setString(2, next15.toString());
            statement.setInt(3, LoginScreenController.getCurrentUserID());
            ResultSet result = statement.executeQuery();

            if (result.next()) {

                LocalTime aptTime = result.getTime("start").toLocalTime();
                name = result.getString("customerName");
                long timeDiff = ChronoUnit.MINUTES.between(aptTime, currentTime.toLocalTime());
                long timeToApt = abs(timeDiff - 1);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Upcomming Appointment Notification");
                alert.setContentText("You have a scheduled appointment in " + timeToApt
                        + " minute(s) with " + name);
                alert.showAndWait();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.NullPointerException e) {

            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("This Should not happen, please seek admin help.");
            alert.showAndWait();
        }
        return false;
    }

    public static LocalDateTime getTZ(LocalDate date, LocalTime time, String city, String country) {

        if (country.equals("US") || country.equals("Canada")) {
            country = "America";
        } else if (country.equals("Norway")) {
            country = "Europe";
        }

        LocalDateTime ldt = LocalDateTime.of(date, time);
        ZoneId zid = ZoneId.of(country + "/" + city.replace(" ", "_"));
        ZonedDateTime locZdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        ZonedDateTime utcZdt = locZdt.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime custZdt = utcZdt.withZoneSameInstant(zid);

        return custZdt.toLocalDateTime();

    }

}
