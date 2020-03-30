/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server.transaction;

import java.util.ArrayList;
import transaction.server.TransactionServer;
import transaction.server.lock.Lock;

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */
 
/**
   Class to define transactions
*/
public class Transaction {

    private int transID;
    private ArrayList<Lock> locks = null;
    
    private StringBuffer log = new StringBuffer("");
    
	/**
	    Default Constructor
	*/
    Transaction(int transID)
    {
       this.transID = transID;
       this.locks = new ArrayList();
    }
    
    /**
      Returns the transaction ID
    */
    public int getID()
    {
       return transID;
    }
    
    /**
      Returns the transaction list
    */
    public ArrayList<Lock> getLocks() 
    {
       return locks;
    }

    /**
      Adds a lock to the list
    */
    public void addLock(Lock lock) 
    {
       locks.add(lock);
    }

    /**
       Keeps track of transactions
    */
    public void log (String logString) 
    {
       log.append("\n").append(logString);
       
       if(!TransactionServer.transactionView)
       {
          System.out.println("Transaction #" + this.getID() + ((this.getID() < 10) ? " " : "") + logString);
       }
    
    /**
        Returns the log
    */
    public StringBuffer getLog()
    {
       return log;
    }
    
}
