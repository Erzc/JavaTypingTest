package com.erzc.typingtestapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DBController {

    @FXML
    private Label lblResults;

    //------------------------
    //Private class variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    int recordIDDB = 0;
    String gameNameDB = "Round 1", roundResultsDB = "";
    double totalWordsDB = 0.0, wpmDB = 0.0, accuracyDB = 0.0;
    ObservableList<String> wordsOL = FXCollections.observableArrayList();

    //Create an object of the DBManager class to use
    private DBManager manager = new DBManager();

    //------------------------
    //Methods








    @FXML
    void switchToHelloView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

            root = loader.load();

            //Call calculate score method, return final score
            //calculateScore();
            ///getFinalScore();

            //create an instance for the HelloController:
            HelloController controller = loader.getController();
            //use the instance to access the transferData method and pass an observable list
            controller.transferData(wordsOL, roundResultsDB);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(IOException ex) {
            System.err.println(ex);
        }

    }

    @FXML
    private void getRecordValues(){
        //extract the text values from the textFields, converts if necessary, and assigns to the
        //class member variables i.e. columns in the db


        /*

        if (txtFieldName.getText().isEmpty() ||
                txtFieldWords.getText().isEmpty() ||
                txtFieldWPM.getText().isEmpty() ||
                txtFieldAccuracy.getText().isEmpty())
        {
            //To get record values if ID is entered
            if (txtfieldID.getText().isEmpty()) {
                recordIDDB = -123;
            }
            //Otherwise, return -1 so field can't be set later
            else {
                recordIDDB = -123;
                String sRecordID = txtfieldID.getText();
                recordIDDB = Integer.parseInt(sRecordID); //convert String to int
            }

            gameNameDB = "Please enter ID";
            totalWordsDB = -1.0;
            wpmDB = -1.0;
            accuracyDB = -1.0;
        }
        else
        {
            String sRecordID = txtfieldID.getText();
            recordIDDB = Integer.parseInt(sRecordID); //convert String to int

            gameNameDB = txtFieldName.getText();

            String sTotalWords = txtFieldWords.getText();
            totalWordsDB = Double.parseDouble(sTotalWords)

            String sWpm = txtFieldWPM.getText();
            wpmDB = Double.parseDouble(sWpm)

            String sAccuracy = txtFieldAccuracy.getText();
            accuracyDB = Double.parseDouble(sAccuracy)
        }



         */

    }



    @FXML
    public void transferData(String resultsC, double wordsC, double wpmC,
                             double accuracyC, ObservableList<String> wordsOLC) {

        roundResultsDB = resultsC;
        totalWordsDB = wordsC;
        wpmDB = wpmC;
        accuracyDB = accuracyC;
        wordsOL = wordsOLC;

        lblResults.setText(resultsC);
    }


    //Initialize would go here


}
