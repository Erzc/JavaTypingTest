package com.erzc.typingtestapp;

import java.text.DecimalFormat;

public class TypingGame {

    //------------------------
    //Private member variables
    private String gameResults = "";
    private double gameTime = 30, corrChars = 0, incorrChars = 0, totalWords = 0, accuracy = 0, wpm = 0;

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
    public double GetWPM() {
        return wpm;
    }
    public double GetTotalWords() {
        return totalWords;
    }
    public double GetAccuracy() {
        return accuracy;
    }


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

        //Format to no decimal places
        gameTime = Math.round(gameTime);
        corrChars = Math.round(corrChars);
        incorrChars = Math.round(incorrChars);
        totalWords = Math.round(totalWords);
        wpm = Math.round(wpm);

        //Format to two decimal places
        accuracy = Math.round(accuracy * 100.0) / 100.0;


        gameResults = "Typing Game Results: \n" +
                "Time: " + gameTime + " seconds\n" +
                "Correct characters typed: " + corrChars + "\n" +
                "Incorrect characters typed: " + incorrChars + "\n" +
                "Total words typed: " + totalWords + "\n\n" +
                "Words per minute: " + wpm + "\n" +
                "Accuracy: " + accuracy + "%\n";
    }

}
