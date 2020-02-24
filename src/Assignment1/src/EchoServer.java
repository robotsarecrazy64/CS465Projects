import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class serves as a simple echo server to repeat any message sent to it back to the client
 */
public class EchoServer 
{
    //Class variables
   ServerSocket serverSocket; //Socket Object to represent the connection

    /**
     * Prepares the server to establish a connection
     */
   public void start() 
   {
       // Encase in a try block in case of an exception being thrown
      try
      {
         serverSocket = new ServerSocket(8080); //binds the server to port 8080
         // Server loop to accept multiple connections
         while(true)
         {
            Thread clientThread = new Thread(new EchoThread(serverSocket.accept())); // Creates client thread object to handle connection
            clientThread.start();
         }
      }

      // Output the exception if one occurred
      catch (IOException error)
      {
         error.printStackTrace();
      }

      // Attempt to close the connection
      finally 
      {
          // Encase in a try block in case of an exception being thrown
         try 
         {
            System.out.println("Closing Server Socket");
            serverSocket.close(); // Close the connection
         }

         // Output the exception if one occurred
         catch (IOException error)
         {
            error.printStackTrace();
         }
      }
 
   }

    /**
     * Main method to run the server
     * @param args
     */
   public static void main(String[] args) 
   {
      EchoServer echoServer = new EchoServer(); // Create Server Object
       echoServer.start();    // Start the server
   }
}