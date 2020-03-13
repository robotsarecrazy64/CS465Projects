import java.util.Vector;

public class Lock 
{
    private Object lockObject; // the object that lock is protecting
    private Vector<E> holders; // transaction ids of current holders
    private LockType lockType; // current type of lock

    public synchronized void acquire(TransID transaction, LockType type) {
        
    }

    public synchronized void release(TransID transaction) {
        
    }
	
	public synchronized bool checkConflict(TransID transaction) {
		// implement method that checks for conflict
		for( int iter = 0; iter < holders.size(); iter++)
		{
			if() // if the lock is in use in one of the transactions
			{
				// there is a conflict
				return true;
			}
		}
		// no conflict
		return false;
	}
}