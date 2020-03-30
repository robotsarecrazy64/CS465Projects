/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.client;
import java.util.Properties;
import utils.PropertyHandler;
/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

public class TransactionClient extends Thread
{
    /**
        Class variables
    */
    public static String host; // host
    public static int port; // port number
    
    public static int numberTransactions; // number of transactions
    public static int numberAccounts; // number of accoutns
    public static int initialBalance; // initial balance
    
    public static StringBuffer log; // transaction log
    
    /**
        Default Constructor
    */
    public TransactionClient(String clientPropertiesFile, String serverPropertiesFile)
    {
        try
        {
            Properties serverProperties = new PropertyHandler(serverPropertiesFile); // Load the server properties file
            host = serverProperties.getProperty("HOST"); // gets the Host from the server properties file
            port = Integer.parseInt(serverProperties.getProperty("PORT")); // gets the port number from the server properties file
            numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS")); // gets the number of accounts from the server properties file
            initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE")); // gets the initial balance from the server properties file
      
            Properties clientProperties = new PropertyHandler(clientPropertiesFile); // load the client properties file
            numberTransactions = Integer.parseInt(clientProperties.getProperty("NUMBER_TRANSACTIONS")); // gets the number of transactions from the client properties file
            
         }
   
        catch(Exception error)
        {
            // prints the error if one occurred
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
               
               
               int accountFrom = (int)Math.floor(Math.random() * numberAccounts);
               int accountTo = (int)Math.floor(Math.random() * numberAccounts);
               int amount = (int)Math.ceil(Math.random() * initialBalance);
               int balance;
               System.out.println("\tTransaction #" + transID + ", $" + amount + " " + 
                                  accountFrom + "=>" + accountTo + ".");
               
               
               
               balance = transaction.read(accountFrom);
               System.out.println("Acc: "+ accountTo);
               transaction.write(accountFrom, balance - amount);
               
               balance = transaction.read(accountTo);
               
               transaction.write(accountTo, balance + amount);
               
               transaction.closeTransaction();
               
               System.out.println("Transaction #" + transID + " finished.");
            }
         }.start();
      }
   }
   
   public static void main(String[] args)
   {
      (new TransactionClient("D:/Programming/Repos/CS465Projects/CS465 Transaction Server/src/main/java/utils/TransactionClient.properties", 
                             "D:/Programming/Repos/CS465Projects/CS465 Transaction Server/src/main/java/utils/TransactionServer.properties")).start();
   }
   
}
