public class LockManager
{
	/**
        Class Variables
    */
    private Hashtable locks; 

    /**
        Finds the lock associated with the object if it exists and adds it to the hash table
    */
    public void setLock(Object object, Transaction transaction, LockType type )
	{
        Lock currentLock;
		Enumeration enumLocks = locks.elements();
		// assuming lock is new until set otherwise
		int isNewLock = true;
		synchronized (this)
		{
			// look for a lock associated with this lock
			// if there isn't one, create one and add it to the hashtable
			while (enumLocks.hasMoreElements())
			{
				Lock currentLock = (Lock) (enumLocks.nextElement();
				if ()// this lock is in the hashtable
				{
					isNewLock = false
				}
			}
			if (isNewLock) // if lock is brand new
			{
				Lock newLock = new Lock();
				// TODO: Add lock to hashtable
			}
		}
		currentLock.acquire(transaction, type);
    }

	/**
        Finds the lock associated with the transactions and releases it
    */
    public synchronized void unlock(Transaction transaction)
	{
        Enumeration enumLocks = locks.elements();
		while (enumLocks.hasMoreElements())
		{
			Lock currentLock = (Lock) (enumLocks.nextElement();
			if () // this transation holds this lock
			{
				currentLock.release(transaction);
			}
		}
    }
}