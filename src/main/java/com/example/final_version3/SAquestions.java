package com.example.testgenerator;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

public class SAquestions {
    String url = "jdbc:mysql://localhost:3306/TestGenerator";
    String user = "root";
    String password = "nona2005";
    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField;

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label warn;

    public void initialize() {
        // Populate the ChoiceBox with initial options
        choiceBox.getItems().addAll("Easy", "Medium", "Hard");

        // Set a default selected item if desired
        choiceBox.setValue("Easy");

        // Add a listener to handle selection changes
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the change in selection
            System.out.println("Selected: " + newValue);
        });
    }

    protected int isempty(){
        if(questionField.getText().isEmpty() || answerField.getText().isEmpty()){
            return 1;
        }
        else return 0;
    }

    @FXML
    protected void goMenu()throws Exception {
        MainApp.switchForm("ChooseProcess.fxml");
    }

    @FXML
    protected void gochoosequest() throws Exception{
        System.out.println(isempty());
        if(isempty()==0) {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("connection established succesfully");
                String query = "INSERT INTO QUESTIONS(QUESTION,QUESTION_NUMBER,EXAM_ID,ANSWER_TYPE,ANSWER,SUBJECT,DIFFICULTY) Values(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, questionField.getText());
                preparedStatement.setString(2, String.valueOf(MainApp.questionnumber));
                preparedStatement.setString(3, MCQquestions.exam_id);
                preparedStatement.setString(4, "Short Answer");
                preparedStatement.setString(5, answerField.getText());
                preparedStatement.setString(6, MCQquestions.subject);
                preparedStatement.setString(7, choiceBox.getValue());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MainApp.questionnumber += 1;
            MainApp.switchForm("ChooseQuestion.fxml");
        }
        else warn.setVisible(true);
    }//eh yalla 3amel ma4akel leh hehe
    // ana??? haram 3leik beyedy errors 3andak
    // ento el gaybeen gomhoreyet masr el3arabeya wara
    //bas yabny

}
//masa7t leh?
// sbny a4t8l ya 3m enta