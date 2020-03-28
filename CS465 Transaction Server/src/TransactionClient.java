
public class TransactionClient implements Runnable
{

   private int numberTransactions;
   private int numberAccounts;
   private int initialBalance;
   String host = null;
   int port;
   
   public TransactionClient(int numberTransactions, int numberAccounts, 
                            int initialBalance, String host, int port)
   {
      this.numberTransactions = numberTransactions;
      this.numberAccounts = numberAccounts;
      this.initialBalance = initialBalance;
      this.host = host;
      this.port = port;
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
   
   public static void main(String[] args)
   {
      (new TransactionClient("../../config/TransactionClient.properties", 
                             "../../config/TransactionServer.properties")).start();
   }
   
}
