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
   static ServerSocket socket;
   public static boolean transaction;
   public static AccountManager account;
   public static TransactionManager transactionManager;
   public static LockManager lock;
   
   /**
      Default Constructor for connection
   */
   public TransactionServer(String serverPropertiesFile) 
   {
      Properties serverProperties = null;

      try 
      {
           serverProperties = new PropertyHandler(serverPropertiesFile);
      }
      catch (Exception error) 
      {
         // print the error if one occurred
         System.out.println("[TransactionServer.TransactionServer] Didnt find properties file");
         error.printStackTrace();
         System.exit(1);
      }
      
	  transaction = Boolean.valueOf(serverProperties.getProperty("TRANSACTION_VIEW"));
	  TransactionServer manager = new TransactionManager();
	  System.out.println("[TransactionServer.TransactionServer] Transaction Manager created");
	  
	  boolean applyLock = Boolean.valueOf(serverProperties.getProperty("APPLY_LOCKING"));
	  TransactionServer lockManager = new LockManager(applyLock);
	  System.out.println("[TransactionServer.TransactionServer] Lock Manager created");
	  
	  int numAccounts = 0;
	  numAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
	  int initBalance = 0;
	  initBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE");
	  
	  TransactionServer.accountManager = new AccountManager(numAccounts, initBalance);
	  System.out.println("[TransactionServer.TransactionServer] Account Manager created");
      try 
      {
         socket = new ServerSocket(Integer.parseInt(serverProperties.getProperty("PORT"));
	     System.out.println("[TransactionServer.TransactionServer] Server Socket created");
      }
      
      catch (IOException error) 
      {
         System.out.println("[TransactionServer.TransactionServer] Could not create Server Socket");
         error.printStackTrace();
         System.exit(1);
      }
   }

   public void log() 
   {
      // allows you to attach any info to the transactions
   }

   public void run() 
   {
      // run method
      while (true) 
      {
         try 
         {
            transactionManager.runTransaction(socket.accept());
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


