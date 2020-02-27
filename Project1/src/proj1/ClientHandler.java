package proj1;

import java.io.*; 
import java.util.*; 
import java.net.*; 

// ClientHandler class 
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    DataInputStream dis; 
    DataOutputStream dos; 
    Socket socket; 
    boolean isLoggedLn; 
    
    // Initialize variables
    private int connectionId = 0;
    Socket clientSocket;
    PrintWriter toClient = null;
    BufferedReader fromClient = null;
    String regex = "[^a-zA-Z]+";
    String inputLine, outputLine;
      
    // constructor 
    public ClientHandler(Socket socket, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.socket = socket; 
        this.isLoggedLn = true; 
    } 
  
    @Override
    public void run() {  
        String messageOut; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                messageOut = dis.readUTF();
                this.dos.writeUTF("hey");
                this.dos.flush();
                  
                System.out.println(messageOut + "hi"); 
                  
                if(messageOut.equals("logout"))
                { 
                    this.isLoggedLn = false; 
                    this.socket.close(); 
                    break; 
                } 
  
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                for (ClientHandler activeClient : Server.ar)  
                { 
                   activeClient.dos.writeUTF(messageOut);
                   activeClient.dos.flush();
                   break; 
                } 
            } 
            
            catch (IOException e) 
            { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 