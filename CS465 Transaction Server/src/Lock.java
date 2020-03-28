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
    private Object lockObject; // the object that lock is protecting
    private Vector<Transaction> holders; // transaction ids of current holders
    private LockType lockType; // current type of lock

	/**
        Lock the current transaction
    */
    public synchronized void acquire(Transaction transaction, LockType curLockType) 
	{
		// while another transaction holds the lock needed
        while (checkConflict(curLockType))
		{
			try 
			{
				wait();
			} catch (InterruptedException except)
			{
				// send error
			}
		}
		if (holders.isEmpty()) // no PIDs hold lock
		{
			holders.addElement(transaction);
			lockType = curLockType;
		}
		else if ()// another transaction holds the lock, share it
		{
			if() // this transaction not a holder
			{
				holders.addElement(transaction);
			}
		}
		else if() // this transation is a holder but needs a more exclusive lock
		{
			lockType.promote();
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
	public synchronized bool checkConflict(LockType curLockType) 
	{
		// implement method that checks for conflict
		for( int iter = 0; iter < holders.size(); iter++)
		{
			if(iter.lockType) // if the lock is in use in one of the transactions
			{
				// there is a conflict
				return true;
			}
		}
		// no conflict
		return false;
	}
}