/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server.transaction;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */


public class TransactionManager extends Thread {
	/**
        Class Variables
    */
    private static int numTransactions = 0;
    private static final ArrayList<Transaction> transactionList = new ArrayList<>();
    
    /**
         Default Constructor
    */
    public TransactionManager() 
	 {
     
    }

    public ArrayList<Transaction> getTransactions() 
	 {
        // returns the list of current transactions
    }

    public void runTransaction(Socket socket) 
	 {
    
    }
}
