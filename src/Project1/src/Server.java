// Java implementation of Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.io.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server 
{ 

   // Vector to store active clients 
   static Vector<ClientHandler> ar = new Vector<>(); 
   
   // counter for clients 
   private static int numConnections;

   public static void main(String[] args) throws IOException 
   { 
      // server is listening on port 1234 
      ServerSocket serverSocket = new ServerSocket(9002); 
      
      Socket socket; 
      
      // running infinite loop for getting 
      // client request 
      while (true) 
      { 
         // Accept the incoming request 
         socket = serverSocket.accept(); 

         System.out.println("New client request received : " + socket); 
         
         // obtain input and output streams 
         DataInputStream dis = new DataInputStream(socket.getInputStream()); 
         DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
         //dos.writeUTF("welcome");
         //dos.flush();
         
         System.out.println("Creating a new handler for this client..."); 

         // Create a new handler object for handling this request. 
         ClientHandler clientConnection = new ClientHandler(socket, "Client " + numConnections, dis, dos); 

         // Create a new Thread with this object. 
         Thread clientThread = new Thread(clientConnection); 
         
         System.out.println("Adding this client to active client list"); 

         // add this client to active clients list 
         ar.add(clientConnection); 

         // start the thread. 
         clientThread.start(); 

         // increment numConnections for new client. 
         // numConnections is used for naming only, and can be replaced 
         // by any naming scheme 
         numConnections++; 

      }
   } 
} 

