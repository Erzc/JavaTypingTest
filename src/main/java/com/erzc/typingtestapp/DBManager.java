package com.erzc.typingtestapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JOptionPane;

//Uses CRUD

public class DBManager {

    private Connection connection = null;
    private Statement statement = null;
    private final String TABLE_NAME = "TypingTestApp";
    private Result result = null;

    //Constructor established the connection to the database
    public DBManager(){

        try{
            // Instantiate/Establish a connection
            //Set the database name here.  Default is sample
            connection = DriverManager.getConnection("jdbc:sqlite:typingtestapp.db");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);

        }
    }


    //These arguments = columns of the db table, modify this--------------------------------------
    public void insert(int recordID, String jgameName, double jtotalWords, double jwpm, double jaccuracy){
        try {
            //Insert a new record into the database
            String insertQuery =
                    "INSERT INTO "+TABLE_NAME +
                            " VALUES (?, ?, ? , ?, ?)"; //format-----------------------------------------

            PreparedStatement  insertUpdate = null; //preparedstatment to protect database--------
            insertUpdate = connection.prepareStatement(insertQuery);

            insertUpdate.setInt(1, recordID); //called individually-------------------
            insertUpdate.setString(2, jgameName); //these are paramaterized inputs--------
            insertUpdate.setDouble(3, jtotalWords);
            insertUpdate.setDouble(4, jwpm);
            insertUpdate.setDouble(5, jaccuracy);
            insertUpdate.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLastID() {
        int ID = 0;
        ResultSet results;
        try {
            statement = connection.createStatement();

            String newQuery = "SELECT MAX(ID)"
                    + "FROM " + TABLE_NAME; //Select the max id from the tablename---------------
            results = statement.executeQuery(newQuery);

            if (results.next()) {
                ID = Integer.parseInt(results.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ID;
    }

    public String[] getRecordById(int recordID){

        String jgameName = "";
        String jtotalWords= "";
        String jwpm = "";
        String jaccuracy = "";

        try {
            PreparedStatement getRecordStmt = null;


            //Display the contents of the record

            String getRecordQuery = "SELECT * "
                    +"FROM "+TABLE_NAME
                    +" WHERE ID = ?";//+ recordID;
            getRecordStmt = connection.prepareStatement(getRecordQuery);
            getRecordStmt.setInt(1, recordID);

            ResultSet result = getRecordStmt.executeQuery();

            if(result.next())
            {
                jgameName = result.getString(2);
                jtotalWords = result.getString(3);
                jwpm = result.getString(4);
                jaccuracy = result.getString(5);
            }


        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] getRow = {jgameName, jtotalWords, jwpm, jaccuracy};
        return getRow; //Created array to return the row values------------------------------
        //getRow is assigned in the controller---------------------------------------------------
    }

    public void editRecord(int recordID, String jgameName, double jtotalWords, double jwpm, double jaccuracy){
        try {
            PreparedStatement  editStatement = null;
            //update the record
            String editQuery = "UPDATE " + TABLE_NAME
                    + " SET NAME = ?, WORDS = ?, WPM = ?, ACCURACY = ? WHERE ID = ?";
            editStatement = connection.prepareStatement(editQuery);

            editStatement.setString(1, jgameName);
            editStatement.setDouble(2, jtotalWords);
            editStatement.setDouble(3, jwpm);
            editStatement.setDouble(4, jaccuracy);
            editStatement.setInt(5, recordID);
            editStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRecord(int  recordID) {

        try {
            PreparedStatement deleteStmt = null;
            //Delete the record.
            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?"; //delete ID------
            deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, recordID);
            deleteStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Classes for creating the table and putting values in it.

    //Create a table
    public void createTable(){
//P8: These are the Primary keys and columns-----------------------------------------------------
        //  statement.executeUpdate
        String sql = "CREATE TABLE IF NOT EXISTS TypingTestApp ( \n"
                + "   id integer PRIMARY KEY, \n"
                + "   name string NOT NULL, \n"
                + "   words double NOT NULL, \n"
                + "   wpm double NOT NULL, \n"
                + "   accuracy double Not Null\n"
                + ");";
        //Using SQLlite ^^^ ---------------------------------------------------------------------
        try { //Need try-catch for every sql function you execute--------------------------------
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void populateDatabase(){ //inserts [initial] elements to the table---------------------

        insert(1, "Round 1", 40.0, 80, 98.00);

    }

    public void dropTable(){

        try {
            statement = connection.createStatement();

            //Be sure to change the name of the table
            String drop = "Drop Table " +TABLE_NAME+ " " ;
            statement.execute(drop);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
