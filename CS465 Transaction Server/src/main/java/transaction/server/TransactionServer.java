/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import transaction.server.account.AccountManager;
import transaction.server.lock.LockManager;
import transaction.server.transaction.TransactionManager;
import utils.PropertyHandler;

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

public class TransactionServer extends Thread
{
   /**
      Class Variables
   */
   static ServerSocket serverSocket;
   public static boolean transactionView = true;
   public static AccountManager accountManager = null;
   public static TransactionManager transactionManager = null;
   public static LockManager lockManager = null;
   
   /**
      Default Constructor for connection
   */
   public TransactionServer(String serverPropertiesFile) 
   {
      Properties serverProperties = null;

      try 
      {
           serverProperties = new PropertyHandler(serverPropertiesFile); // load the server properties file
      }
      catch (Exception error) 
      {
         // print the error if one occurred
         System.out.println("[TransactionServer.TransactionServer] Didnt find properties file");
         error.printStackTrace();
         System.exit(1);
      }
      
	  transactionView = Boolean.valueOf(serverProperties.getProperty("TRANSACTION_VIEW")); // gets the transaction view from the properties file
	  TransactionServer.transactionManager = new TransactionManager();
	  System.out.println("[TransactionServer.TransactionServer] TransactionManager created");
	  
	  boolean applyLock = Boolean.valueOf(serverProperties.getProperty("APPLY_LOCKING")); // gets the lock from the properties file
	  TransactionServer.lockManager = new LockManager(applyLock);
	  System.out.println("[TransactionServer.TransactionServer] LockManager created");
	  
	  int numAccounts = 0;
	  numAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS")); // gets the number of accounts from the properties file
	  int initBalance = 0;
	  initBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE")); // gets the initial balance from the properties file
	  
	  TransactionServer.accountManager = new AccountManager(numAccounts, initBalance);
	  System.out.println("[TransactionServer.TransactionServer] AccountManager created");
      try 
      {
          serverSocket = new ServerSocket(Integer.parseInt(serverProperties.getProperty("PORT"))); // gets the port number from the properties file
	      System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
      }
      
      catch (IOException error) 
      {
         // Prints the error if one occurred
         System.out.println("[TransactionServer.TransactionServer] Could not create Server Socket");
         error.printStackTrace();
         System.exit(1);
      }
   }

   @Override
   public void run() 
   {
      // server loop
      while (true) 
      {
         try 
         {
            transactionManager.runTransaction(serverSocket.accept()); // accept the connection that returns a socket
         }
         
         catch (IOException error) 
         {
            // print the exception of one occured
            System.out.println("[TransactionServer.run] Warning: Error accepting Client");
            error.printStackTrace();
         }
      }
   }
}


