import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
    This class is responsible for handling messages sent between clients
*/
public class Message {
    /**
        Initialize Class Variables
    */
    private final Runnable receiver;
    private final Runnable sender;
    private boolean run = true;

    public Message(DistributedChat parent) 
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
                
                catch (SocketException error) 
                {
                    error.printStackTrace();
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
                    
                    catch (IOException error) 
                    {
                        error.printStackTrace();
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
                
                // Encase in a try block in case of an exception being thrown
                try 
                {
                    socket = new DatagramSocket(); // Creates socket to send message
                } 
                
                catch (SocketException error) 
                {
                    error.printStackTrace();
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
                    // Encase in a try block in case of an exception being thrown
                    try 
                    {
                        // Sends the packet
                        socket.send(packet);
                        Thread.sleep(Globals.UDPINTERVAL);
                    } 
                    
                    // Output the exception if one occurred
                    catch (IOException error) 
                    {
                        error.printStackTrace();
                        parent.quit();
                    } 
                    
                    // Output the exception if one occurred
                    catch (InterruptedException error) 
                    {
                        error.printStackTrace();
                        parent.quit();
                    }
                }
            }
        };
        
        //Start sending and receiving messages
        new Thread(receiver).start();
        new Thread(sender).start();
    }

    /**
        Indicates that connection is no longer active
    */
    public void quit() 
    {
        run = false;
    }
}