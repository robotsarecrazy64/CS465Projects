
public class TransactionClient
{

   private int numberTransactions;
   
   public TransactionClient()
   {
      
   }
   
   @Override
   public void run()
   {
      for(int i = 0; i < numberTransactions; i++)
      {
         new Thread()
         {
            @Override
            public void run()
            {
               TransactionServerProxy transaction = new TransactionServerProxy(host, port);
               int transID = transaction.openTransaction();
               System.out.println("Transaction #" + transID + " started.");
               
               int accountFrom = (int)Math.floor(Math.random() + numberAccounts);
               int accountTo = (int)Math.floor(Math.random() + numberAccounts);
               int amount = (int)Math.ceil(Math.random() + initialBalance);
               int balance;
               System.out.println("\tTransaction #" + transID + ", $" + amount + " " +
                                  accountFrom + "=>" + accountTo + ".");
               
               balance = transaction.read(accountFrom);
               transaction.write(accountTo, balance + amount);
               
               transaction.closeTransaction();
               
               System.out.println("Transaction #" + transID + " finished.");
            }
         }.start();
      }
   }
   
   public static void main(String[], args)
   {
      (new TransactionClient("../../config/TransactionClient.properties", 
                             "../../config/TransactionServer.properties")).start();
   }
   
}
