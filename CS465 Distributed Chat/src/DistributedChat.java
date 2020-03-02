import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.net.*;

public class DistributedChat extends Frame implements Runnable {
    // text output of all connections
    private final TextArea textArea;
    // broadcast and receive of UDP; used for TCP connection(s) to peer(s)
    private final Broadcasts broadcasts;
    // list of all sockets for TCP output
    private final ArrayList<Socket> sockets;
    // storage for text data
    private StringBuilder lines;
    private StringBuilder outMessage;
    // continue running application?
    private boolean run = true;
    // name
    String name;

    public DistributedChat() 
    {
        // create field objects
        sockets = new ArrayList<>();
        lines = new StringBuilder();
        outMessage = new StringBuilder();
        textArea = new TextArea(20, 80);
        // set focusable to false to ensure keys are captured by frame
        textArea.setFocusable(false);
        // monospace ftw
        textArea.setFont(Font.decode("monospaced"));
        // the only gui object is the text area
        add(textArea);
        pack();

        // start socket server to accept incoming connections
        new Thread(this).start();
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: "); 
        name = scan.nextLine();
        // instantiate and assign window listener and key listener to frame
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        addKeyListener(frameListener);

        // late initialize of UDP broadcast and receive, to ensure needed
        // objects are instantiated
        broadcasts = new Broadcasts(this);

        setVisible(true);
    }

    // global quit method shuts down everything and exits
    public void quit() {
        run = false;
        broadcasts.quit();
        System.exit(0);
    }

    // method called by key listener
    public void keyTyped(KeyEvent ke) 
    {
        int i;
        String msg;
        
        if(ke.getKeyChar() != KeyEvent.VK_ENTER)
        {
           if(ke.getKeyChar() == KeyEvent.VK_BACK_SPACE)
           {
              outMessage.deleteCharAt(outMessage.length() - 1);
           }
           
           else
           {
              outMessage.append(ke.getKeyChar());
           }
           
           System.out.println(outMessage);
           textArea.setText(name + ": " + outMessage + "|");
        }
        
        else
        {
           synchronized (sockets) 
           {
               // iterate through all sockets, and flush character through
               for (i = 0; i < sockets.size(); i++) 
               {
                   try 
                   {
                       Socket s = sockets.get(i);
                       PrintWriter pw = new PrintWriter(s.getOutputStream());
                       pw.println(name + ": " + outMessage.toString() + '\n');
                       pw.flush();
                   } 

                   catch (IOException ex) 
                   {
                       // remove socket, continue to any next if exception occurs
                       // (socket closed)
                       ex.printStackTrace();
                       sockets.remove(i);
                       continue;
                   }
                   

               }
               outMessage.delete(0, outMessage.length());
           }
        }
        
    }

    // method called by per-connection thread defined in socketStream
    /*public void putChar(int ch) {
        // check for backspace and space for delete,
        // otherwise put character into buffer,
        // and show updated buffer
        if (ch == 8 && lines.length() > 0)
        {
           lines.delete(lines.length() - 1, lines.length());
        }
        
        else
        {
           lines.append((char)ch);
        }
        
        synchronized (textArea) 
        {
           textArea.setText(outMessage.toString() + '|');
        }
    }*/

    // method called by UDP listener
    // exits if connection fails
    void newAddress(InetAddress address) 
    {
        synchronized (sockets) 
        {
            // check if already connected to address, and exit if true
            for (Socket addr: sockets)
            {
               if (addr.getInetAddress().getHostAddress()
                     .equals(address.getHostAddress()))
               {
                  return;
               }
            }
                
            // create a new socket and add it to transmission pool
            Socket s;
            try 
            {
                s = new Socket(address.getHostAddress(), Globals.TCPPORT);
            } 
            
            catch (IOException ex) 
            {
                return;
            }
            
            sockets.add(s);
        }
    }

    // called by socket server thread
    // defines a thread for each connection,
    // which calls putChar for every received character
    // exits thread if error occurs (socket closed)
    private void socketStream(final Socket s) 
    {
        final InputStream is;
        
        try 
        {
            is = s.getInputStream();
        } 
        
        catch (IOException ex) 
        {
            return;
        }
        
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);
        
        new Thread(new Runnable() 
        {
            public void run() 
            {
                while (run && s.isConnected()) 
                {
                    try 
                    {
                        if (br.ready())
                        {
                           //putChar(br.read());
                        }
                            
                    } 
                    
                    catch (IOException ex) 
                    {
                        return;
                    }
                }
            }
        }).start();
    }

    // socket server accepts incoming connection,
    // and creates a thread to pass characters to the screen
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(Globals.TCPPORT);
            while (ss.isBound() && run) {
                socketStream(ss.accept());
            }
            quit();
        } catch (IOException ex) {
            ex.printStackTrace();
            quit();
        }
    }

    // application entry
    public static void main(String[] args) {
        new DistributedChat();
    }
}