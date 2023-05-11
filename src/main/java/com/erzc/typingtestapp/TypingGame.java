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

        //Format decimal place for displaying to user
        DecimalFormat dfTwo = new DecimalFormat("#.##"); //Format to 2 decimal places
        DecimalFormat dfOne = new DecimalFormat("#");

        String accuracyFormat = dfTwo.format(accuracy);

        String gameTimeFormat = dfOne.format(gameTime);
        String corrCharsFormat = dfOne.format(corrChars);
        String incorrCharsFormat = dfOne.format(incorrChars);
        String totalWordsFormat = dfOne.format(totalWords);
        String wpmFormat = dfOne.format(wpm);

        //String to display to user
        gameResults = "Typing Game Results: \n" +
                "Time: " + gameTimeFormat + " seconds\n" +
                "Correct characters typed: " + corrCharsFormat + "\n" +
                "Incorrect characters typed: " + incorrCharsFormat + "\n" +
                "Total words typed: " + totalWordsFormat + "\n\n" +
                "Words per minute: " + wpmFormat + "\n" +
                "Accuracy: " + accuracyFormat + "%\n";

    }


}
