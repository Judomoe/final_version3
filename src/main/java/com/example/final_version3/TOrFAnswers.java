package com.example.testgenerator;
import java.io.IOException;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class TOrFAnswers {
    @FXML public RadioButton TrueButton, FalseButton;
    @FXML
    private ToggleGroup myToggleGroup; // Automatically linked from FXML
    @FXML
    public Label questionlabel,questionNumberlabel;
    @FXML
    public void setQuestion(String s){
        questionlabel.setText(s);
    }
    @FXML
    protected void BackToMenu()throws Exception{
        MainApp.switchForm("ChooseProcess.fxml");
    }
    @FXML
    public void nextQuestion() throws IOException {
        //Exams.i += 1;
        System.out.println(Exams.i);
        if(Exams.i<=9) {
        if (Exams.selectedExam != null) {
            System.out.println("Selected Exam: " + Exams.selectedExam);
            try {
                // Load the new FXML file
                Connection connection = DriverManager.getConnection(Exams.DB_URL, Exams.DB_USER, Exams.DB_PASSWORD);
                System.out.println("Connection established successfully!");

                // Create a statement object to execute SQL queries
                String query = "SELECT MAX(QUESTION_NUMBER) FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, Exams.selectedExam); // Use the variable value for arg2
                ResultSet h = preparedStatement.executeQuery();
                h.next();
                int num = h.getInt(1);

                int[] randques = MainApp.ar;
                String query7 = "SELECT Difficulty FROM QUESTIONS WHERE QUESTION_NUMBER = ? AND EXAM_ID IN (SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?)";
                PreparedStatement preparedStatement7 = connection.prepareStatement(query7);
                preparedStatement7.setString(1, String.valueOf(randques[Exams.i]));
                preparedStatement7.setString(2, Exams.selectedExam);
                ResultSet sawy = preparedStatement7.executeQuery();
                sawy.next();
                if(sawy.getString(1).equalsIgnoreCase("easy"))
                    MainApp.diff[Exams.i] =1;
                else if(sawy.getString(1).equalsIgnoreCase("hard"))
                    MainApp.diff[Exams.i] =3;
                else
                    MainApp.diff[Exams.i] =2;
                String query5="SELECT ANSWER FROM QUESTIONS WHERE QUESTION_NUMBER = ? AND EXAM_ID IN (SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?)";
                PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
                preparedStatement5.setString(1, String.valueOf(randques[Exams.i]));
                preparedStatement5.setString(2, Exams.selectedExam);
                ResultSet qatary = preparedStatement5.executeQuery();
                qatary.next();
                String modelAnswer= qatary.getString(1);
                modelAnswer=modelAnswer.toLowerCase();
                if(TrueButton.isSelected())
                    Exams.UserAnss[Exams.i]="true";
                else if(FalseButton.isSelected())
                    Exams.UserAnss[Exams.i]="false";
                if (modelAnswer.equals("true")&&TrueButton.isSelected()) {
                    MainApp.RoWAns[Exams.i]=1;
                } else if (modelAnswer.equals("false")&&FalseButton.isSelected()) {
                    MainApp.RoWAns[Exams.i]=1;
                } else{
                    MainApp.RoWAns[Exams.i]=0;
                }
                Exams.i+=1;
                if(Exams.i<=9) {
                    String query2 = "SELECT ANSWER_TYPE FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?) AND QUESTION_NUMBER=?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.setString(1, Exams.selectedExam);
                    preparedStatement2.setString(2, String.valueOf(randques[Exams.i]));
                    ResultSet g = preparedStatement2.executeQuery();
                    g.next();
                    String far5a = g.getString(1);
                    String query3 = "SELECT QUESTION FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?) AND QUESTION_NUMBER=?";
                    PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                    preparedStatement3.setString(1, Exams.selectedExam);
                    preparedStatement3.setString(2, String.valueOf(randques[Exams.i]));
                    ResultSet f = preparedStatement3.executeQuery();
                    f.next();
                    String far8a = f.getString(1);


                    //Malek Messelhy is the GOAT !!!!
                    //heya ela4kal deeh el gaybana wara
                    String query6 = "SELECT Difficulty FROM QUESTIONS WHERE EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?) AND QUESTION_NUMBER=?";
                    PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
                    preparedStatement6.setString(1, Exams.selectedExam);
                    preparedStatement6.setString(2, String.valueOf(randques[Exams.i]));
                    ResultSet Difficulty = preparedStatement6.executeQuery();
                    Difficulty.next();
                    String farsha= Difficulty.getString(1);


                    FXMLLoader loader;

                    if (far5a.equals("Short Answer")) {
                        loader = new FXMLLoader(getClass().getResource("SAnswers.fxml"));
                        Parent root = loader.load();

                        // Get the controller of the new FXML
                        SAnswers sendebad = loader.getController();
                        sendebad.setQuestion(far8a);
                        sendebad.setNumber();
                        // Switch the scene
                        Stage stage = (Stage) questionlabel.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } else if (far5a.equals("MCQ")) {
                        loader = new FXMLLoader(getClass().getResource("MCQAnswers.fxml"));
                        Parent root = loader.load();
                        String query4 = "SELECT OPTION1, OPTION2, OPTION3, OPTION4 FROM MCQQUESTIONS WHERE QUESTION_ID IN (SELECT QUESTION_ID FROM QUESTIONS WHERE QUESTION_nUMBER=? AND EXAM_ID IN (SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?));";
                        PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                        preparedStatement4.setString(1, String.valueOf(randques[Exams.i]));
                        preparedStatement4.setString(2, Exams.selectedExam);
                        String s1, s2, s3, s4;
                        ResultSet malek = preparedStatement4.executeQuery();
                        malek.next();
                        s1 = malek.getString(1);
                        s2 = malek.getString(2);
                        s3 = malek.getString(3);
                        s4 = malek.getString(4);
                        // Get the controller of the new FXML
                        MCQAnswers sendebad = loader.getController();
                        // Pass the data to the controller
                        sendebad.setQuestion(far8a);
                        sendebad.setNumber();
                        sendebad.setRadioAnswers(s1, s2, s3, s4);
                        // Switch the scene
                        Stage stage = (Stage) questionlabel.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } else {
                        loader = new FXMLLoader(getClass().getResource("TOrFAnswers.fxml"));
                        Parent root = loader.load();

                        // Get the controller of the new FXML
                        TOrFAnswers sendebad = loader.getController();
                        // Pass the data to the controller
                        sendebad.setQuestion(far8a);
                        sendebad.setNumber();
                        // Switch the scene
                        Stage stage = (Stage) questionlabel.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }}

        }
        else{
            Exams.timer.endTimer();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("NoScrollPane.fxml"));
            Parent root=loader.load();
            NoScrollPane sendebad = loader.getController();
            sendebad.setquestions();
            Stage stage=(Stage) questionlabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            //ana gamed

            System.out.println("Me4 4a8al");
            System.out.println(Arrays.toString(MainApp.RoWAns));
            System.out.println(Arrays.toString(MainApp.ar));
        }
    }
    @FXML
    private void onHelloButtonClick() {
        // Get the selected RadioButton
        RadioButton selectedRadioButton = (RadioButton) myToggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            System.out.println("Selected option: " + selectedRadioButton.getText());
        } else {
            System.out.println("Please choose an answer");
        }
    }
    @FXML
    protected void setNumber()throws Exception{
        questionNumberlabel.setText('Q'+String.valueOf(Exams.i+1));
    }
}
