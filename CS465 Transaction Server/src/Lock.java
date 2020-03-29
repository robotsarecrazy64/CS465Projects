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
        holders.removeElement(transaction);
		// set locktype to none
		notifyAll();
    }
	
	/**
        Checks if the lock needed is already in use
    */
	public synchronized bool checkConflict(Transaction transaction, LockType curLockType) 
	{
		if (lockHolders.isEmpty())
		{
			return false;
		}
		else if (lockholders.size() == 1 && lockHolders.contains(transaction))
		{
			return false;
		}
		else 
		{ // new ting
			Iterator <Transaction> lockIterator = lockHolders.iterator();
			Transaction otherTransaction;
			while (lockIterator.hasNext())
			{
				otherTransaction = lockIteractor.next();
			}
		}
		// implement method that checks for conflict
		for( int iter = 0; iter < lockHolders.size(); iter++)
		{
			if(lockeHolder.at(iter).lockType) // if the lock is in use in one of the transactions
			{
				// there is a conflict
				return true;
			}
		}
		// no conflict
		return false;
	}
}