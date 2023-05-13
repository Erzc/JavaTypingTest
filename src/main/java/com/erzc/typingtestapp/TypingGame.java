package com.erzc.typingtestapp;

import java.text.DecimalFormat;

public class TypingGame {

    //------------------------
    //Private member variables
    private String gameResults = "";
    private double gameTime = 30, corrChars = 0, incorrChars = 0, totalWords = 0, accuracy = 0, wpm = 0;
    private int gameTimeI = 30, corrCharsI = 0, incorrCharsI = 0, totalWordsI = 0, wpmI = 0;

    //Code > Generate > Constructor > None
    public TypingGame() {
    }

    //------------------------
    //Methods


    public String getGameResults()  {
        return gameResults;
    }
    public double GetTime() {
        return gameTime;
    }
    public int GetWPM() {
        return wpmI;
    }
    public int GetTotalWords() {
        return totalWordsI;
    }
    public double GetAccuracy() {
        return accuracy;
    }


    //Implicitly converted to doubles for calculation
    public void setTime(double time) {
        gameTime = time;
    }
    public void setCorrect(double corrCh) { corrChars = corrCh; }
    public void setIncorrect(double incorrCh) { incorrChars = incorrCh; }
    public void setTotalWords(double totalW) { totalWords = totalW; calculate(); }

    private void calculate() {

        //Calculate WPM
        wpm = (totalWords / gameTime) * 60;

        //Calculate round accuracy
        accuracy = (corrChars / (incorrChars + corrChars)) * 100;

        formatResults();
    }

    private void formatResults() {

        //Cast doubles back to ints for displaying and inserting in database
        corrCharsI = (int)corrChars;
        incorrCharsI = (int)incorrChars;
        gameTimeI = (int)gameTime;
        totalWordsI = (int)totalWords;
        wpmI = (int)wpm;

        //Format to two decimal places
        accuracy = Math.round(accuracy * 100.0) / 100.0;


        gameResults = "Typing Game Results: \n" +
                "Time: " + gameTimeI + " seconds\n" +
                "Correct characters typed: " + corrCharsI + "\n" +
                "Incorrect characters typed: " + incorrCharsI + "\n" +
                "Total words typed: " + totalWordsI + "\n\n" +
                "Words per minute: " + wpmI + "\n" +
                "Accuracy: " + accuracy + "%\n";
    }

}
