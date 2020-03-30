package transaction.server.lock;
import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.account.Account;
import java.util.HashMap;
import java.util.Iterator;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */


public class Lock implements LockTypes
{
	/**
        Class Variables
    */
	
	// account that this lock protects
	private final Account account;
	
	// the current lock type
	private int currentLockType;
	
	// the current lock holders
	private final ArrayList<Transaction> lockHolders;
	
	// the current lock requestors
	private final HashMap<Transaction, Object[]> lockRequestors;
	
	public Lock(Account account)
	{
		this.lockHolders = new ArrayList();
		this.lockRequestors = new HashMap();
		this.account = account;
		this.currentLockType = EMPTY_LOCK;
	}

	/**
        Lock the current transaction
     * @param transaction
     * @param newLockType
    */
    public synchronized void acquire(Transaction transaction, int newLockType) 
	{
		// while another transaction holds the lock needed
        while (checkConflict(transaction, newLockType))
		{
			try 
			{
				addLockRequestor(transaction, newLockType);
				wait();
				removeLockRequestor(transaction);
			} catch (InterruptedException except)
			{
				// send error
			}
		}
		
		// psuedo code from book implementation
		if (lockHolders.isEmpty()) // no PIDs hold lock
		{
			// no tranactions hold this lock, add to holders and set lock
			lockHolders.add(transaction);
			currentLockType = newLockType;
			transaction.addLock(this);
		}
		
		else if (!lockHolders.contains(transaction))
		{
			// another transaction holds the lock (read lock) so just share the read lock and add the transaction
			Iterator <Transaction> lockIterator = lockHolders.iterator();
			Transaction otherTransaction;
			
			// pretty sure this loop was just used to output stuff to the console, which i will most likely incorportate later for testing purposes
			while (lockIterator.hasNext())
			{
				otherTransaction = lockIterator.next();
			}
			
			// add the transaction to the list of holders
			lockHolders.add(transaction);
			transaction.addLock(this);
		}

		else if(lockHolders.size() == 1 && currentLockType == READ_LOCK && newLockType == WRITE_LOCK) // this transation is a holder but needs a more exclusive lock
		{
			// the transaction is the lock holder, promote it
			currentLockType = newLockType;
		}
		
		else
		{
			// do nothing
		} 
    }

	/**
        Releases lock from current transaction
     * @param transaction
    */
    public synchronized void release(Transaction transaction) 
	{
		// transaction no longer holds this lock, remove it
        lockHolders.remove(transaction);
	
		if (lockHolders.isEmpty())
		{
			// set locktype to none
			currentLockType = EMPTY_LOCK;
			if (lockRequestors.isEmpty())
			{
				// this lock is not used, so delete it
				// TODO: how do i delete it??
			}
		}

		// stops the waiting in aquire function
	notifyAll();
    }
	
	/**
        Checks if the lock needed is already in use
    */
	private boolean checkConflict(Transaction transaction, int newLockType) 
	{
		if (lockHolders.isEmpty())
		{
			// no locks are held
			return false;
		}
		else if (lockHolders.size() == 1 && lockHolders.contains(transaction))
		{
			// there is only one lock holder and it happens to be the transaction in question
			return false;
		}
		
		else if (currentLockType == READ_LOCK && newLockType == READ_LOCK)
		{
			// share lock
			return false;
		}
		else 
		{ // new ting
			Iterator <Transaction> lockIterator = lockHolders.iterator();
			Transaction otherTransaction;
			
			// can output holders into a string, helpful for testing
			StringBuilder holders = new StringBuilder("");
			while (lockIterator.hasNext())
			{
				otherTransaction = lockIterator.next();
				holders.append(" ").append(otherTransaction.getID());
			}
			
			return true;
		}
		
	}
	
	public synchronized int getLockType()
	{
		return currentLockType;
	}
	
	public Account getAccount()
	{
		return account;
	}
	
	public static String getLockTypeString(int lockType)
	{
		// in case of error
		String lockString = "Locktype not implemented";
		
		// give string for all implemented types
		switch (lockType)
		{
			case READ_LOCK:
				lockString = "READ_LOCK";
				break;
			case WRITE_LOCK:
				lockString = "WRITE_LOCK";
				break;
			case EMPTY_LOCK:
				lockString = "EMPTY_LOCK";
				break;
		}
		
		return lockString;
	}
	
	public HashMap<Transaction, Object[]> getLockRequestors()
	{
		return lockRequestors;
	}
	
	private void addLockRequestor(Transaction requestor, int newLockType)
	{
		int[] lockTypes = new int[2];
		lockTypes[0] = currentLockType;
		lockTypes[1] = newLockType;
		
		
		Iterator<Transaction> lockHoldersIterator = lockHolders.iterator();
		Transaction otherTransaction;
		StringBuilder transactionsString = new StringBuilder("");
		
		// gets the transaction for the lock info
		 while(lockHoldersIterator.hasNext())
		 {
			 otherTransaction = lockHoldersIterator.next();
			 // assuming getID is implemented
			 transactionsString.append(" ").append(otherTransaction.getID());
		 }
		 
		 // copies info about the lock type and the transaction
		 Object[] lockInfo = new Object[2];
		 lockInfo[0] = lockTypes;
		 lockInfo[1] = transactionsString.toString();
		 
		 // adds a new requestor for this lock
		 lockRequestors.put(requestor, lockInfo);
	}
	
	private void removeLockRequestor(Transaction requestor)
	{
		// no longer requesting the lock, so remove it
		lockRequestors.remove(requestor);
	}
}
