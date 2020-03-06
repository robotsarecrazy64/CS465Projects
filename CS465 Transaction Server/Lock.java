public class Lock 
{
    private Object lockObject; // the object that lock is protecting
    private Vector holders; // transaction ids of current holders
    private LockType lockType; // current type of lock

    public synchronized void acquire(TransID transaction, LockType type) {
        
    }

    public synchronized void release(TransID transaction) {
        
    }
}