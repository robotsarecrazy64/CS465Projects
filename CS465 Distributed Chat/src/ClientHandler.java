import java.io.*; 
import java.util.*; 
import java.net.*; 

// ClientHandler class 
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    BufferedReader dis; 
    DataOutputStream dos; 
    Socket socket; 
    boolean isLoggedLn;
      
    // constructor 
    public ClientHandler(Socket socket, String name, 
          BufferedReader dis, DataOutputStream dos) { 
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
                messageOut = this.dis.readLine() + "\r\n";
                  
                if(messageOut.equals("Logout\r\n"))
                { 
                    Server.ar.remove(this);
                    this.isLoggedLn = false; 
                    this.socket.close();
                    
                    break; 
                } 
  
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                for (ClientHandler activeClient : Server.ar)  
                { 
                   if(activeClient != this)
                   {
                      activeClient.dos.writeBytes(name +" : " +messageOut);
                      activeClient.dos.flush();
                   }
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