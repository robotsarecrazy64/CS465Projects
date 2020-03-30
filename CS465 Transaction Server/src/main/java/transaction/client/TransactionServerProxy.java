/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import java.net.Socket;
/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

public class TransactionServerProxy implements MessageTypes
{
   String host = null;
   int port;
   
   private Socket dbConnection = null;
   private ObjectOutputStream writeToNet = null;
   private ObjectInputStream readFromNet = null;
   private Integer transID = 0;
   
   TransactionServerProxy(String host, int port)
   {
      this.host = host;
      this.port = port;
   }
   
   public int openTransaction()
   {
      Message openMessage = new Message(OPEN_TRANSACTION, null);

      try 
      {
         dbConnection = new Socket(host, port);
         writeToNet = new ObjectOutputStream(dbConnection.getOutputStream());
         writeToNet.writeObject(openMessage);
         readFromNet = new ObjectInputStream(dbConnection.getInputStream());
         transID = (Integer) readFromNet.readObject();
      }
      
      catch (Exception error)
      {
         System.out.println("[TransactionServerProxy.openTransaction] Error occurred");
         error.printStackTrace();
      }
      return transID;
   }
   
   public void closeTransaction()
   {
      Message closeMessage = new Message(CLOSE_TRANSACTION, null);

      try 
      {
          writeToNet.writeObject(closeMessage);
          writeToNet.close();
          readFromNet.close();
          dbConnection.close();
      }
      
      catch (Exception error) 
      {
         System.out.println("[TransactionServerProxy.closeTransaction] Error occurred");
         error.printStackTrace();
      }
   }
   
   public int read(int accountNumber)
   {
      Message readMessage = new Message(READ_REQUEST, accountNumber);
      Integer balance = null;
      
      try
      {
         writeToNet.writeObject(readMessage);
         balance = (Integer) readFromNet.readObject();
      }
      
      catch (Exception error)
      {
         System.out.println("[TransactionServerProxy.read] Error occured");
         error.printStackTrace();
      }
      
      return balance;
   }
   
   public int write(int accountNumber, int amount)
   {
      Message writeMessage = new Message(WRITE_REQUEST, content);
      Object[] content = new Object[]{accountNumber, amount};
      Integer balance = null;

      try 
      {
          writeToNet.writeObject(writeMessage);
          balance = (Integer) readFromNet.readObject();
      }
      
      catch (Exception error) 
      {
         System.out.println("[TransactionServerProxy.write] Error occured");
         error.printStackTrace();
      }
      return balance;
   }
}

