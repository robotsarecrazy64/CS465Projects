import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;
import transaction.server.lock.LockTypes;

/*
 * Class that manages the accounts and their transactions
 */
public class AccountManager implements LockTypes
{
	/**
        Class Variables
    */
    private static ArrayList<Account> accounts; // transaction ids of current holders
    private static int numberAccounts;
    private static int initialBalance;
    
    // Constructor for the class which adds all accounts to an array
    public AccountManager(int numberAccounts, int initialBalance) 
    {
        for(int i = 0; i < numberAccounts; i++)
        {
           //TODO: add accounts to accounts list 
        }
    }
    
    // Retrieves a specific account from the list of accounts
    public Account getAccount(int accountNumber)
    {
       return accounts.get(accountNumber);
    }
    
    // Returns an array of all the accounts
    public ArrayList<Account> getAccounts() 
    {
        return accounts;
    }
    
    // Get the balance of the account
    public int read(int accountNumber, Transaction transaction)
    {
       // get account and read balance
       account = getAccount(accountNumber);
       return account.getBalance();
    }
    
    // Set the balance of the account
    public int write(int accountNumber, Transaction transaction, int balance)
    {
       // get account and set balance
       account.setBalance(balance);
       return balance;
    }
}