import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class Globals {
	// Ports to connect to and address to send message to
    public static final int UDPPORT = 9111;
    public static final int TCPPORT = 9112;
    public static final int UDPINTERVAL = 1000;
    public static final InetAddress broadcastAddress;
    static
    {
        broadcastAddress = getBroadcastAddress();
        assert (broadcastAddress != null);
    }
    
    // Get IP address that messages from clients will be sent
    private static InetAddress getBroadcastAddress() 
    {
        ArrayList<NetworkInterface> interfaces = new ArrayList<>();
        
        // Add to the list of IP address
        try 
        {
            interfaces.addAll(Collections.list(NetworkInterface.getNetworkInterfaces()));
        } 
        
        catch (SocketException errorMessage) 
        {
            errorMessage.printStackTrace();
            return null;
        }
        
        // Gets the address of the current client
        for (NetworkInterface networkInterfaceCard: interfaces) 
        {
            try 
            {
                if (!networkInterfaceCard.isUp() || networkInterfaceCard.isLoopback())
                {
                   continue;
                }
            } 
            
            catch (SocketException errorMessage) 
            {
                continue;
            }
            
            for (InterfaceAddress interfaceAddr: networkInterfaceCard.getInterfaceAddresses()) 
            {
                if (interfaceAddr == null || interfaceAddr.getBroadcast() == null)
                {
                   continue;
                }
                
                return interfaceAddr.getBroadcast();
            }
        }
        
        return null;
    }
}