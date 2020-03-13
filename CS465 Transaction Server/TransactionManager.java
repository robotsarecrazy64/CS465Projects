import java.io.*;
import java.net.*;
import Java.util.ArrayList;

public class TransactionManager extends Thread {
    private static int numTransactions = 0;
    private static final ArrayList<Transaction> transactionList = new ArrayList<>();
    
    /**
         Default Constructor
    */
    public TransactionManager() {
     
    }

    public ArrayList<Transaction> getTransactions() {
        // returns the list of current transactions
    }

    public void runTransaction(Socket socket) {
    
    }
}