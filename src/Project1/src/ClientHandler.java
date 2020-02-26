import java.io.*; 
import java.util.*; 
import java.net.*; 

// ClientHandler class 
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    private DataInputStream dis; 
    private DataOutputStream dos; 
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
  
        String received; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                  
                System.out.println(received + "hi"); 
                  
                if(received.equals("logout"))
                { 
                    this.isLoggedLn = false; 
                    this.socket.close(); 
                    break; 
                } 
                  
                // break the string into message and recipient part 
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String MsgToSend = st.nextToken(); 
                String recipient = st.nextToken(); 
  
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                for (ClientHandler mc : Server.ar)  
                { 
                    // if the recipient is found, write on its 
                    // output stream 
                    if (mc.name.equals(recipient) && mc.isLoggedLn == true)  
                    { 
                        mc.dos.writeUTF(this.name+" : "+MsgToSend);
                        mc.dos.flush();
                        break; 
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