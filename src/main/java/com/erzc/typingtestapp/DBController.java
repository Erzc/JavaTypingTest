package com.erzc.typingtestapp;

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


    //------------------------
    //Methods





    //int recordID, double jgameName, double jtotalWords, double jwpm, double jaccuracy







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
            //use the instance to access the transferData method and pass a String
            //controller.transferData(finScore);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(IOException ex) {
            System.err.println(ex);
        }

    }

    @FXML
    public void transferData(String mess) {
        lblResults.setText(mess);
    }


    //Initialize would go here


}
