import java.util.Vector;
import java.lang.Object;
import javax.ejb.LockType;

public class Lock 
{
    private Object lockObject; // the object that lock is protecting
    private Vector<E> holders; // transaction ids of current holders
    private LockType lockType; // current type of lock

    public synchronized void acquire(TransactionManager transaction, LockType type) 
	{
        while ()
		{
			try 
			{
				wait();
			} catch (InterruptedException except)
			{
				
			}
		}
		if (holders.isEmpty())
		{
			holders.addElement(transaction);
			lockType = type;
		}
		else if ()
		{
			if()
			{
				holders.addElement(transaction);
			}
		}
		else if()
		{
			lockType.promote();
		}
    }

    public synchronized void release(TransID transaction) 
	{
        holders.removeElement(transaction);
		// set locktype to none
		notifyAll();
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