import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Broadcasts {

    private final Runnable receiver;
    private final Runnable sender;
    private boolean run = true;

    public Broadcasts(DistributedChat parent) 
    {
    	// Receiver thread for the client
        receiver = new Runnable() 
        {
            public void run() 
            {
                byte data[] = new byte[0];
                DatagramSocket socket = null;
                
                // Creates socket to receive message
                try 
                {
                    socket = new DatagramSocket(Globals.UDPPORT);
                } 
                
                catch (SocketException ex) 
                {
                    ex.printStackTrace();
                    parent.quit();
                }
                
                // Creates packet to hold the data in
                DatagramPacket packet = new DatagramPacket(data, data.length);
                while (run) 
                {
                    try 
                    {
                    	// Receives the packet
                        socket.receive(packet);
                    } 
                    
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                        parent.quit();
                    }
                    
                    parent.newAddress(packet.getAddress());
                }
            }
        };
        
        // Sender thread for the client
        sender = new Runnable() 
        {
            public void run() 
            {
                byte data[] = new byte[0];
                DatagramSocket socket = null;
                
                // Creates socket to send message
                try 
                {
                    socket = new DatagramSocket();
                } 
                
                catch (SocketException ex) 
                {
                    ex.printStackTrace();
                    parent.quit();
                }
               
                // Creates packet to send the data in
                DatagramPacket packet = new DatagramPacket(
                        data, 
                        data.length, 
                        Globals.broadcastAddress, 
                        Globals.UDPPORT);
               
                while (run) 
                {
                    try 
                    {
                    	// Sends the packet
                        socket.send(packet);
                        Thread.sleep(Globals.UDPINTERVAL);
                    } 
                    
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                        parent.quit();
                    } 
                    
                    catch (InterruptedException ex) 
                    {
                        ex.printStackTrace();
                        parent.quit();
                    }
                }
            }
        };
        
        //Start sending and receiving messages
        new Thread(receiver).start();
        new Thread(sender).start();
    }

    // Stop running
    public void quit() 
    {
        run = false;
    }
}