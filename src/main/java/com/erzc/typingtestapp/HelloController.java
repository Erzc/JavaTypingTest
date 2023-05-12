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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.*;

public class HelloController {

    @FXML
    private Button btnStart;
    @FXML
    private Button btnViewDb;
    @FXML
    private ListView<String> lvPrompts;
    @FXML
    private TextArea taSummary;
    @FXML
    private TextField tfTypeHere;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblCorrect;
    @FXML
    private Label lblIncorrect;
    @FXML
    private Label lblWordPrompt;
    @FXML
    private ComboBox<String> cbxTimer;

    //------------------------
    //Instantiate objects
    TypingGame typeGame = new TypingGame();
    Random rand = new Random();

    //------------------------
    //Private class variables
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String gameTotals = "", word = "", phrase = "";;
    private char character = 'a';
    private double clock = 0.1;
    private int tempLetterIndex = 0, tempWordIndex = 0, corrCharCount = 0, incorrCharCount = 0, totalWordsTyped = 0;
    private long startTime = 0, numTime = 0;

    private ObservableList<String> cbList = FXCollections.observableArrayList("10", "30", "60", "90");
    private ArrayList<String> commonWords = new ArrayList<String>();
    private ArrayList<String> gameWords = new ArrayList<String>();
    ObservableList<String> wordsOL = FXCollections.observableArrayList();

    //------------------------
    //Methods
    @FXML
    void closeOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void switchToDbScene(ActionEvent event) {
        Stage oldStage = (Stage)btnViewDb.getScene().getWindow();
        oldStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("db-view.fxml"));
            root = loader.load();

            DBController controller = loader.getController();

            wordsOL = lvPrompts.getItems();
            controller.transferData(typeGame.getGameResults(), typeGame.GetTotalWords(), typeGame.GetWPM(),
           typeGame.GetAccuracy(), wordsOL);

            //show scene in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Typing Test Database");
            stage.show();


        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @FXML
    public void transferData(ObservableList<String> wordsOLDB, String roundResultsDB) {

        wordsOL = wordsOLDB;
        String roundResults = roundResultsDB;

        lvPrompts.setItems(wordsOL);
        taSummary.setText(roundResults);


    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {

        startTimer();

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
                tempLetterIndex = 0;
                corrCharCount = 0;
                incorrCharCount = 0;
                totalWordsTyped = 0;
                tfTypeHere.setDisable(false);
                cbxTimer.setDisable(true);
                btnStart.setDisable(true);
                lblWordPrompt.setVisible(true);
                lvPrompts.getItems().clear(); //clear listview
                tfTypeHere.setText(""); //clear textfield
                lblIncorrect.setText("0");
                lblCorrect.setText("0");
                tfTypeHere.requestFocus();
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
                tfTypeHere.setDisable(true);
                cbxTimer.setDisable(false);
                btnStart.setDisable(false);
                lblWordPrompt.setVisible(false);

                typeGame.setCorrect(corrCharCount);
                typeGame.setIncorrect(incorrCharCount);
                typeGame.setTotalWords(totalWordsTyped);

                taSummary.setText(typeGame.getGameResults());
            }

//            @Override
//            public void handle(long timestamp) {
//                long now = System.currentTimeMillis();
//                time.set((now - startTime) / 1000.0);
//            }

            @Override
            public void handle(long timestamp) {
                double limit = typeGame.GetTime() * 1000;
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
    public void newWord(int wordNum) {

        for (int i = 0; i < wordNum; i++)
        {
            int j = rand.nextInt(commonWords.size()); //Random integer
            word = commonWords.get(j); //Get a random string by index and assign to word
            gameWords.add(word);
            phrase += word + " ";
        }

        lblWordPrompt.setText(phrase);

        phrase = phrase.substring(phrase.indexOf(" ")+1); //remove first word

    }


    @FXML
    void typeOnKeyTyped(KeyEvent event) {

        character = event.getCharacter().charAt(0);
        //KeyCode checkBspc = event.getCode();

        //System.out.println("Key pressed: " + character);

        String newestWord = gameWords.get(tempWordIndex);


        if (tempLetterIndex < newestWord.length())
        {
            //Check key entered is not a backspace
            //if (event.getCode() != "\b") {
                if (character == newestWord.charAt(tempLetterIndex)) {
                    corrCharCount++;
                    tempLetterIndex++;
                    lblCorrect.setText(Long.toString(corrCharCount));
                }
                else {
                    incorrCharCount++;
                    lblIncorrect.setText(Long.toString(incorrCharCount));
                }
            //}
        }
        else
        {
            tempLetterIndex = 0;
            tfTypeHere.setText(""); //clear textfield in order to type a new word
            //lvPrompts.getItems().add(0, word); //Add word to listview
            lvPrompts.getItems().add(0, gameWords.get(tempWordIndex));
            tempWordIndex++;

            totalWordsTyped++;
            newWord(1); //Get a new word
        }

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
        else {
            String time = cbxTimer.getValue();
            numTime = Long.parseLong(time); //convert string to double
            typeGame.setTime(numTime);
            lblTime.setText(Long.toString(numTime));
        }

    }

    //Initializer method
    @FXML
    private void initialize(){
        //Disable UI elements
        tfTypeHere.setDisable(true);
        lblWordPrompt.setVisible(false);

        openFile(); //Open txt document and build arraylist of words

        //Initialize combobox to value
        cbxTimer.setValue("Set the timer");

        //put the list of strings into the combobox
        cbxTimer.setItems(cbList);

        //get the selected index, single selection then getSelectedIndex
        int index = cbxTimer.getSelectionModel().getSelectedIndex();

        newWord(7); //Create 7 new words to display

    }

}
