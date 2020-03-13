import java.io.*;
import java.net.*;
import java.util.*;

public class TransactionServer extends Thread{
    /**
        Class Variables
    */
    static ServerSocket socket;
    public static boolean transaction;
    public static AccountManager account;
    public static TransactionManager transactionManager;
    public static LockManager lock;

    /**
        Default Constructor for connection
    */
    public TransactionServer() {
        
    }

    public void log() {
        // allows you to attach any info to the transactions
    }

    public void run() {
        // run method
    }
}


