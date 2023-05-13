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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;

public class DBController {

    @FXML
    private Label lblResults;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnDisplay;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnEnterID;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnBack;
    @FXML
    private ListView<String> lvDisplay;
    @FXML
    private TextField txtFieldAccuracy;
    @FXML
    private TextField txtFieldName;
    @FXML
    private TextField txtFieldWPM;
    @FXML
    private TextField txtFieldWords;
    @FXML
    private TextField txtfieldID;

    //------------------------
    //Private class variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    int recordIDDB = 0;
    String gameNameDB = "Round 1", roundResultsDB = "";
    double totalWordsDB = 0.0, wpmDB = 0.0, accuracyDB = 0.0;

    //Instantiate arraylist because the ListView<String> requires an ObservableList
    ObservableList<String> wordsOL = FXCollections.observableArrayList();
    private ObservableList<String> roundList = FXCollections.observableArrayList();

    //Instantiate object of the DBManager class
    //private DBManager managerDB = new DBManager();
    private DBManager managerDB;

    //------------------------
    //Methods

    //Event handlers
    @FXML
    private void handleInsertButton(ActionEvent event) {

        //Pick up 5 values each time
        getRecordValues();
        //Insert at the end, recordID is what comes back from getLastID
        int recordLastID = managerDB.getLastID();

        //add 1 to recordID for the new record
        //call the insertMethod in the DBManager and pass the values obtained

        //Check if fields were not entered
        if (recordIDDB == -123 | gameNameDB == "Please enter ID" || totalWordsDB == -1.0 || wpmDB == -1.0 || accuracyDB == -1.0)
        {
            JOptionPane.showMessageDialog(null, "Error! Fields cannot be empty.");
        }
        else {
            managerDB.insert(recordLastID + 1, gameNameDB, totalWordsDB, wpmDB, accuracyDB);
            clearTextFields(); //clear textfields for user
        }

        displayDB(); //Display the table
    }



    //To edit, enter ID, click ENTER ID button, edit the field(s) you want, then click EDIT button
    @FXML
    private void handleEditButton(ActionEvent event){

        getRecordValues();

        //Calls the editRecord method in the DBManager, passing the values from the textfields
        managerDB.editRecord(recordIDDB, gameNameDB, totalWordsDB, wpmDB, accuracyDB);

        clearTextFields();
        displayDB();
    }

    @FXML
    private void handleEnterIDButton(ActionEvent event) {

        //Get the recordIDDB entered in the recordID textfield
        getRecordValues();

        //Create an array of num of columns (currently 4 via. String[] getRow in DBManager, does not count ID)
        //array has null pointers
        String[] enterRecord = new String[4];

        //Fill the row with the values corresponding to recordID
        //getRecordByID returns the array of values (via. "return getRow" in DBManager)
        enterRecord = managerDB.getRecordById(recordIDDB);

        //Set the values from the array into the textfields
        txtFieldName.setText(enterRecord[0]);
        txtFieldWords.setText(enterRecord[1]);
        txtFieldWPM.setText(enterRecord[2]);
        txtFieldAccuracy.setText(enterRecord[3]);

    }

    @FXML
    private void handleDisplayButton(ActionEvent event) {
        displayDB();
    }

    @FXML
    private void getRecordValues(){
        //extract the text values from the textFields, converts if necessary, and assigns to the
        //class member variables (i.e. columns in the db)

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
        else {
            String sRecordID = txtfieldID.getText();
            recordIDDB = Integer.parseInt(sRecordID); //convert String to int

            gameNameDB = txtFieldName.getText();

            String sTotalWords = txtFieldWords.getText();
            totalWordsDB = Double.parseDouble(sTotalWords);

            String sWpm = txtFieldWPM.getText();
            wpmDB = Double.parseDouble(sWpm);

            String sAccuracy = txtFieldAccuracy.getText();
            accuracyDB = Double.parseDouble(sAccuracy);
        }

    }

    private void clearTextFields(){
        //clear all the text fields
        txtfieldID.clear();
        txtFieldName.clear();
        txtFieldWords.clear();
        txtFieldWPM.clear();
        txtFieldAccuracy.clear();
    }

    //DISPLAY button displays the db in the ListView
    private void displayDB(){

        //clear the observable List object
        roundList.clear();

        int numRecord = managerDB.getLastID(); //if numRecords = 10, then have 10 records

        //create a local array for records
        String[] record = new String[4]; //4 = num columns

        for (int i = 1; i <= numRecord; i++)
        {
            record = managerDB.getRecordById(i);
            //check if record[0] has blank row
            if (record[0].isBlank()) {
                continue;
            }
            else {
                //Add a String to the roundList that describes the row and its values
                roundList.add(i + " | " + record[0] + " | " + record[1] + " | " + record[2] + " | " + record[3]);
            }
        }

        //set the roundList into the listView
        lvDisplay.setItems(roundList);

    }


    @FXML
    private void handleDeleteButton(ActionEvent event) {
        //get the record ID entered in the textfield
        getRecordValues();

        if (recordIDDB == -123) {
            JOptionPane.showMessageDialog(null, "Error! Please enter a record ID to delete.");
        }
        else {
            managerDB.deleteRecord(recordIDDB);
            clearTextFields(); //clear textfields for user
            displayDB();
        }
    }


    @FXML
    void switchToHelloView(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

            root = loader.load();

            //create an instance for the HelloController:
            HelloController controller = loader.getController();
            //use the instance to access the transferData method and pass variables
            controller.transferData(wordsOL, roundResultsDB, managerDB);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(IOException ex) {
            System.err.println(ex);
        }

    }

    @FXML
    public void transferData(DBManager managerC, String resultsC, double wordsC, double wpmC,
                             double accuracyC, ObservableList<String> wordsOLC) {

        managerDB = managerC;
        roundResultsDB = resultsC;
        totalWordsDB = wordsC;
        wpmDB = wpmC;
        accuracyDB = accuracyC;
        wordsOL = wordsOLC;
    }

    
/*
    //Initializer method
    @FXML
    private void initialize(){

    }
*/


}
