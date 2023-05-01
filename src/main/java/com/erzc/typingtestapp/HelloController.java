package com.erzc.typingtestapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.logging.*;

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
    Timer timer = new Timer();

    //Private class variables
    private String gameTotals = "";
    //ListView requires ObservableList
    private ObservableList<String> sentenceList = FXCollections.observableArrayList();
    ArrayList<String> commonWords = new ArrayList<String>();

    @FXML
    void closeOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        //Enable textfield again
        tfTypeHere.setDisable(false);

        startTimer();

        runGame();

        int index = rand.nextInt(commonWords.size());
        System.out.println(commonWords.get(index));

        displayResults();
    }

    @FXML
    public void startTimer() {


    }

    @FXML
    public void runGame() {


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

    @FXML
    public void openFile() {

        //Efficient way to read text
        try (BufferedReader buffRead = new BufferedReader(new FileReader("./1-1000.txt"))) {
            String line;
            while ((line = buffRead.readLine()) != null) {
                commonWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //To print full word list:
//        for (String element : commonWords) {
//            System.out.println(element);
//        }
    }



    //Initializer method
    @FXML
    private void initialize(){
        //Disable textbox on initial load
        tfTypeHere.setDisable(true);

        openFile(); //Open txt document and build arraylist of words

    }




}