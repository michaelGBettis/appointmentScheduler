/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Myque
 */
public class User extends LogInfo{
    
     private int userID ;
     private String userName ;
     private String password ;
     private int active;
     
     public User(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, 
                 int userID, String userName, String password, int active) {
         super(createDate, createdBy, lastUpdate, lastUpdateBy);
         this.userID = userID;
         this.userName = userName;
         this.password = password;
         this.active = active;
     }
     
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
}
