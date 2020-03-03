import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.net.*;

// Main class that runs the chat
public class DistributedChat extends Frame implements Runnable 
{
	// Creates global variables
    private final TextArea textArea;
    private final Broadcasts broadcasts;
    private static ArrayList<Socket> sockets;
    private StringBuilder lines;
    private StringBuilder outMessage;
    private boolean run = true;
    private String name;

    public DistributedChat() {
        // Initialize field variables
        sockets = new ArrayList<>();
        lines = new StringBuilder();
        outMessage = new StringBuilder();
        
        // Initialize GUI variables
        textArea = new TextArea(20, 80);
        textArea.setFocusable(false);
        textArea.setFont(Font.decode("monospaced"));
        add(textArea);
        pack();

        // Start the server socket
        new Thread(this).start();

        // Take in client name
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: "); 
        name = scan.nextLine();
        scan.close();
        
        // Instantiate and assign window listener and key listener to GUI
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        addKeyListener(frameListener);

        // Initialize message output
        broadcasts = new Broadcasts(this);

        // Set GUI visible
        setVisible(true);
    }

    // Quit method that closes everything
    public void quit() {
    	
    	// Send leave message to all other clients
    	synchronized (sockets) 
        {
            List<Socket> toRemove = new LinkedList<>();
            
            for (Socket clientSocket : sockets) 
            {
               try 
               {
            	  // Leave message
                  PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
                  printWriter.println(">> " + name + " has left the chat");
                  printWriter.flush();
               } 
            
               catch (IOException errorMessage) 
               {
                  errorMessage.printStackTrace();
                  toRemove.add(clientSocket);
               }
            }

            sockets.removeAll(toRemove);
            outMessage.delete(0, outMessage.length());
        }
    	
    	// Shut down 
        run = false;
        broadcasts.quit();
        System.exit(0);
    }

    // Method that listens for key typing, and generates output string
    public void keyTyped(KeyEvent keyPress) {

        synchronized (sockets) 
        {
        	// If not send, then append
        	if(keyPress.getKeyChar() != KeyEvent.VK_ENTER)
            {
        	   // Backspace
               if(keyPress.getKeyChar() == KeyEvent.VK_BACK_SPACE && outMessage.length() > 0)
               {
                  outMessage.deleteCharAt(outMessage.length() - 1);
               }
               
               //Backspace
               else if(keyPress.getKeyChar() == KeyEvent.VK_BACK_SPACE && outMessage.length() == 0) {}
               
               // Append
               else
               {
                  outMessage.append(keyPress.getKeyChar());
               }
               
               // Display current out message in GUI
               synchronized (textArea) 
               {
            	   textArea.setText(lines.toString() + name + ": " + outMessage + "|");
               }
            }
        	
        	// If send, no append
        	else
            {
               synchronized (sockets) 
               {
                   List<Socket> toRemove = new LinkedList<>();
                   
                   // Send message to all clients
                   for (Socket clientSocket : sockets) 
                   {
                      try 
                      {
                    	 // Message send
                         PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
                         printWriter.println(name + ": " + outMessage.toString());
                         printWriter.flush();
                      } 
                   
                      catch (IOException errorMessage) 
                      {
                         errorMessage.printStackTrace();
                         toRemove.add(clientSocket);
                      }
                   }

                   // Reset current message
                   sockets.removeAll(toRemove);
                   outMessage.delete(0, outMessage.length());
               }
            }
        }
    }

    // Keep track of the message log
    public void putChar(int characterInput) 
    {
        // Update log
        if ((char)characterInput == KeyEvent.VK_BACK_SPACE && lines.length() > 0) // yo test
        {
        	lines.delete(lines.length() - 1, lines.length());
        }
        
        // Update log
        else
        {
        	lines.append((char)characterInput);
        }
        
        // Update log
        synchronized (textArea) 
        {
            textArea.setText(lines.toString() + '|');
        }
    }

    // Get address to send messages
    void newAddress(InetAddress address) 
    {
        synchronized (sockets) 
        {
            // Check is socket connected, if so, exit
            for (Socket clientSocket: sockets)
            {
            	if (clientSocket.getInetAddress().getHostAddress().equals(address.getHostAddress()))
            	{
            		return;
            	}
            }
                    
            // Create new socket to add to array
            Socket clientSocket;
            
            try 
            {
                clientSocket = new Socket(address.getHostAddress(), Globals.TCPPORT);
            } 
            
            catch (IOException errorMessage) 
            {
                return;
            }
            
            sockets.add(clientSocket);
            
            // Send join message
            List<Socket> toRemove = new LinkedList<>();

            try 
            {
               // Join message
               PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
               printWriter.println(">> " + name + " has joined the chat");
               printWriter.flush();
            } 
        
            catch (IOException errorMessage) 
            {
               errorMessage.printStackTrace();
               toRemove.add(clientSocket);
            }

            sockets.removeAll(toRemove);
            outMessage.delete(0, outMessage.length());
        }
    }

    // Establishes the socket stream for a given socket
    private void socketStream(final Socket clientSocket) 
    {
        final InputStream is;
        
        try 
        {
            is = clientSocket.getInputStream();
        } 
        
        catch (IOException errorMessage) 
        {
            return;
        }
        
        // Create input and output for socket
        final InputStreamReader inputReader = new InputStreamReader(is);
        final BufferedReader bufferedRead = new BufferedReader(inputReader);
        
        // Update log
        new Thread(new Runnable() 
        {
            public void run() 
            {
                while (run && clientSocket.isConnected()) 
                {
                    try 
                    {
                        if (bufferedRead.ready())
                        {
                    		putChar(bufferedRead.read());
                        }
                    } 
                    
                    catch (IOException errorMessage) 
                    {
                        return;
                    }
                }
            }
        }).start();
    }

    // Run thread and accepts socket stream
    public void run() 
    {
        try 
        {
            ServerSocket socketServer = new ServerSocket(Globals.TCPPORT);
            while (socketServer.isBound() && run)
            {
                socketStream(socketServer.accept());
            }
            
            socketServer.close();
            // Quit when done
            quit();
        } 
        
        // Quit if error
        catch (IOException errorMessage) 
        {
            errorMessage.printStackTrace();
            quit();
        }
    }

    // Run program
    public static void main(String[] args) 
    {
        new DistributedChat();
    }
}