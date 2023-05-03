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
import javafx.stage.FileChooser;

import javax.swing.*;
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
    double clock = 0.1;
    private long startTime ;
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

        runGame();

        int index = rand.nextInt(commonWords.size());
        System.out.println(commonWords.get(index));

        displayResults();
    }

    @FXML
    public void startTimer() {

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
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
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
            long numTime = Long.parseLong(time); //convert string to double
            typeGame.setTime(numTime);
            lblTime.setText(Long.toString(numTime));
        }


    }


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
