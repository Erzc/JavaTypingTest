package com.erzc.typingtestapp;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.paint.Color;
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
    private Label lblPoints;
    @FXML
    private Label lblWordPrompt;

    @FXML
    private Label lblWordPrompt1;

    @FXML
    private Label lblWordPrompt2;

    @FXML
    private Label lblWordPrompt3;
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
    private String word = "", word1 = "", word2 = "", word3 = "";
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

        lvPrompts.getItems().clear(); //clear listview
        tfTypeHere.setText(""); //clear textfield

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
                newWordGroup(); //Get a new word
                tempWordIndex = 0;
                tempPoints = 0;
                tfTypeHere.setDisable(false);
                cbxTimer.setDisable(true);
                btnStart.setDisable(true);
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
                tfTypeHere.setDisable(true);
                cbxTimer.setDisable(false);
                btnStart.setDisable(false);
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

        timer.start();


        btnStart.textProperty().bind(
                Bindings.when(running)
                        .then("RUNNING")
                        .otherwise("START"));

        btnStart.setOnAction(e -> {
            if (running.get()) {
                timer.stop();
            } else {
                timer.start();
            }
        });

    }

    @FXML
    public void newWordGroup() {

        int index = 0, index1 = 0, index2 = 0, index3 = 0;

        index = rand.nextInt(commonWords.size());
        index1 = rand.nextInt(commonWords.size());
        index2 = rand.nextInt(commonWords.size());
        index3 = rand.nextInt(commonWords.size());
        //System.out.println(commonWords.get(index));

        word = commonWords.get(index);
        word1 = commonWords.get(index1);
        word2 = commonWords.get(index2);
        word3 = commonWords.get(index3);

        lblWordPrompt.setText(word);
        lblWordPrompt1.setText(word1);
        lblWordPrompt2.setText(word2);
        lblWordPrompt3.setText(word3);

    }

    @FXML
    public void newWord() {

        int index = 0;

        index = rand.nextInt(commonWords.size());
        //System.out.println(commonWords.get(index));

        word = word1;
        word1 = word2;
        word2 = word3;
        word3 = commonWords.get(index);

        lblWordPrompt.setText(word);
        lblWordPrompt1.setText(word1);
        lblWordPrompt2.setText(word2);
        lblWordPrompt3.setText(word3);

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
                lblPoints.setText(Long.toString(tempPoints));
                lblPoints.setTextFill(Color.color(0, 1, 0));
            }
            else
            {
                tempPoints--;
                lblPoints.setText(Long.toString(tempPoints));
                lblPoints.setTextFill(Color.color(1, 0, 0));
            }
        }
        else
        {
            tempWordIndex = 0;
            tfTypeHere.setText(""); //clear textfield in order to type a new word
            lvPrompts.getItems().add(0, word); //Add word to listview
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
