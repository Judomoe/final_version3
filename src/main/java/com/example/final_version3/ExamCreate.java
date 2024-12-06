package com.example.testgenerator;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;

public class ExamCreate {
    String url = "jdbc:mysql://localhost:3306/TestGenerator";
    String user = "root";
    String password = "nona2005";
    @FXML
    private TextField ExamName;
    @FXML
    private ChoiceBox<String> choicebox;
    @FXML
    private Label warn;

    @FXML
    public void initialize() {
        // Populate the ChoiceBox with initial options
        choicebox.getItems().addAll("MATH", "SCIENCE", "COMPUTER", "ENGLISH", "PHYSICS");

        // Set a default selected item if desired
        choicebox.setValue("MATH");

        // Add a listener to handle selection changes
        choicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the change in selection
            System.out.println("Selected: " + newValue);
        });
    }

    // Method to update the options in the ChoiceBox
    public void updateOptions(String... newOptions) {
        choicebox.getItems().setAll(newOptions);
        if (newOptions.length > 0) {
            choicebox.setValue(newOptions[0]); // Set the first item as the default value
        }
    }

    protected boolean isempty(){
        if(ExamName.getText().isEmpty()){
            return true;
        }
        else return false;
    }

    @FXML
    protected void choosetype()throws Exception{

        if(!isempty()) {
            MainApp.questionnumber = 0;
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection established successfully!");

                // Create a statement object to execute SQL queries
                String query = "INSERT INTO exams(EXAMNAME, SUBJECT) VALUES(?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ExamName.getText());  // Use the variable value for arg1
                preparedStatement.setString(2, choicebox.getValue());  // Use the variable value for arg2
                preparedStatement.executeUpdate();
                String query2 = "SELECT exam_id FROM exams where examname=? AND subject=?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setString(1, ExamName.getText());  // Use the variable value for arg1
                preparedStatement2.setString(2, choicebox.getValue());
                ResultSet resultSet = preparedStatement2.executeQuery();
                resultSet.next();

                MCQquestions.exam_id = resultSet.getString(1);
                String query3 = "SELECT subject FROM exams where examname=? AND subject=?";
                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement3.setString(1, ExamName.getText());  // Use the variable value for arg1
                preparedStatement3.setString(2, choicebox.getValue());
                ResultSet resultSet2 = preparedStatement3.executeQuery();
                resultSet2.next();

                MCQquestions.subject = resultSet2.getString(1);
                //TorFquestions.subject=resultSet2.getString(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            MainApp.switchForm("ChooseQuestion.fxml");
        }
        else warn.setVisible(true);
    }
    @FXML
    protected void goBack()throws Exception{
        MainApp.switchForm("ChooseProcess.fxml");
    }
}

