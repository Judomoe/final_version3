package com.example.testgenerator;

import javafx.fxml.FXML;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

public class NoScrollPane {
    String url = "jdbc:mysql://localhost:3306/TestGenerator";
    String user = "root";
    String password = "nona2005";
    @FXML
    private Label queslabel1,queslabel2,queslabel3,queslabel4,queslabel5,queslabel6,queslabel7,queslabel8,queslabel9,queslabel10,question1;
    @FXML
    private Label modans1,modans2,modans3,modans4,modans5,modans6,modans7,modans8,modans9,modans10;
    @FXML
    private Label userans1,userans2,userans3,userans4,userans5,userans6,userans7,userans8,userans9,userans10;
    @FXML
    private Label time,score;
    @FXML
    public void setquestions(){
        time.setText(Exams.timer.getTimeInMinutesAndSeconds());
        String[] as2ela = new String[10];
        String[] agda3agweba=new String[10];
        Arrays.fill(agda3agweba,"NULL");
        Arrays.fill(as2ela, "NULL");
        int Score=0;
        try {
            // Load the new FXML file
            Connection connection = DriverManager.getConnection(Exams.DB_URL, Exams.DB_USER, Exams.DB_PASSWORD);
            System.out.println("Connection established successfully!");
            for (int j = 0; j < 10; j++) {
                String query = "SELECT QUESTION FROM QUESTIONS WHERE QuESTION_NUMBER = ? AND EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME = ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(2, Exams.selectedExam);
                preparedStatement.setString(1, String.valueOf(MainApp.ar[j]));
                ResultSet hmm = preparedStatement.executeQuery();
                hmm.next();
                as2ela[j] = hmm.getString(1);
            }
            for (int j = 0; j < 10; j++) {
                String query1="SELECT Answer_type FROM QUESTIONS WHERE QuESTION_NUMBER = ? AND EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME = ?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(2, Exams.selectedExam);
                preparedStatement1.setString(1, String.valueOf(MainApp.ar[j]));
                ResultSet hmm1 = preparedStatement1.executeQuery();
                hmm1.next();
                String anaFein = hmm1.getString(1);
                if(anaFein.equalsIgnoreCase("mcq")){
                    String query4="SELECT OPTION1, OPTION2, OPTION3, OPTION4 FROM MCQQUESTIONS WHERE QUESTION_ID IN (SELECT QUESTION_ID FROM QUESTIONS WHERE QUESTION_nUMBER=? AND EXAM_ID IN (SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME=?));";
                    PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                    preparedStatement4.setString(1,String.valueOf(MainApp.ar[j]));
                    preparedStatement4.setString(2,Exams.selectedExam);
                    String s1,s2,s3,s4;
                    ResultSet malek=preparedStatement4.executeQuery();
                    malek.next();
                    s1=malek.getString(1);
                    s2=malek.getString(2);
                    s3=malek.getString(3);
                    s4=malek.getString(4);
                    String query = "SELECT Answer FROM QUESTIONS WHERE QuESTION_NUMBER = ? AND EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME = ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(2, Exams.selectedExam);
                    preparedStatement.setString(1, String.valueOf(MainApp.ar[j]));
                    ResultSet hmm = preparedStatement.executeQuery();
                    hmm.next();
                    agda3agweba[j] = hmm.getString(1);
                    if(Exams.UserAnss[j].equalsIgnoreCase("a")){
                        Exams.UserAnss[j] = s1;
                    }else if(Exams.UserAnss[j].equalsIgnoreCase("b")){
                        Exams.UserAnss[j] = s2;
                    }else if(Exams.UserAnss[j].equalsIgnoreCase("c")){
                        Exams.UserAnss[j] = s3;
                    }else if (Exams.UserAnss[j].equalsIgnoreCase("d")){
                        Exams.UserAnss[j] = s4;
                    }
                    if(agda3agweba[j].equalsIgnoreCase("a")){
                        agda3agweba[j] = s1;
                    }else if(agda3agweba[j].equalsIgnoreCase("b")){
                        agda3agweba[j] = s2;
                    }else if(agda3agweba[j].equalsIgnoreCase("c")){
                        agda3agweba[j] = s3;
                    }else if (agda3agweba[j].equalsIgnoreCase("d")){
                        agda3agweba[j] = s4;
                    }
                }
                else {
                    String query = "SELECT Answer FROM QUESTIONS WHERE QuESTION_NUMBER = ? AND EXAM_ID IN(SELECT EXAM_ID FROM EXAMS WHERE EXAMNAME = ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(2, Exams.selectedExam);
                    preparedStatement.setString(1, String.valueOf(MainApp.ar[j]));
                    ResultSet hmm = preparedStatement.executeQuery();
                    hmm.next();
                    agda3agweba[j] = hmm.getString(1);
                }
                }

        }

        // ana 3rft aslah mo3zam el m4akel
        //go go sasa
        catch(Exception e){
            e.printStackTrace();
        }
        for(int k=0;k<10;k++){
           Score+= (MainApp.RoWAns[k] * MainApp.diff[k]);
        }
        score.setText(String.valueOf(Score));
        queslabel1.setText(as2ela[0]);
        queslabel2.setText(as2ela[1]);
        queslabel3.setText(as2ela[2]);
        queslabel4.setText(as2ela[3]);
        queslabel5.setText(as2ela[4]);
        queslabel6.setText(as2ela[5]);
        queslabel7.setText(as2ela[6]);
        queslabel8.setText(as2ela[7]);
        queslabel9.setText(as2ela[8]);
        queslabel10.setText(as2ela[9]);
        modans1.setText(agda3agweba[0]);
        modans2.setText(agda3agweba[1]);
        modans3.setText(agda3agweba[2]);
        modans4.setText(agda3agweba[3]);
        modans5.setText(agda3agweba[4]);
        modans6.setText(agda3agweba[5]);
        modans7.setText(agda3agweba[6]);
        modans8.setText(agda3agweba[7]);
        modans9.setText(agda3agweba[8]);
        modans10.setText(agda3agweba[9]);
        userans1.setText(Exams.UserAnss[0]);
        userans2.setText(Exams.UserAnss[1]);
        userans3.setText(Exams.UserAnss[2]);
        userans4.setText(Exams.UserAnss[3]);
        userans5.setText(Exams.UserAnss[4]);
        userans6.setText(Exams.UserAnss[5]);
        userans7.setText(Exams.UserAnss[6]);
        userans8.setText(Exams.UserAnss[7]);
        userans9.setText(Exams.UserAnss[8]);
        userans10.setText(Exams.UserAnss[9]);
        if(MainApp.RoWAns[0]==1) {
            queslabel1.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[1]==1) {
            queslabel2.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[2]==1) {
            queslabel3.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[3]==1) {
            queslabel4.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[4]==1) {
            queslabel5.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[5]==1) {
            queslabel6.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[6]==1) {
            queslabel7.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[7]==1) {
            queslabel8.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[8]==1) {
            queslabel9.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
        if(MainApp.RoWAns[9]==1) {
            queslabel10.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;");
        }
    }
    @FXML
    protected  void backToMenu() throws Exception {
        MainApp.switchForm("ChooseProcess.fxml");
    }
}// top el top
// rod 3lya yabny
// ya 3am enta
// mana lw kan 43ry taweel w 3nya zar2a w gesmy reyady w esmy jomana kont radet 3lya
//ma4oftak4 wallahi
//wassup jomana
//rod yabny brn 3leik
//mabeyrene4