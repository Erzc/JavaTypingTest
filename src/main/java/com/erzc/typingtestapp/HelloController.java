package com.erzc.typingtestapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import java.io.IOException;
import java.util.Random;
import java.util.logging.*;

import java.io.File;
import java.io.PrintWriter;

public class HelloController {

    @FXML
    private Button btnStart;

    @FXML
    private ListView<String> lvPrompts;

    @FXML
    private TextArea taSummary;

    @FXML
    private TextField tfTypeHere;

    //Instantiate objects
    TypingGame typeGame = new TypingGame();
    Random rand = new Random();

    //Private class variables
    private String gameTotals = "";
    //arraylist for the ListView, which requires an ObservableList
    private ObservableList<String> sentenceList = FXCollections.observableArrayList();
    String[] wordArr = {"blue", "red", "orange", "purple"};

    @FXML
    void closeOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        //Enable textfield again
        tfTypeHere.setDisable(false);

        String randElement = wordArr[rand.nextInt(wordArr.length)];

        System.out.println(randElement);

        displayResults();
    }

    @FXML
    void typeOnKeyTyped(KeyEvent event) {

        String character = event.getCharacter();

        System.out.println("Key pressed: " + character);

    }

    @FXML
    void displayResults()
    {


        //typeGame.setResults();

    }

    //Saves final game and round results in a text file
    @FXML
    void saveFileOnAction(ActionEvent event) {

        //Create FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("Save your Typing Test game results in a File");
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show the Save File Dialog
        File file = fileChooser.showSaveDialog(null);

        if(file != null)
        {
            PrintWriter outputFile = null;
            try {
                String filename = file.getCanonicalPath();
                File myFile = new File(filename);
                outputFile = new PrintWriter(filename);
                outputFile.println(typeGame.getGameResults()); //-------------------------

                outputFile.close();

            } catch (IOException ex) {
                Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    //Initializer method
    @FXML
    private void initialize(){
        //Disable textbox on initial load
        tfTypeHere.setDisable(true);

    }




}