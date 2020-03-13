import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;
import transaction.server.lock.LockTypes;

public class AccountManager implements LockTypes
{
    
    static ServerSocket server;
    private static ArrayList<Account> accounts; // transaction ids of current holders
    private static int numberAccounts;
    private static int initialBalance;
    
    
    public AccountManager(int numberAccounts, int initialBalance) 
    {
        
    }
    
    public Account getAccount(int accountNumber)
    {
       
    }
    
    public void ArrayList<Account> getAccounts() 
    {
        
    }
    
    public int read(int accountNumber, Transaction transaction)
    {
       
    }
    
    public int write(int accountNumber, Transaction transaction, int balance)
    {
       
    }
}