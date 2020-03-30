/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server.transaction;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import transaction.comm.Message;
import  transaction.comm.MessageTypes;
import transaction.server.TransactionServer;
/**
*
* @author Jessica Smith, Jesse Rodriguez, John Jacobelli
*/

public class TransactionManager implements MessageTypes
{
  private static int numTransactions = 0;
  private static final ArrayList<Transaction> transactionList = new ArrayList<>();

  public TransactionManager() {}

  public ArrayList<Transaction> getTransactions()
  {
      return transactionList;
  }

  public void runTransaction(Socket socket)
  {
      
      (new TransactionManagerWorker(socket)).start();
  }

  // Objects of this inner class run transactions, one thread represents one transaction
  public class TransactionManagerWorker extends Thread
  {

    // Networking communication related fields
    Socket client = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Message message = null;

    // Transaction related fields
    Transaction transaction = null;
    int accountNum = 0;
    int accountBalance = 0;

    // Flag for jumping out of while loop after this transaction closed
    boolean keepgoing = true;

    // The constructor just opens up the network channels
    private TransactionManagerWorker(Socket curClient)
    {

            this.client = curClient;

            // Setting up object streams
            try
            {
                    readFromNet = new ObjectInputStream(client.getInputStream());
                    writeToNet = new ObjectOutputStream(client.getOutputStream());
            }
            catch (IOException exception)
            {
                    System.out.println("[TransactionManagerWorker.run] Failed to open object streams");
                    exception.printStackTrace();
                    System.exit(1);
            }
    }

    @Override
    public void run()
    {
            // Loop is left when transaction closes
            while(keepgoing)
            {
                    try
                    {
                        message = (Message) readFromNet.readObject();
                    }
                    catch (IOException | ClassNotFoundException exception)
                    {
                        System.out.println("[TransactionManagerWorker.run] Message could not be read from object stream.");
                        System.exit(1);
                    }

                    // Processing message
                    switch(message.getType())
                    {
                      //--------
                      case OPEN_TRANSACTION:
                      //--------
                        synchronized (transactionList)
                        {
                          transaction = new Transaction(numTransactions++);
                          transactionList.add(transaction);
                        }

                        try
                        {
                          writeToNet.writeObject(transaction.getID());
                        }
                        catch (IOException exception)
                        {
                          System.out.println("[TransactionManagerWorker.run] OPEN_TRANSACTION - Error when writing transID");
                        }

                        transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getID());

                        break;

                        //------
                        case CLOSE_TRANSACTION:
                        //------

                        TransactionServer.lockManager.unLock(transaction);
                        transactionList.remove(transaction);

                        try
                        {
                            readFromNet.close();
                            writeToNet.close();
                            client.close();
                            keepgoing = false;
                        }
                        catch (IOException exception)
                        {
                            System.out.println("[TransactionManagerWorker.run] CLOSE_TRANSACTION - Error when closing connection to client.");
                        }

                        transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getID());

                        // Final print out of all transaction's logs
                        if (TransactionServer.transactionView)
                        {
                            System.out.println(transaction.getLog());
                        }

                        break;

                        //-----
                        case READ_REQUEST:
                        //-----

                        accountNum = (Integer) message.getContent();
                        transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>> account #" + accountNum);
                        accountBalance = TransactionServer.accountManager.read(accountNum, transaction);

                        try
                        {
                            writeToNet.writeObject((Integer) accountBalance);
                        }
                        catch (IOException exception)
                        {
                            System.out.println("[TransactionManagerWorker.run] READ_REQUEST - Eror when writing to object stream");
                        }

                        transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<< account #" + accountNum + ", balance $" + accountBalance);

                        break;

                        //------
                        case WRITE_REQUEST:
                        //------

                        Object[] content = (Object[]) message.getContent();
                        accountNum = ((Integer) content[0]);
                        accountBalance = ((Integer) content[1]);
                        transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>> account #" + accountNum + ", new balance $" + accountBalance);
                        accountBalance = TransactionServer.accountManager.write(accountNum, transaction, accountBalance);
                        
                        try 
                        {
                            writeToNet.writeObject((Integer) accountBalance);
                        }
                        catch (IOException exception)
                        {
                             System.out.println("[TransactionManagerWorker.run] Warning: Message type not implemented"); 
                        }
                        transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<< account #" + accountNum + ", new balance $" + accountBalance);
                        break;
                        
                        default:
                        System.out.println("[TransactionManagerWorker.run] Warning: Message type not implemented");
                    }
            }
    }
  }
}