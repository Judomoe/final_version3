package com.example.testgenerator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import org.mindrot.jbcrypt.BCrypt;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Users {
    @FXML
    private TextField user_textfield;
    @FXML
    private PasswordField pass1_textfield,pass2_textfield;
    @FXML
    private TextField login_username;
    @FXML
    private PasswordField login_password;
    @FXML
    private Label message,User_textfield;
    @FXML
    private Label sheesh;
    @FXML
    private Label warn1;
    @FXML
    private Label warn2;
    @FXML
    protected void SignUp() throws Exception {
        if (!isempty1()){
        String arg1=user_textfield.getText();
        String arg2=pass1_textfield.getText();
        String arg3=pass2_textfield.getText();
        String arg4=generateSalt();
        if(arg2.equals(arg3)) {
            String url = "jdbc:mysql://localhost:3306/TestGenerator";
            String user = "root";
            String password = "nona2005";
            arg2 = hashPassword(arg2, arg4);
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection established successfully!");

                // Create a statement object to execute SQL queries
                Statement statement = connection.createStatement();
                Statement statement2 = connection.createStatement();
                String query2 = "SELECT * FROM users";
                String query = "INSERT INTO users(username, password, salt) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, arg1);  // Use the variable value for arg1
                preparedStatement.setString(2, arg2);
                preparedStatement.setString(3, arg4);// Use the variable value for arg2
                preparedStatement.executeUpdate();

                ResultSet resultSet = statement2.executeQuery(query2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MainApp.switchForm("Login.fxml");
        }
        }
        else warn1.setVisible(true);
    }

    @FXML
    protected void Login() throws Exception{
        if(!isempty2()) {
            String arg1 = login_username.getText();
            String arg2 = login_password.getText();
            String url = "jdbc:mysql://localhost:3306/testgenerator";
            String user = "root";
            String password = "nona2005";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection established successfully!");
                // Create a statement object to execute SQL queries
                String query2 = "SELECT Password, Salt FROM users WHERE username = ?";

                PreparedStatement prepst = connection.prepareStatement(query2);
                prepst.setString(1, arg1);
                ResultSet resultSet = prepst.executeQuery();
                resultSet.next();
                String salt = resultSet.getString("Salt");
                arg2 = hashPassword(arg2, salt);
                String query = "select count(*) from users where username=? and password=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, arg1);  // Use the variable value for arg1
                preparedStatement.setString(2, arg2);  // Use the variable value for arg2
                ResultSet h = preparedStatement.executeQuery();
                h.next();
                int num = h.getInt(1);
                if (num == 0) {
                    message.setText("Incorrect Username or Password");
                } else {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseProcess.fxml"));
                    Parent root = loader.load();

                    // Get the controller of the new FXML
                    Users sendebad = loader.getController();
                    sendebad.setSheesh(arg1);
                    // Switch the scene
                    Stage stage = (Stage) message.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                    PreparedStatement stat = connection.prepareStatement("SELECT User_ID FROM USERS WHERE username=? and password=?");
                    stat.setString(1, arg1);
                    stat.setString(2, arg2);
                    ResultSet result = stat.executeQuery();
                    result.next();
                    MainApp.setCurrentUserID(result.getInt(1));
//                MainApp.switchForm("ChooseProcess.fxml");

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //MainApp.switchForm("Login.fxml");
        }
        else warn2.setVisible(true);

    }
    @FXML protected void setSheesh(String s) throws Exception {
        if (sheesh != null) {
            sheesh.setText(s);
        }
    }
    @FXML
    protected void setUser_textfield(){
        User_textfield.setText(String.valueOf(MainApp.getCurrentUserID()));
    }

    @FXML
    protected void SignUp2() throws Exception{
        MainApp.switchForm("SignUp.fxml");
    }

    // Method to generate a salt
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // Method to hash a password using SHA-256 and a salt
    public static String hashPassword(String password, String salt) {
        String hashedPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes()); // Add salt to the hash
            byte[] hashedBytes = messageDigest.digest(password.getBytes()); // Hash the password
            hashedPassword = Base64.getEncoder().encodeToString(hashedBytes); // Encode the hash
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    protected boolean isempty1(){
        if(user_textfield.getText().isEmpty() || pass1_textfield.getText().isEmpty() || pass2_textfield.getText().isEmpty()){
            return true;
        }
        else return false;
    }
    protected boolean isempty2(){
        if(login_username.getText().isEmpty() || login_password.getText().isEmpty() ){
            return true;
        }
        else return false;
    }

    @FXML
    protected void Login2() throws Exception{
        MainApp.switchForm("Login.fxml");
    }

    @FXML
    protected void newExam()throws Exception{
        MainApp.switchForm("ExamCreate.fxml");
    }

    @FXML
    protected void goAdvance()throws Exception{
        MainApp.switchForm("ChooseQuestion.fxml");
    }
    @FXML
    protected void getback() throws Exception{
        MainApp.switchForm("ChooseProcess.fxml");
    }
    @FXML
    protected void gotosearch()throws Exception{
        MainApp.switchForm("SearchExam.fxml");
    }

    @FXML
    protected void goMCQ()throws Exception{
        MainApp.switchForm("MCQquestions.fxml");
    }
    @FXML
    protected void goTorF()throws Exception{
        MainApp.switchForm("TorFquestions.fxml");
    }
    @FXML
    protected void returnexamcreate()throws Exception{
        MainApp.switchForm("ExamCreate.fxml");
    }
    @FXML
    protected void goSAnswers()throws Exception{
        MainApp.switchForm("SAquestions.fxml");
    }
}