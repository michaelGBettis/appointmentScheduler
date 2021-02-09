/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package michaelbettis_jdbcapp;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.LoginLogger;

/**
 * FXML Controller class
 *
 * @author Myque
 */
public class LoginScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private static String currentUser;
    private static int currentUserID;

    public static int getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(int currentUserID) {
        LoginScreenController.currentUserID = currentUserID;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        LoginScreenController.currentUser = currentUser;
    }
    
    //Sets the current locale of the machine switches between English and German
    ResourceBundle resource = ResourceBundle.getBundle("utils/Bundle", Locale.getDefault());

    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordBox;
    @FXML
    private TextField usernameBox;
    @FXML
    private Label signInLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Changes the language of the login screen based on the locale 
        signInLabel.setText(resource.getString("signIn"));
        usernameBox.setPromptText(resource.getString("username"));
        passwordBox.setPromptText(resource.getString("password"));
        loginButton.setText(resource.getString("login"));

    }

    @FXML
    private void loginButtonHandler(ActionEvent event) throws Exception {

        try {
            //Opens a new connection and checks the User table for the values 
            //entered in the username and password textboxes
            Connection conn = DBConnection.startConnection();
            String sql = ("SELECT * FROM U04252.user WHERE username=? AND password=?");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, usernameBox.getText());
            statement.setString(2, passwordBox.getText());
            ResultSet result = statement.executeQuery();

            //If a match between the values in the user table and the values entered
            //in the username and password textboxes is found then the login is sucessful
            //and the program moves you to the main screen
            if (result.next()) {

                //Sets the current user, userID and successful stores log info
                currentUser = result.getString(2);
                currentUserID = result.getInt(1);
                LoginLogger.Logger(currentUser, true);

                //Used to switch screens
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            } else {

                //Pop-up for unsuccessful login attempt with a language appropriate
                //to locale of the current machine
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resource.getString("errorTitle"));
                alert.setHeaderText(resource.getString("errorHeader"));
                alert.setContentText(resource.getString("errorText"));
                alert.showAndWait();
                
                //Stores unsuccessful log info
                LoginLogger.Logger(currentUser, false);

                //Clears the user name and login textBoxes
                usernameBox.setText("");
                passwordBox.setText("");

            }

            //Closes the connection
            DBConnection.closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
