package transaction.server.account;
import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;
import transaction.server.lock.LockManager;
import transaction.server.lock.LockTypes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

/*
 * Class that manages the accounts and their transactions
 */
public class AccountManager implements LockTypes
{
    // initialize global variables
    private static ArrayList<Account> accounts;
    private static int numberAccounts;
    private static int initialBalance;
    
    // Constructor for the class which adds all accounts to an array
    public AccountManager(int numberAccounts, int initialBalance) 
    {
        accounts = new ArrayList();
        AccountManager.numberAccounts = numberAccounts;
        AccountManager.initialBalance = initialBalance;
        
        // add accounts to accounts list
        for(int i = 0; i < numberAccounts; i++)
        {
           accounts.add(i, new Account(i, initialBalance));
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
       // get account
       Account account = getAccount(accountNumber);
       
       // lock the server
       (TransactionServer.LockManager).lock(account, transaction, READ_LOCK);
       
       //
       return account.getBalance();
    }
    
    // Set the balance of the account
    public int write(int accountNumber, Transaction transaction, int balance)
    {
    // get account
       Account account = getAccount(accountNumber);
       
       // lock the server
       (TransactionServer.LockManager).lock(account, transaction, WRITE_LOCK);
       
       // set the balance
       account.setBalance(balance);
       return balance;
    }
}
