package appserver.server;

import appserver.comm.Message;
import appserver.job.Job;
import appserver.job.Tool;
//import appserver.satellite.Satellite.SatelliteThread;
//import appserver.satellite.Satellite.SatelliteThread;

import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    public static ServerSocket serverSocket = null;
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    
    // Property files that will be used
    Properties serverProperties;
    
    public Server(String serverPropertiesFile) {

        // create satellite manager and load manager
        satelliteManager = new SatelliteManager();
        loadManager = new LoadManager();
        
        // read server properties and create server socket
        try 
        {
	        // Initialize static variables with properties read 
	        // Need to pass info into serverInfo object
	        serverProperties = new PropertyHandler(serverPropertiesFile);
	        serverInfo.setHost(serverProperties.getProperty("HOST"));
	        serverInfo.setPort(Integer.parseInt(serverProperties.getProperty("PORT")));
            
            // Report that port is open for application server
            System.out.println("Application server has host: " + serverInfo.getHost() + " and has port #: " + serverInfo.getPort());
        } 
        
        catch (Exception e) 
        {
            
            // Catch exceptions and report them
            System.err.println("Properties file " + serverPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
    }

    public void run() {
	    // serve clients in server loop ...
	    // when a request comes in, a ServerThread object is spawned
		try 
		{
	            
	        // Create server socket and report to user
	        serverSocket = new ServerSocket(serverInfo.getPort());
	        System.out.println("[Server.run] Creating server socket");
	        
	        // Start taking job requests in a server loop
	        while(true)
	        {
	            // Accept socket and report to user
	            Socket socket = serverSocket.accept(); 
	            System.out.println("[Server.run] Socket accepted, starting server thread...");
	            
	            new ServerThread(socket).start();
	        }
	    } 
	    // Catch exceptions and report them
	    catch (IOException ioe) 
	    {
	        System.out.println("[Server.run] Socket could not be created/accepted");
	        ioe.printStackTrace();
	    }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        ObjectInputStream readFromSatellite = null;
        ObjectOutputStream writeToSatellite = null;
        Message message = null;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message - OTTE
        	// Set up object input/output streams
            try 
            {
                writeToNet = new ObjectOutputStream(client.getOutputStream());
                readFromNet = new ObjectInputStream(client.getInputStream());
            }
            
            // Catch exceptions and report them
            catch (Exception exception)
            {
                System.out.println("[ServerThread.run] Failed to open object streams");
                exception.printStackTrace();
                System.exit(1);
            }
            
            try
            {
                // Reading message
                message = (Message) readFromNet.readObject();
            }
            
            // Catch exceptions and report them
            catch (IOException | ClassNotFoundException exception)
            {
                System.out.println("[ServerThread.run] Message could not be read from object stream.");
                exception.printStackTrace();
                System.exit(1);
            }
            
            // process message
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    // read satellite info - OTTE
                    ConnectivityInfo satelliteInfo = (ConnectivityInfo) message.getContent();
                    
                    // register satellite - OTTE
                    synchronized (Server.satelliteManager) {
                    	satelliteManager.registerSatellite(satelliteInfo);
                    	System.out.println("[ServerThread.run] Satellite registered to satelliteManager.");
                    }

                    // add satellite to loadManager - OTTE
                    synchronized (Server.loadManager) {
                    	loadManager.satelliteAdded(satelliteInfo.getName());
                    	System.out.println("[ServerThread.run] Satellite added to loadManager.");
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");
                    
                    //Job satelliteJob = (Job) message.getContent();
                    String satelliteName = null;
                    
                    synchronized (Server.loadManager) 
                    {
                        // get next satellite from load manager - OTTE
                        try
                        {
                            // Get satellite name
                        	satelliteName = loadManager.nextSatellite();
                        	System.out.println("[ServerThread.run] Sat name: " + satelliteName);
                        }
                        
                        // Catch exceptions and report them
                        catch (Exception exception)
                        {
                            System.out.println("[ServerThread.run] Message could not get next satellite.");
                            exception.printStackTrace();
                            System.exit(1);
                        }
                        
                        // get connectivity info for next satellite from satellite manager - OTTE
                        ConnectivityInfo satelliteConnectInfo = satelliteManager.getSatelliteForName(satelliteName);
                        System.out.println("[ServerThread.run] ConnectivityInfo successfully obtained");


	                    Socket satellite = null;
	                    // connect to satellite - OTTE
		                // Create server socket for satellite server to use
	                    try 
	                    {
	                        // Create server socket and report to user
	                    	satellite = new Socket(satelliteConnectInfo.getHost(), satelliteConnectInfo.getPort());
	                    	System.out.println("[ServerThread.run] Satellite successfully connected to");
		                    
		                    writeToSatellite = new ObjectOutputStream(satellite.getOutputStream());
		                    readFromSatellite = new ObjectInputStream(satellite.getInputStream());
	                    }
	                    
	                    // Catch exceptions and report them
	                    catch (IOException exception) 
	                    {
	                        System.out.println("[ServerThread.run] Socket could not be created/accepted");
	                        exception.printStackTrace();
	                        exception.printStackTrace();
	                    }
	
	                    // open object streams,
	                    // forward message (as is) to satellite,
	                    // receive result from satellite and
	                    // write result back to client
	                    try 
	                    {
	                    	System.out.println("[ServerThread.run] Reading message...");
	                    	// write to satellite stream
	                    	writeToSatellite.writeObject(message);
	                    	// write to server stream
	                    	writeToNet.writeObject(readFromSatellite.readObject());
	                    }
	                    
	                    // Catch exceptions and report them
	                    catch (ClassNotFoundException | IOException exception) 
	                    {
	                        System.out.println("[ServerThread.run] Socket could not be created/accepted");
	                        exception.printStackTrace();
	                        exception.printStackTrace();
	                    }
                    }
                    
                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("/config/Server.properties");
        }
        server.run();
    }
}
