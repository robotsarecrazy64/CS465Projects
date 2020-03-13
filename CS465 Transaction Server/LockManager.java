public class LockManager
{
    private Hashtable locks; 

    /**
        Finds the lock associated with the object if it exists and adds it to the hash table
    */
    public void setLock(Object object, TrasnactionManager transaction, LockType type )
	 {
        Lock newLock = Lock();
		  newLock.acquire();
    }

    public synchronized void unlock(TransID trans)
	 {
        
    }
}