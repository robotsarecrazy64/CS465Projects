package transaction.server.lock;

import java.util.HashMap;
import java.util.Iterator;
import transaction.server.account.Account;
import transaction.server.transaction.Transaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */
public class LockManager implements LockTypes
{
	/**
        Class Variables
    */
    private static HashMap<Account, Lock> locks; 
	private static boolean applyLocking;
	
	public LockManager(boolean applyLocking)
	{
		locks = new HashMap<>();
		this.applyLocking = applyLocking;
	}
    /**
        Finds the lock associated with the object if it exists and adds it to the hash table
    */
    public void lock(Account account, Transaction transaction, int lockType )
	{
		// return, if we don't do locking
		if (!applyLocking) return;
		
		// get the lock that is attached to this account
        Lock lock;
		
		synchronized (this)
		{
			// look for a lock associated with this lock
			// if there isn't one, create one and add it to the hashtable
			lock = locks.get(account);
			
			if(lock == null)
			{
				lock = new Lock(account);
				locks.put(account, lock);
				
			}
		}
		lock.acquire(transaction, lockType);
    }

	/**
        Finds the lock associated with the transactions and releases it
    */
    public synchronized void unLock(Transaction transaction)
	{
		// if we don't do locking
		if (!applyLocking) return;
		
		Iterator<Lock> lockIterator = transaction.getLocks().listIterator();
		Lock currentLock;
		
		while(lockIterator.hasNext())
		{
	
			currentLock = lockIterator.next();
			
			// releases lock associated with this transaction
			currentLock.release(transaction);
		}
		/* old idea
        Enumeration enumLocks = locks.elements();
		while (enumLocks.hasMoreElements())
		{
			Lock currentLock = (Lock) (enumLocks.nextElement();
			if () // this transation holds this lock
			{
				currentLock.release(transaction);
			}
		}
		*/
    }
	
	public HashMap<Account, Lock> getLocks()
	{
		return locks;
	}
}
