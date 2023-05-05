package com.erzc.typingtestapp;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
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

    @FXML
    private Label lblTime;
    @FXML
    private ComboBox<String> cbxTimer;

    //------------------------
    //Instantiate objects
    TypingGame typeGame = new TypingGame();
    Random rand = new Random();

    //------------------------
    //Private class variables
    private String gameTotals = "";
    private char character = 'a';
    private String word = "";
    private double clock = 0.1;
    private int tempWordIndex = 0;
    private int tempPoints = 0;
    private long startTime = 0;
    long numTime = 0;
    //ListView requires ObservableList
    private ObservableList<String> sentenceList = FXCollections.observableArrayList();
    private ObservableList<String> cbList = FXCollections.observableArrayList("10", "30", "60", "90");
    private ArrayList<String> commonWords = new ArrayList<String>();

    //------------------------
    //Methods
    @FXML
    void closeOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        //Enable textfield again
        tfTypeHere.setDisable(false);

        startTimer();

        displayResults();
    }

    @FXML
    public void startTimer() {

        //cbxTimer.setDisable(true);

        //DoubleProperty is mutable and observed for changes, useful for data bindings
        DoubleProperty time = new SimpleDoubleProperty();
        lblTime.textProperty().bind(time.asString("%.1f"));

        BooleanProperty running = new SimpleBooleanProperty();
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                running.set(true);
                super.start();
                newWord(); //Get a new word
                tempWordIndex = 0;
                tempPoints = 0;
                cbxTimer.setDisable(true);
                btnStart.setDisable(true);
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
                cbxTimer.setDisable(false);
                btnStart.setDisable(false);
                lvPrompts.getItems().clear(); //clear listview
            }

//            @Override
//            public void handle(long timestamp) {
//                long now = System.currentTimeMillis();
//                time.set((now - startTime) / 1000.0);
//            }

            @Override
            public void handle(long timestamp) {
                long limit = typeGame.GetTime() * 1000;
                long now = System.currentTimeMillis();

                clock = (limit - (now - startTime)) / 1000.0;

                time.set(clock);

                if (clock > 0) {
                    time.set(clock);
                }
                else {
                    time.set(0.0);
                    stop();
                }
            }
        };

        //timer.start();


//        btnStart.textProperty().bind(
//                Bindings.when(running)
//                        .then("RUNNING")
//                        .otherwise("START"));

        btnStart.setOnAction(e -> {
            if (running.get()) {
                timer.stop();
            } else {
                timer.start();
            }
        });

    }

    @FXML
    public void newWord() {


        int index = rand.nextInt(commonWords.size());
        //System.out.println(commonWords.get(index));

        word = commonWords.get(index);

        lvPrompts.getItems().add(0, word);


    }

    @FXML
    void typeOnKeyTyped(KeyEvent event) {

        //character = event.getCharacter();

        character = event.getCharacter().charAt(0);
        //System.out.println("Key pressed: " + character);

        if (tempWordIndex < word.length())
        {
            if (character == word.charAt(tempWordIndex))
            {
                tempPoints++;
                tempWordIndex++;
            }
            else
            {
                tempPoints--;
            }
        }
        else
        {
            tempWordIndex = 0;
            tfTypeHere.setText(""); //clear textfield in order to type a new word
            newWord(); //Get a new word
        }


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
    void aboutOnAction(ActionEvent event) {

        //create a message about the Typing Test and display with a JOP
        JOptionPane.showMessageDialog(null, "Typing Test is a JavaFX application written by Eric N.");
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

        //Print full word list:
//        for (String element : commonWords) {
//            System.out.println(element);
//        }
    }

    @FXML
    void onActionComboBoxEvent(ActionEvent event) {

        //Check if combobox is empty
        boolean isCBXEmpty = cbxTimer.getSelectionModel().isEmpty();

        if (isCBXEmpty){
            JOptionPane.showMessageDialog(null, "Error! Please select a time");
        }
        else
        {
            String time = cbxTimer.getValue();
            numTime = Long.parseLong(time); //convert string to double
            typeGame.setTime(numTime);
            lblTime.setText(Long.toString(numTime));
        }


    }

//    @FXML
//    private void setTimerLabel() {
//
//    }


    //Initializer method
    @FXML
    private void initialize(){
        //Disable textbox on initial load
        tfTypeHere.setDisable(true);

        openFile(); //Open txt document and build arraylist of words

        //Initialize combobox to value
        cbxTimer.setValue("Set the timer");

        //put the list of strings into the combobox
        cbxTimer.setItems(cbList);

        //get the selected index, single selection then getSelectedIndex
        int index = cbxTimer.getSelectionModel().getSelectedIndex();

    }


}
