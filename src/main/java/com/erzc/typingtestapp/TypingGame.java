package com.erzc.typingtestapp;

public class TypingGame {

    //private member variables
    private String gameResults = "";
    private long gameTime = 30;


    //Code > Generate > Constructor > None
    public TypingGame() {
    }

    private void calculate() {

        //Calculate round accuracy

        //Calculate total accuracy


        gameResults = "";
    }

    public void setResults(String gResult) {
        gameResults = gResult;
    }

    public void setTime(long time) {
        gameTime = time;
    }


    public String getGameResults()  {
        return gameResults;
    }

    public long GetTime() {
        return gameTime;
    }

    public String getResults() {
        return gameResults;
    }

}
