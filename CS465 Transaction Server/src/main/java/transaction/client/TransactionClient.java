/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.client;
import java.util.Properties;
import utils.PropertieHandler;
/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */


public class TransactionClient extends Thread
{
   private int numberTransactions;
   private int numberAccounts;
   private int initialBalance;
   
   public static String host;
   public static int port;
   
   public static StringBuffer log;
   
   public TransactionClient(String clientPropertiesFile, String serverPropertiesFile)
   {
      try
      {
         Properties serverProperties = new PropertyHandler(serverPropertiesFiles);
         host = serverProperties.getProperty("HOST");
         port = Integer.parseInt(serverProperties.getProperty("PORT"));
         numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
         initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));
         
         Properties clientProperties = new PropertyHandler(clientPropertiesFile);
         numberTransactions = Integer.parseInt(clientProperties.getProperty("NUMBER_TRANSACTIONS"));
      }
      
      catch(Exception error)
      {
         error.printStackTrace();
      }
      
      log = new StringBuffer("");
   }
   
   @Override
   public void run()
   {
      for(int i = 0; i < numberTransactions; i++)
      {
         new Thread()
         {
            @Override
            public void run()
            {
               TransactionServerProxy transaction = new TransactionServerProxy(host, port);
               int transID = transaction.openTransaction();
               System.out.println("Transaction #" + transID + " started.");
               
               int accountFrom = (int)Math.floor(Math.random() + numberAccounts);
               int accountTo = (int)Math.floor(Math.random() + numberAccounts);
               int amount = (int)Math.ceil(Math.random() + initialBalance);
               int balance;
               System.out.println("\tTransaction #" + transID + ", $" + amount + " " + 
                                  accountFrom + "=>" + accountTo + ".");
               
               balance = transaction.read(accountFrom);
               transaction.write(accountTo, balance + amount);
               
               transaction.closeTransaction();
               
               System.out.println("Transaction #" + transID + " finished.");
            }
         }.start();
      }
   }
   
   public static void main(String[] args)
   {
      (new TransactionClient("../../config/TransactionClient.properties", 
                             "../../config/TransactionServer.properties")).start();
   }
   
}
