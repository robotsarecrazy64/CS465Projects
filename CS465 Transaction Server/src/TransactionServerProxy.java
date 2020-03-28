import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Not separated currently so no reason to import
//import transaction.conn.Message;
//import transaction.conn.MessageTypes;

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
      
   }
   
   public int closeTransaction()
   {
      
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
      
   }
}
