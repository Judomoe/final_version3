package com.example.testgenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class Exams {
    //<3 sasa ;)
    public static String[] UserAnss=new String[10];
    public static String selectedExam;
    public static int i;
    @FXML
    private AnchorPane bedayetelpane;
    @FXML
    private VBox fera5pane;

    @FXML
    private Label questionlabel;
    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> suggestionList;

    private ObservableList<String> examList = FXCollections.observableArrayList();

    static Timer timer = new Timer();



    // MySQL Database Configuration
    static final String DB_URL = "jdbc:mysql://localhost:3306/testgenerator"; // Replace with your DB name
    static final String DB_USER = "root"; // Replace with your MySQL username
    static final String DB_PASSWORD = "nona2005"; // Replace with your MySQL password

    @FXML
    public void initialize() {
        suggestionList.setVisible(false); // Initially hide the suggestions
    }

    @FXML
    private void onSearchKeyReleased() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            suggestionList.setVisible(false); // Hide suggestions when input is empty
            return;
        }

        // Fetch data from the database based on the query
        examList = fetchExamSuggestions(query);

        // Update the ListView and toggle visibility
        suggestionList.setItems(examList);
        suggestionList.setVisible(!examList.isEmpty());
    }

    @FXML
    private void onSuggestionClicked() {

        timer.startTimer();
        i=0;
        selectedExam = suggestionList.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {


            System.out.println("Selected Exam: " + selectedExam);
            try {
                // Load the new FXML file
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD) ;
                System.out.println("Connection established successfully!");

                // Create a statement object to execute SQL queries
                String query = "SELECT MAX(QUESTION_NUMBER) FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedExam); // Use the variable value for arg2
                ResultSet h=preparedStatement.executeQuery();
                h.next();
                int num=h.getInt(1);

                int[] randques=MainApp.getArAndFill(num);
                String query2="SELECT ANSWER_TYPE FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?) AND QUESTION_NUMBER=?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setString(1, selectedExam);
                preparedStatement2.setString(2, String.valueOf(randques[i]));
                ResultSet g=preparedStatement2.executeQuery();
                g.next();
                String far5a=g.getString(1);
                String query3="SELECT QUESTION FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?) AND QUESTION_NUMBER=?";
                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement3.setString(1, selectedExam);
                preparedStatement3.setString(2, String.valueOf(randques[i]));
                ResultSet f=preparedStatement3.executeQuery();
                f.next();
                String far8a=f.getString(1);
                FXMLLoader loader;

                if(far5a.equals("Short Answer")) {
                    loader = new FXMLLoader(getClass().getResource("SAnswers.fxml"));
                    Parent root = loader.load();

                    // Get the controller of the new FXML
                    SAnswers sendebad = loader.getController();
                    sendebad.setQuestion(far8a);
                    sendebad.setNumber();
                    // Switch the scene
                    Stage stage = (Stage) suggestionList.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                else if(far5a.equals("MCQ")){
                    loader = new FXMLLoader(getClass().getResource("MCQAnswers.fxml"));
                    Parent root = loader.load();
                    String query4="SELECT OPTION1, OPTION2, OPTION3, OPTION4 FROM MCQQUESTIONS WHERE QUESTION_ID IN (SELECT QUESTION_ID FROM QUESTIONS WHERE QUESTION_nUMBER=? AND EXAM_ID IN (SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?));";
                    PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                    preparedStatement4.setString(1,String.valueOf(randques[i]));
                    preparedStatement4.setString(2,selectedExam);
                    String s1,s2,s3,s4;
                    ResultSet malek=preparedStatement4.executeQuery();
                    malek.next();
                    s1=malek.getString(1);
                    s2=malek.getString(2);
                    s3=malek.getString(3);
                    s4=malek.getString(4);

                    // Get the controller of the new FXML
                    MCQAnswers sendebad = loader.getController();
                    // Pass the data to the controller
                    sendebad.setQuestion(far8a);
                    sendebad.setNumber();
                    sendebad.setRadioAnswers(s1,s2,s3,s4);
                    // Switch the scene
                    Stage stage = (Stage) suggestionList.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                else{
                    loader = new FXMLLoader(getClass().getResource("TOrFAnswers.fxml"));
                    Parent root = loader.load();

                    // Get the controller of the new FXML
                    TOrFAnswers sendebad = loader.getController();
                    // Pass the data to the controller
                    sendebad.setQuestion(far8a);
                    sendebad.setNumber();
                    // Switch the scene
                    Stage stage = (Stage) suggestionList.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private ObservableList<String> fetchExamSuggestions(String query) {
        ObservableList<String> suggestions = FXCollections.observableArrayList();

        String sql = "SELECT ExamName FROM exams WHERE LOWER(ExamName) LIKE ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                suggestions.add(rs.getString("ExamName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return suggestions;
    }
    @FXML
    protected void BackToChooseProcess() throws Exception {
        MainApp.switchForm("ChooseProcess.fxml");
    }

//    @FXML
//    public void sasaFunc() throws Exception {
//
//    }
}
