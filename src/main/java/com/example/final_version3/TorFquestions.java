package com.example.testgenerator;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TorFquestions {
    String url = "jdbc:mysql://localhost:3306/TestGenerator";
    String user = "root";
    String password = "nona2005";
    @FXML
    private TextField questionField;

    @FXML
    private CheckBox checkBoxTrue;

    @FXML
    private CheckBox checkBoxFalse;

    @FXML
    private ChoiceBox<String> choiceBox;


    private boolean answerselected(){
       if (checkBoxTrue.isSelected() || checkBoxFalse.isSelected()){
           return true;
       }
       else return false;
    }

    protected boolean isempty(){
        if(questionField.getText() == null ){
            return true;
        }
        else return false;
    }

    public void initialize() {

        checkBoxTrue.setOnAction(event -> handleCheckBoxSelection(checkBoxTrue));
        checkBoxFalse.setOnAction(event -> handleCheckBoxSelection(checkBoxFalse));


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

    @FXML
    protected void gochoosequest() throws Exception{
        if(!answerselected() || isempty()){
            System.out.println("please Fill all Fields");
        }
        else {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("connection established succesfully");
                Statement statement = connection.createStatement();
                String query = "INSERT INTO QUESTIONS(QUESTION,QUESTION_NUMBER,EXAM_ID,ANSWER_TYPE,ANSWER,SUBJECT,DIFFICULTY Values(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, questionField.getText());
                preparedStatement.setString(2, String.valueOf(MainApp.questionnumber));
                preparedStatement.setString(3, MCQquestions.exam_id);
                preparedStatement.setString(4, "T/F");//waassup
                if (checkBoxTrue.isSelected()) {                        //wassup
                    preparedStatement.setString(5, "True");
                } else {
                    preparedStatement.setString(5, "False");
                }
                preparedStatement.setString(6, MCQquestions.subject);
                preparedStatement.setString(7, choiceBox.getValue());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MainApp.questionnumber += 1;
            MainApp.switchForm("ChooseQuestion.fxml");
        }
    }

//    @FXML
//    public void initialize() {
//        checkBoxTrue.setOnAction(event -> handleCheckBoxSelection(checkBoxTrue));
//        checkBoxFalse.setOnAction(event -> handleCheckBoxSelection(checkBoxFalse));
//
//    }

    private void handleCheckBoxSelection(CheckBox selectedCheckBox) {
        if (selectedCheckBox == checkBoxTrue) {
            checkBoxFalse.setSelected(false);
        } else if (selectedCheckBox == checkBoxFalse) {
            checkBoxTrue.setSelected(false);
        }
    }

    private void saveQuestionAndAnswer() {
        String question = questionField.getText();
        String correctAnswer = "";

        if (checkBoxTrue.isSelected()) {
            correctAnswer = "TRUE";
        } else if (checkBoxFalse.isSelected()) {
            correctAnswer = "FALSE";
        }

        System.out.println("Question: " + question);
        System.out.println("Correct Answer: " + correctAnswer);
        // Here you can add logic to save the question and correct answer to a database or a file
    }
    @FXML
    protected void goMenu()throws Exception {
        MainApp.switchForm("ChooseProcess.fxml");
    }

}
