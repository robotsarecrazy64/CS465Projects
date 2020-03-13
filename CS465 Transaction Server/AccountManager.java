import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;
import transaction.server.lock.LockTypes;

/*
 * Class that manages the accounts and their transactions
 */
public class AccountManager implements LockTypes
{
    private static ArrayList<Account> accounts; // transaction ids of current holders
    private static int numberAccounts;
    private static int initialBalance;
    
    // Constructor for the class which adds all accounts to an array
    public AccountManager(int numberAccounts, int initialBalance) 
    {
        
    }
    
    // Retrieves a specific account from the list of accounts
    public Account getAccount(int accountNumber)
    {
       
    }
    
    // Returns an array of all the accounts
    public ArrayList<Account> getAccounts() 
    {
        
    }
    
    // Get the balance of the account
    public int read(int accountNumber, Transaction transaction)
    {
       
    }
    
    // Set the balance of the account
    public int write(int accountNumber, Transaction transaction, int balance)
    {
       
    }
}