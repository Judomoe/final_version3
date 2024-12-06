package com.example.testgenerator;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import java.sql.*;

public class MCQquestions {
    String url = "jdbc:mysql://localhost:3306/TestGenerator";
    String user = "root";
    String password = "nona2005";
    public static String exam_id,subject;

    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField1;

    @FXML
    private TextField answerField2;

    @FXML
    private TextField answerField3;

    @FXML
    private TextField answerField4;

    @FXML
    private CheckBox checkBox1;

    @FXML
    private CheckBox checkBox2;

    @FXML
    private CheckBox checkBox3;

    @FXML
    private CheckBox checkBox4;

    @FXML
    private Label warn;

    @FXML
    private ChoiceBox<String> choicebox;

    private CheckBox[] checkBoxes;
    private TextField[] answerFields;

    @FXML
    public void initialize() {
        checkBoxes = new CheckBox[]{checkBox1, checkBox2, checkBox3, checkBox4};
        answerFields = new TextField[]{answerField1, answerField2, answerField3, answerField4};

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnAction(event -> handleCheckBoxSelection(checkBox));
        }
        // Populate the ChoiceBox with initialeh eldonia  options
        choicebox.getItems().addAll("Easy", "Medium", "Hard");

        // Set a default selected item if desired
        choicebox.setValue("Easy");

        // Add a listener to handle selection changes
        choicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the change in selection
            System.out.println("Selected: " + newValue);
        });
    }

    protected boolean isempty(){
        if(questionField.getText().isEmpty() || answerField1.getText().isEmpty() || answerField2.getText().isEmpty() || answerField3.getText().isEmpty() ||answerField4.getText().isEmpty()){
            return true;
        }
        else return false;
    }

//    @FXML
//    public void initialize() {
//        checkBoxes = new CheckBox[]{checkBox1, checkBox2, checkBox3, checkBox4};
//        answerFields = new TextField[]{answerField1, answerField2, answerField3, answerField4};
//
//        for (CheckBox checkBox : checkBoxes) {
//            checkBox.setOnAction(event -> handleCheckBoxSelection(checkBox));
//        }
//    }

    private void handleCheckBoxSelection(CheckBox selectedCheckBox) {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox != selectedCheckBox) {
                checkBox.setSelected(false);
            }
        }
    }

    public String getQuestion() {
        return questionField.getText();
    }

    public String[] getAnswers() {
        String[] answers = new String[answerFields.length];
        for (int i = 0; i < answerFields.length; i++) {
            answers[i] = answerFields[i].getText();
        }
        return answers;
    }

    public int getCorrectAnswerIndex() {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                return i;
            }
        }
        return -1; // No correct answer selected
    }
    private boolean answerselected(){
        for(CheckBox checkBox : checkBoxes){
            if(checkBox.isSelected())
                return true;
        }
        return false;
    }
    @FXML
    protected void gochoosequest()throws Exception{
        if (!answerselected() || isempty()) {
            warn.setVisible(true);
        }
        else {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection established successfully!");

                // Create a statement object to execute SQL queries
                Statement statement = connection.createStatement();
                String query = "INSERT INTO QUESTIONS(QUESTION,QUESTION_NUMBER,EXAM_ID,ANSWER_TYPE,ANSWER,SUBJECT,DIFFICULTY) VALUES(?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, questionField.getText());  // Use the variable value for arg1
                preparedStatement.setString(2, String.valueOf(MainApp.questionnumber));  // Use the variable value for arg2
                preparedStatement.setString(3, exam_id);
                preparedStatement.setString(4, "MCQ");
                if (checkBox1.isSelected()) {
                    preparedStatement.setString(5, "a");
                } else if (checkBox2.isSelected()) {
                    preparedStatement.setString(5, "b");
                } else if (checkBox3.isSelected()) {
                    preparedStatement.setString(5, "c");
                } else if (checkBox4.isSelected()) {
                    preparedStatement.setString(5, "d");
                }
                preparedStatement.setString(6, subject);
                preparedStatement.setString(7, choicebox.getValue());
                preparedStatement.executeUpdate();

                String query3 = "select question_id from questions where QUESTION=? and QUESTION_NUMBER=? and EXAM_ID=?";
                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement3.setString(1, questionField.getText());  // Use the variable value for arg1
                preparedStatement3.setString(2, String.valueOf(MainApp.questionnumber));
                preparedStatement3.setString(3, exam_id);
                ResultSet resultSet3 = preparedStatement3.executeQuery();
                resultSet3.next();

                String hh = resultSet3.getString(1);

                String query4 = "INSERT INTO MCQQUESTIONS(QUESTION_ID,OPTION1,OPTION2,OPTION3,OPTION4) VALUES(?,?,?,?,?);";
                PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                preparedStatement4.setString(1, hh);  // Use the variable value for arg1
                preparedStatement4.setString(2, answerField1.getText());
                preparedStatement4.setString(3, answerField2.getText());
                preparedStatement4.setString(4, answerField3.getText());
                preparedStatement4.setString(5, answerField4.getText());
                preparedStatement4.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            MainApp.questionnumber += 1;
            MainApp.switchForm("ChooseQuestion.fxml");
        }
    }
    @FXML
    protected void goMenu()throws Exception{
        MainApp.switchForm("ChooseProcess.fxml");
    }

}
