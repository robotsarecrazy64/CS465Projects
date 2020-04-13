package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;
import java.io.PrintStream;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte, Jessisa Smith, John Jacobelli, Jesse Rodriguez
 */
public class Satellite extends Thread {

	// Create our variables
    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable<String, Tool> toolsCache = null;
    
    // Propery files that will be used
    Properties satelliteProperties;
    Properties serverProperties;
    Properties classLoaderProperties;
    
    // socket info
    static ServerSocket serverSocket;
    static int port;

    /*
     * Constructor for the class, reads in all the config files and deals with them
     * appropriately
     */
    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) 
    {

        // Read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        super(satellitePropertiesFile);

        try 
        {
        	// Initialize static variables with properties read 
            // Need to pass info into satelliteInfo object
            satelliteProperties = new PropertyHandler(satellitePropertiesFile);
            satelliteInfo.setName(satelliteProperties.getProperty("NAME"));
            satelliteInfo.setPort(Integer.parseInt(satelliteProperties.getProperty("PORT")));
            
            // Report that port is open for satellite
            System.out.println("Satellite " + satelliteInfo.getName() + " has port #: " + satelliteInfo.getPort());
        } 
        
        // Catch exceptions and report them
        catch (Exception e) 
        {
            System.err.println("Properties file " + satellitePropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // Read properties of the application server and populate serverInfo object
        // Other than satellites, the application server doesn't have a human-readable name, so leave it out
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
        
        // Catch exceptions and report them
        catch (Exception e) 
        {
            System.err.println("Properties file " + serverPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // Read properties of the code server and create class loader
        try 
        {
            // Initialize static variables with properties read 
            // Need to pass info into serverInfo object
            classLoaderProperties = new PropertyHandler(classLoaderPropertiesFile);
            
            // Create HTTPClassLoader object with read-in property data
            classLoader = new HTTPClassLoader(classLoaderProperties.getProperty("HOST"), Integer.parseInt(classLoaderProperties.getProperty("PORT")));
            
            // Report that port is open for class loader
            System.out.println("Class loader has host: " + classLoaderProperties.getProperty("HOST") + " and has port #: " + Integer.parseInt(classLoaderProperties.getProperty("PORT")));
        } 
        
        // Catch exceptions and report them
        catch (Exception e) 
        {
            System.err.println("Properties file " + classLoaderPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // Create tools cache
        toolsCache = new Hashtable<>();
    }

    @Override
    public void run() 
    {
        Socket socket = null;
        
        // Create server socket for satellite server to use
        try 
        {
        	// Create server socket and report to user
            serverSocket = new ServerSocket(satelliteInfo.getPort());
            System.out.println("[Satellite.run] Creating server socket");
            
            // Start taking job requests in a server loop
            while(true)
            {
            	// Accept socket and report to user
                socket = serverSocket.accept(); 
                System.out.println("[Satellite.run] Socket accepted, starting satellite thread...");
                
                new SatelliteThread(socket, this).start();
            }
        }
        
        // Catch exceptions and report them
        catch (IOException ioe) 
        {
        	System.out.println("[Satellite.run] Socket could not be created/accepted");
            System.err.println("IOException" + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    // Inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread 
    {
    	// Create variables that will be used
        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;
        boolean waluigi = true;

        // Constructor that takes in a job request and satellite object
        SatelliteThread(Socket jobRequest, Satellite satellite) 
        {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() 
        {
            
            // Set up object input/output streams
            try 
            {
                writeToNet = new ObjectOutputStream(jobRequest.getOutputStream());
                readFromNet = new ObjectInputStream(jobRequest.getInputStream());
            }
            
            // Catch exceptions and report them
            catch (Exception exception)
            {
                System.out.println("[SatelliteThread.run] Failed to open object streams");
                exception.printStackTrace();
                System.exit(1);
            }
        	// Server runs while boolean is true, indefinitely
            while(waluigi)
            {
            	// Reading message
                try
                {
                    message = (Message) readFromNet.readObject();
                }
                
                // Catch exceptions and report them
                catch (IOException | ClassNotFoundException exception)
                {
                    System.out.println("[SatelliteThread.run] Message could not be read from object stream.");
                    System.exit(1);
                }
                
                // Switch to determine what to do based on message type
                // Only do something if it is a job request
                switch (message.getType()) 
                {
                	// If job request...
                    case JOB_REQUEST:
                        
                    	// Processing job request
                        try 
                        {
                        	// Create job based on message content
                            Job getJob = (Job) message.getContent();
                            
                            // Report what the tool is in the message
                            System.out.println("Tool: " + getJob.getToolName());
                            
                            //Retrieve the tool that was specified by the job
                            Tool getTool = getToolObject(getJob.getToolName());
                            
                            // Get function results
                            Object returnToClient = getTool.go(getJob.getParameters());
                            
                            // Use tool function to output to client
                            writeToNet.writeObject(returnToClient);
                            writeToNet.flush();
                            
                            // Close io and job request
                            readFromNet.close();
                            writeToNet.close();
                            jobRequest.close();
                            
                            // Set server loop to false to close it until new job
                            waluigi = false;
                        }
                        
                        // Catch exceptions and report them
                        catch (Exception ex)
                        {
                        	System.out.println("Error occurred: " + ex);
                        }
                        
                        break;

                    // If not a job request, do not carry out the action and report why
                    default:
                        System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
                }
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        // Create tool object variable to use
        Tool toolObject;

        // Is the tool already in the cache? If so, load and ignore if statement
        // If not, enter if statement
        if ((toolObject = toolsCache.get(toolClassString)) == null) 
        {
        	// Report to user the tool
            System.out.println("\n[Satellite.getToolObject] Tool's Class: " + toolClassString);
            
            // Make sure that the tool being requested isn't null, throw exception if so
            if (toolClassString == null) 
            {
                throw new UnknownToolException();
            }
            
            // Load class in, and create an isntance of it. Save to the tool cache
            // in case it is requested again in the future
            Class toolClass = classLoader.loadClass(toolClassString);
            toolObject = (Tool) toolClass.newInstance();
            toolsCache.put(toolClassString, toolObject);
        } 
        
        // If already in cache, report to user
        else 
        {
            System.out.println("[Satellite.getToolObject] Tool: \"" + toolClassString + "\" already in Cache");
        }

        // Return the tool after creating it or retrieving from cache
        return toolObject;
    }

    // Main class that creates a satellite and runs it
    public static void main(String[] args) 
    {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
