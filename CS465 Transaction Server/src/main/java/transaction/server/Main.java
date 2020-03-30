/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server;

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
public class Main {
    public void main(String args[])
    {
        if(args.length == 1)
        {
        	new TransactionServer(args[0]).start();
        }
        
        else
        {
        	new TransactionServer("../../config/TransactionServer.properties").start();
        }
        
        new Thread()
        {
        	public void run()
        	{
        		try
        		{
        			Thread.sleep(10000);
        		}
        		
        		catch(InterruptedException error)
        		{
        			System.out.println("Error sleeping in transaction #3");
        			error.printStackTrace();
        		}
        		
        		// Once we wake up, print out information
        		System.out.println("\n\n====================== DEADLOCKED ACCOUNTS INFORMATION ======================");
        		
        		Lock lock;
        		Transaction transcation;
        		HashMap <Account, lock> locks = TransactionServer.lockManager.getLocks();
        		Iterator<java.util.concurrent.locks.Lock> lockIterator = locks.values().interator();
        		
        		while(lockIterator.hasNext())
        		{
        			lock = lockIterator.hasNext();
        			HashMap<Transaction, Object[]> lockRequestors = lock.getLockRequesters();
        			
        			if(!lockRequesters.isEmpty())
        			{
        				System.out.print("Account #" + lock.getAccount().getNumber() + "is invloved in deadlock: ");
        				//print transactions that are stuck
        				Iterator<Transaction> lockedTransactionIterator = lockRequestors.keySet().iterator();
        				
        				while(lockedTransactionIterator.hasNext())
        				{
        					transaction = lockedTransactionIterator.next();
        					Onject[] lockInfo = lockRequestors.get(transcation);
        					int[] lockTypes = (int[]) lockInfo[0];
        					String lockHolders = (String) lockInfo[1];
        					
        					System.out.println("\n\tTransaction#" + transaction.getID() + " trying " + Lock.getLockTypeString(lockTypes[1] + ", waiting release "));
        				}
        			}
        		}
        	}
        }
    }
    
}
