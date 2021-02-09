/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Myque
 */
public class Appointment extends LogInfo {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime yourStart;
    private LocalDateTime custStart;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private String customerName;

    public Appointment(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy,
            int appointmentId, String title, String description, String location,
            String contact, String type, LocalDateTime yourStart, LocalDateTime custStart, LocalDateTime end, int userID, int customerID) {

        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.yourStart = yourStart;
        this.custStart = custStart;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    public Appointment(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy,
            int appointmentId, String customerName, String type, String location, LocalDateTime yourStart, LocalDateTime custStart, LocalDateTime end) {

        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.type = type;
        this.location = location;  
        this.yourStart = yourStart;
        this.custStart = custStart;
        this.end = end;
    }

    public LocalDateTime getYourStart() {
        return yourStart;
    }

    public void setYourStart(LocalDateTime yourStart) {
        this.yourStart = yourStart;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCustStart() {
        return custStart;
    }

    public void setCustStart(LocalDateTime custStart) {
        this.custStart = custStart;
    }
    
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
