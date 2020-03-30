/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.server.transaction;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
*
* @author Jessica Smith, Jesse Rodriguez, John Jacobelli
*/

public class TransactionManager extends Thread
{
  private static int numTransactions = 0;
  private static final ArrayList<Transaction> transactionList = new ArrayList<>();

  public TransactionManager() {}

  public ArrayList<Transaction> getTransactions()
  {
      return transactions;
  }

  public void runTransaction(Socket socket)
  {
      (new TransactionManagerWorker(client)).start();
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
    private TransactionManagerWorker(Socket socket)
    {

            this.client = client;

            // Setting up object streams
            try
            {
                    readFromNet = net ObjectInputStream(client.getInputStream());
                    writeToNet = new ObjectOutputStream(client.getOutputStream());
            }
            catch (IOexception exception)
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
                        synchronized (transactions)
                        {
                          transaction = new Transaction(transactionCounter++);
                          transactions.add(transaction);
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

                      TransanctionServer.lockManger.unLock(transaction);
                      transactions.remove(transaction);

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
                      if (TransanctionServer.transactionView)
                      {
                        System.out.println(transaction.getLog());
                      }

                      break;

                      //-----
                      case READ_REQUEST:
                      //-----

                      accountNumber = (Integer) message.getContent();
                      transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>> account #" + accountNumber);
                      balance = TransanctionServer.accountManager.read(accountNumber, transaction);

                      try
                      {
                        writeToNet.writeObject((Integer) balance);
                      }
                      catch (IOException exception)
                      {
                        System.out.println("[TransactionManagerWorker.run] READ_REQUEST - Eror when writing to object stream");
                      }

                      transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<< account #" + accountNumber + ", balance $" + balance);

                      break;

                      //------
                      case WRITE_REQUEST:
                      //------

                      Object[] content = (Object[]) message.getContent();
                      accountNumber = ((Integer) content[0]);
                      balance = ((Integer) content[1]);
                      transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>> account #" + accountNumber + ", new balance $" + balance);

                      break;

                      default:
                        System.out.println("[TransactionManagerWorker.run] Warning: Message type not implemented");
                    }
            }
    }
  }
}