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
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable<String, Tool> toolsCache = null;
    Properties satelliteProperties;
    Properties serverProperties;
    Properties classLoaderProperties;
    static ServerSocket serverSocket;
    static int port;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        // ...
        // do we need super here?
        super(satellitePropertiesFile);

        try {
            // init static variables with properties read 
            // need to pass info into satelliteInfo object
            satelliteProperties = new PropertyHandler(satellitePropertiesFile);
            satelliteInfo.setName(satelliteProperties.getProperty("NAME"));
            satelliteInfo.setPort(Integer.parseInt(satelliteProperties.getProperty("PORT")));
            System.out.println("Satellite " + satelliteInfo.getName() + " has port #: " + satelliteInfo.getPort());
        } catch (Exception e) {
            System.err.println("Properties file " + satellitePropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        // ...
        try {
            // init static variables with properties read 
            // need to pass info into serverInfo object
            serverProperties = new PropertyHandler(serverPropertiesFile);
            serverInfo.setHost(serverProperties.getProperty("HOST"));
            serverInfo.setPort(Integer.parseInt(serverProperties.getProperty("PORT")));
            System.out.println("Application server has host: " + serverInfo.getHost() + " and has port #: " + serverInfo.getPort());
        } catch (Exception e) {
            System.err.println("Properties file " + serverPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // read properties of the code server and create class loader
        // -------------------
        // ...
        try {
            // init static variables with properties read 
            // need to pass info into serverInfo object
            classLoaderProperties = new PropertyHandler(classLoaderPropertiesFile);
            // create HTTPClassLoader object with read-in property data
            classLoader = new HTTPClassLoader(classLoaderProperties.getProperty("HOST"), Integer.parseInt(classLoaderProperties.getProperty("PORT")));
            System.out.println("Class loader has host: " + classLoaderProperties.getProperty("HOST") + " and has port #: " + Integer.parseInt(classLoaderProperties.getProperty("PORT")));
        } catch (Exception e) {
            System.err.println("Properties file " + classLoaderPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
        
        // create tools cache
        // -------------------
        // ...
        toolsCache = new Hashtable<>();
    }

    @Override
    public void run() {
        Socket socket = null;
        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ignore for now
        
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
        try {
            serverSocket = new ServerSocket(satelliteInfo.getPort());
            
            // start taking job requests in a server loop
            // ---------------------------------------------------------------
            while(true)
            {
                socket = serverSocket.accept(); 
                new SatelliteThread(socket, this).start();
            }
        } 
        catch (IOException ioe) 
        {
            System.err.println("IOException" + ioe.getMessage());
            ioe.printStackTrace();
        }
        
        
        // TODO: confirmation messages
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;
        boolean waluigi = true;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            
            // setting up object streams
            // ...
            try {
                writeToNet = new ObjectOutputStream(jobRequest.getOutputStream());
                readFromNet = new ObjectInputStream(jobRequest.getInputStream());
                
            }
            catch (Exception exception)
            {
                System.out.println("[Satellite.run] Failed to open object streams");
                exception.printStackTrace();
                System.exit(1);
            }
                while(waluigi)
                {
                // reading message
                // ...
                    try
                    {
                        message = (Message) readFromNet.readObject();
                        
                        
                    }
                    catch (IOException | ClassNotFoundException exception)
                    {
                        System.out.println("[SatelliteThread.run] Message could not be read from object stream.");
                        System.exit(1);
                    }
                    switch (message.getType()) 
                    {
                        case JOB_REQUEST:
                            // processing job request
                            // ...
                            try 
                            {
                                Job getJob = (Job) message.getContent();
                                System.out.println("Tool: " + getJob.getToolName());
                                Tool getTool = getToolObject(getJob.getToolName());
                                // get function results
                                Object returnToClient = getTool.go(getJob.getParameters());
                                // use tool function
                                writeToNet.writeObject(returnToClient);
                                writeToNet.flush();
                                
                                readFromNet.close();
                                writeToNet.close();
                                
                                jobRequest.close();
                                waluigi = false;
                            }
                            catch (Exception ex)
                            {
                                      System.out.println("Error occurred: " + ex);
                            }
                            break;

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
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //TODO: fix plox
        Tool toolObject;

        if ((toolObject = toolsCache.get(toolClassString)) == null) 
        {
            System.out.println("\nTool's Class: " + toolClassString);
            
            if (toolClassString == null) 
            {
                throw new UnknownToolException();
            }
            
            Class toolClass = classLoader.loadClass(toolClassString);
            toolObject = (Tool) toolClass.newInstance();
            toolsCache.put(toolClassString, toolObject);
        } 
        else 
        {
            System.out.println("Tool: \"" + toolClassString + "\" already in Cache");
        }

        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
