import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.account.Account;
import java.util.HashMap;
import java.util.Iterator;


public class Lock implements LockTypes
{
	/**
        Class Variables
    */
	
	// TODO: add comments describing each variable
	private final Account account;
	
	private int currentLockType;
	
	private final ArrayList<Transaction> lockHolders;
	
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
		
		if (lockHolders.isEmpty()) // no PIDs hold lock
		{
			lockHolders.add(transaction);
			currentLockType = newLockType;
			transaction.addLock(this);
		}
		
		else if (!lockHolders.contains(transaction))
		{
			Iterator <Transaction> lockIterator = lockHolders.iterator();
			Transaction otherTransaction;
			
			// pretty sure this loop was just used to output stuff to the console, which i will most likely incorportate later for testing purposes
			while (lockIterator.hasNext())
			{
				otherTransaction = lockIteractor.next();
			}
			
			lockHolders.add(transaction);
			transaction.addLock(this);
		}

		else if(lockHolders.size() == 1 && currentLockType == READ_LOCK && newLockType == WRITE_LOCK) // this transation is a holder but needs a more exclusive lock
		{
			currentLockType = newLockType;
		}
		
		else
		{
			// do nothing
		} 
    }

	/**
        Releases lock from current transaction
    */
    public synchronized void release(Transaction transaction) 
	{
        lockHolders.remove(transaction);
		// set locktype to none
		this.currentLockType = "EMPTY_LOCK"; // come back to this
		notifyAll();
    }
	
	/**
        Checks if the lock needed is already in use
    */
	public synchronized bool checkConflict(Transaction transaction, LockType curLockType) 
	{
		if (lockHolders.isEmpty())
		{
			// no locks are held
			return false;
		}
		else if (lockholders.size() == 1 && lockHolders.contains(transaction))
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
				otherTransaction = lockIteractor.next();
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
		String lockString = "Locktype not implemented";
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
		 while(lockHoldersIterator.hasNext())
		 {
			 otherTransaction = lockHoldersIterator.next();
			 // assuming getID is implemented
			 transactionsString.append(" ").append(otherTransaction.getID());
		 }
		 
		 Object[] lockInfo = new Object[2];
		 lockInfo[0] = lockTypes;
		 lockInfo[1] = transactionsString.toString();
		 lockRequestors.put(requestor, lockInfo);
	}
	
	private void removeLockRequestor(Transaction requestor)
	{
		lockRequestors.remove(requestor);
	}
}