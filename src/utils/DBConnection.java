/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Myque
 */
public class DBConnection {
    
    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress  = "//3.227.166.251/U04252";
    
    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    //Driver Interface Reference 
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    //Username
    private static final String username = "U04252";
    
    //password
    private static final String password = "53688148290";
    
    public static Connection startConnection()
    {
        try{
           Class.forName(MYSQLJDBCDriver); 
           conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
           //System.out.println("Connection Succesful!");
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;

    }
            
    public static void closeConnection()
    {
        try{
            conn.close();
            System.out.println("Connection Closed");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
