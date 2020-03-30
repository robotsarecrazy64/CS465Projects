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
public class Transaction {

    int transID;
    ArrayList<Lock> locks = null;
    
    StringBuffer log = new StringBuffer("");
    
    Transaction(int transID)
    {
       this.transID = transID;
       this.locks = new ArrayList();
    }
    
    public int getID()
    {
       return transID;
    }
    
    public ArrayList<Lock> getLocks() 
    {
       return locks;
    }

    public void addLock(Lock lock) 
    {
       locks.add(lock);
    }

    public void log (String logString) 
    {
       log.append("\n").append(logString);
       
       if(!TransactionServer.transactionView)
       {
          System.out.println("Transaction #" + this.getID() + ((this.getID() < 10) ? " " : "") + logString);
       }
    }
    
    public StringBuffer getLog()
    {
       return log;
    }
    
}
