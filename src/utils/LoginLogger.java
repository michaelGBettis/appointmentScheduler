/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.*;
import java.time.LocalDateTime;

/**
 *
 * @author Myque
 */
public class LoginLogger {

    public static void Logger(String user, boolean success) throws FileNotFoundException, IOException {

        LocalDateTime now = LocalDateTime.now();

        //Flename and itme variables
        String filename = "src/files/log.txt";

        //Create fileWriter object
        FileWriter fWriter = new FileWriter(filename, true);

        //Creates a file and writes items to the file
        try (PrintWriter outputFile = new PrintWriter(fWriter)) {
            //Get items and write to file
            outputFile.println(user + " " + now + " " + (success ? " Success" : " Failure"));
            //Close file
        }

    }

}
