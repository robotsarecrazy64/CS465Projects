/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server;

/**
   Imports
*/
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import transaction.server.transaction.Transaction;
import transaction.server.account.Account;
import transaction.server.lock.Lock;


/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */
 
/**
    Main class that runs the Transaction Server with Locking
*/
public class Main {
	/**
      Main method that accepts a custom port number
	*/
    public static void main(String args[])
    {
        // If the user passes in a properties file, use it
        if(args.length == 1)
        {
        	new TransactionServer(args[0]).start();
        }
        // Otherwise look in the default properties file location
        else
        {
        	new TransactionServer("D:/Programming/Repos/CS465Projects/CS465 Transaction Server/src/main/java/utils/TransactionServer.properties").start();
        }
        
        new Thread()
        {
            public void run()
            {
            	try
            	{
            		Thread.sleep(10000); // Sleep for 10 seconds
            	}
            	
            	catch(InterruptedException error)
            	{
					// Print the error if one occurs
            		System.out.println("Error sleeping in transaction #3");
            		error.printStackTrace();
            	}
            	
            	// Once we wake up, print out information
            	System.out.println("\n\n====================== DEADLOCKED ACCOUNTS INFORMATION ======================");
            	
            	Lock lock;
            	Transaction transaction;
            	HashMap <Account, Lock> locks = TransactionServer.lockManager.getLocks();
            	Iterator<Lock> lockIterator = locks.values().iterator();
            	
				// Iterates through each lock and print all info each lock has
            	while(lockIterator.hasNext())
            	{
            		lock = lockIterator.next();
            		HashMap<Transaction, Object[]> lockRequestors = lock.getLockRequestors();
            		
            		if(!lockRequestors.isEmpty())
            		{
            			System.out.print("Account #" + lock.getAccount().getNumber() + "is invloved in deadlock: ");
            			//print transactions that are stuck
            			Iterator<Transaction> lockedTransactionIterator = lockRequestors.keySet().iterator();
            			
            			while(lockedTransactionIterator.hasNext())
            			{
            				transaction = lockedTransactionIterator.next();
            				Object[] lockInfo = lockRequestors.get(transaction);
            				int[] lockTypes = (int[]) lockInfo[0];
            				String lockHolders = (String) lockInfo[1];
            				
            				System.out.println("\n\tTransaction #" + transaction.getID() + " trying " + 
            				                    Lock.getLockTypeString(lockTypes[1]) + ", waiting release " + 
            				                    Lock.getLockTypeString(lockTypes[0]) + ", help by transaction(s)" + lockHolders);
            			}
            		}
            	}
            	
            	System.out.println("\n\n====================== DEADLOCKED TRANSACTIONS INFORMATION ======================");
            	
            	StringBuffer deadlockedTransactionsLogs = new StringBuffer();
            	ArrayList<Transaction> deadlockedTransactions = TransactionServer.transactionManager.getTransactions();
            	Iterator<Transaction> deadlockedTransactionsIterator = deadlockedTransactions.iterator();
            	Transaction deadlockedTransaction;
            	
            	while(deadlockedTransactionsIterator.hasNext())
            	{
            	   deadlockedTransaction = deadlockedTransactionsIterator.next();
            	   deadlockedTransactionsLogs.append(deadlockedTransaction.getLog() + "\n");
            	}
            	
            	System.out.println(deadlockedTransactionsLogs);
            	      
            	System.out.println("\n\n====================== BRANCH TOTAL ======================");
            	
            	ArrayList<Account> accounts = TransactionServer.accountManager.getAccounts();
                
            	Iterator<Account> accountIterator = accounts.iterator();
            	Account account;
            	int total = 0;
            	// print out the account balances
            	while(accountIterator.hasNext())
            	{
                   System.out.println(total);
            	   account = accountIterator.next();
            	   total += account.getBalance();
            	}
            	
            	System.out.println("---> $" + total + "\n\n");
            	
            	System.exit(1);
            }
        }.start();
    }
}
