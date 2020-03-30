package transaction.server.account;

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
 * Holds the information for a single account in the system
 */
public class Account 
{
	/**
        Class Variables
    */
    private static int balance;
    private static int accountNumber;
    
    // Constructor for account class
    public Account(int givenAccountNumber, int initialBalance) 
    {
        accountNumber = givenAccountNumber;
        balance = initialBalance;
    }
    
    // Returns the accounts certain balance
    public int getBalance()
    {
       return balance;
    }
    
    // Sets the balance for the account
    public void setBalance(int newBalance)
    {
       balance = newBalance;
    }
    
    // Adjusts balance according to withdraw amount
    public void withdraw(int withdrawAmount)
    {
       balance -= withdrawAmount;
    }
    
    // Adjusts balance according to deposit amount
    public void deposit(int depositAmount)
    {
       balance += depositAmount;
    }
}
