public class LockManager
{
	/**
        Class Variables
    */
    private HashMap<Account, Lock> locks; 
	private static boolean applyLocking;
	
	public LockManager(boolean applyLocking)
	{
		locks = new HashMap<>();
		this.applyLocking = applyLocking;
	}
    /**
        Finds the lock associated with the object if it exists and adds it to the hash table
    */
    public void setLock(Account account, Transaction transaction, int lockType )
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
		if (!applyLocking) return;
		
		Iterator<Lock> lockIterator = transaction.getLocks().listIterator();
		Lock currentLock;
		while(lockIterator.hasNext())
		{
			currentLock = lockIterator.next();
			
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
	
	public HashMap<Account, lock> getLocks()
	{
		return locks;
	}
}