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
import javafx.scene.input.KeyCode;
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
    private TypingGame typeGame = new TypingGame();
    private Random rand = new Random();
    private DBManager manager = new DBManager();

    //------------------------
    //Private class variables
    private Parent root;
    private String gameTotals = "", word = "", phrase = "";;
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
        //oldStage.hide();
        oldStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("db-view.fxml"));
            root = loader.load();

            DBController controller = loader.getController();

            wordsOL = lvPrompts.getItems();
            controller.transferData(manager, typeGame.getGameResults(), typeGame.GetTotalWords(), typeGame.GetWPM(),
            typeGame.GetAccuracy(), wordsOL);

            //show scene in new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Typing Test Database");
            //stage.showAndWait();
            newStage.show();


        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @FXML
    public void transferData(ObservableList<String> wordsOLDB, String roundResultsDB, DBManager managerDB) {
        //Set parameters to class variables to use again
        manager = managerDB;
        wordsOL = wordsOLDB;
        String roundResults = roundResultsDB;

        lvPrompts.setItems(wordsOL);
        taSummary.setText(roundResults);
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {

        btnViewDb.setDisable(true);

        startTimer();

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

                btnViewDb.setDisable(false);

                InsertRow(); //Add typing round row to the database

                taSummary.requestFocus();
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

        lblWordPrompt.setText(phrase); //Set new phrase string into label
        phrase = phrase.substring(phrase.indexOf(" ")+1); //remove first word
    }


    @FXML
    void typeOnKeyPressed(KeyEvent event) {

        //Local variables
        String newestWord = gameWords.get(tempWordIndex);
        char character = ' ';
        KeyCode kc = event.getCode();

        //Check if entered key is a letter, apostrophe/quote or space/enter
        //If so, convert the keycode to a char, and call compareChar method
        if (kc.isLetterKey()) {
            character = Character.toLowerCase(kc.getName().charAt(0));
            compareChar(newestWord, character);
        }
        else if (kc == KeyCode.QUOTE) {
            character = "'".charAt(0);
            compareChar(newestWord, character);
        }
        else if (kc == KeyCode.SPACE || kc == KeyCode.ENTER) {
            character = " ".charAt(0);
            compareChar(newestWord, character);
        }
        //System.out.println("Typed character: " + character);
    }


    @FXML
    void compareChar(String newWord, char userChar){
        //Check if user has typed the full word
        if (tempLetterIndex < newWord.length())
        {
            //See if char entered matches the next character in the word
            if (userChar == newWord.charAt(tempLetterIndex)) {
                corrCharCount++;
                tempLetterIndex++;
                lblCorrect.setText(Long.toString(corrCharCount));
            }
            else {
                incorrCharCount++;
                lblIncorrect.setText(Long.toString(incorrCharCount));
            }
        }
        else {
            //Check whether the letter entered is a space, if so, get a new word
            if (Character.isWhitespace(userChar))
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
    }


    //Save round results in a text file
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
    public void InsertRow() {
        //Insert new row at the end, recordID is what comes back from getLastID
        int recordLastID = manager.getLastID();

        //add 1 to recordID for the new record
        //call the insertMethod in the DBManager and pass the values obtained
        manager.insert(recordLastID + 1, "Round " + (recordLastID + 1), typeGame.GetTotalWords(),
                typeGame.GetWPM(), typeGame.GetAccuracy());
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


        //Uncomment to create initial database-------------------------------------------------------------------
        //manager.createTable();
       // manager.populateDatabase();

    }

}
