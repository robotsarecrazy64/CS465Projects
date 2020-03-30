/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server;
import java.io.*;
import java.net.*;
import java.util.*;
import transaction.server.account.AccountManager;
import transaction.server.lock.LockManager;
import transaction.server.transaction.TransactionManager;
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
   public TransactionServer() 
   {
      try 
      {
           
      }
      catch (Exception error) 
      {
         // print the error if one occurred
      }
      
      try 
      {
           
      }
      
      catch (IOException error) 
      {
        
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
                
         }
         
         catch (IOException error) 
         {
            // print the exception of one occured
         }
      }
   }
}


