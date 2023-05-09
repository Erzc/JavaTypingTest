package com.erzc.typingtestapp;

import java.text.DecimalFormat;

public class TypingGame {

    //------------------------
    //Private member variables
    private String gameResults = "", accuracyFormat = "";
    private double gameTime = 30, corrChars = 0, incorrChars = 0, totalWords = 0, accuracy = 0, wpm = 0;


    //Code > Generate > Constructor > None
    public TypingGame() {
    }

    //------------------------
    //Methods

    public void setResults(String gResult) {
        gameResults = gResult;
    }

    public void setTime(double time) {
        gameTime = time;
    }

    public String getGameResults()  {
        return gameResults;
    }

    public double GetTime() {
        return gameTime;
    }


    public void setCorrect(double corrCh) { corrChars = corrCh; }

    public void setIncorrect(double incorrCh) { incorrChars = incorrCh; }

    public void setTotalWords(double totalW) { totalWords = totalW; calculate(); }

    private void calculate() {

        //Calculate WPM
        wpm = (totalWords / gameTime) * 60;

        //Calculate round accuracy
        accuracy = (corrChars / (incorrChars + corrChars)) * 100;

        //Format to 3 decimal places
        DecimalFormat df = new DecimalFormat("#.###");
        accuracyFormat = df.format(accuracy);

        formatResults();
    }

    private void formatResults() {

        gameResults = "Typing Game Results: \n" +
                "Time: " + gameTime + " seconds\n" +
                "Correct characters typed: " + corrChars + "\n" +
                "Incorrect characters typed: " + incorrChars + "\n" +
                "Total words typed: " + totalWords + "\n\n" +
                "Words per minute: " + wpm + "\n" +
                "Accuracy: " + accuracyFormat + "%\n";

    }


}
