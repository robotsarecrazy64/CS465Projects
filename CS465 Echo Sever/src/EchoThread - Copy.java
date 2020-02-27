import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This class makes a thread for a connection when the echo server is connected to.
 */
class EchoThread implements Runnable 
{
   // Initialize variables
   private static int numConnections;
   private int connectionId = 0;
   Socket clientSocket;
   PrintWriter toClient = null;
   BufferedReader fromClient = null;
   String regex = "[^a-zA-Z]+";
   String inputLine, outputLine;
   
   // Construct the thread object
   public EchoThread(Socket socket) 
   {
      connectionId = numConnections++;
      System.out.println("New connection, #" + connectionId);
      clientSocket = socket;
   }
 
   // Run the thread
   public void run() 
   {  
      // Encase in a try block in case of an exception being thrown
      try 
      {
         // Get input from client and save into variable, as well as making a writer
         // for the return to client
         toClient = new PrintWriter(clientSocket.getOutputStream(), true);
         fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         
         // While client has connection, accept line that is sent
         while((inputLine = fromClient.readLine()) != null)
         {
            // Output takes input and removes any non-alphabetic characters; output this
            // to the console
            outputLine = inputLine.replaceAll(regex, "");
            System.out.println("Received (Connection #" + connectionId + "): " + outputLine);
            toClient.write(outputLine + "\n");
            toClient.flush();
     
            // If the output was the work "quit", break the loop
            if (outputLine.equals("quit"))
            {
               break;
            }
         }
      } 
      
      // Output the exception if one occurred
      catch(Exception error)
      {
         error.printStackTrace();
      } 
      
      // Once quit is received, enter this block
      finally 
      {
         // Close connection to client
         toClient.close();
         
         // Close client's connection and their socket. Output connection closed
         try 
         {
            fromClient.close();
            clientSocket.close();
            System.out.println("Closing connection, #" + connectionId);
         } 
         
         // If an exception occurs in the try block above, output exception
         catch (IOException error)
         {
            error.printStackTrace();
         }
      }
   }
}