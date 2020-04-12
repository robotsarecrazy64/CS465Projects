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

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        // ...
        // do we need super here?
        super(satellitePropertiesFile);

        try {
            // init static variables with properties read 
            Properties properties;
            // need to pass info into satelliteInfo object
            properties = new PropertyHandler(satellitePropertiesFile);
            satelliteInfo.setName(properties.getProperty("NAME"));
            satelliteInfo.setPort(Integer.parseInt(properties.getProperty("PORT")));
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
            Properties properties;
            // need to pass info into serverInfo object
            properties = new PropertyHandler(serverPropertiesFile);
            serverInfo.setHost(properties.getProperty("HOST"));
            serverInfo.setPort(Integer.parseInt(properties.getProperty("PORT")));
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
            Properties properties;
            // need to pass info into serverInfo object
            properties = new PropertyHandler(classLoaderPropertiesFile);
            // create HTTPClassLoader object with read-in property data
            classLoader = new HTTPClassLoader(properties.getProperty("HOST"), Integer.parseInt(properties.getProperty("PORT")));
            System.out.println("Class loader has host: " + properties.getProperty("HOST") + " and has port #: " + Integer.parseInt(properties.getProperty("PORT")));
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

        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ...
        
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            // ...
            
            // reading message
            // ...
            
            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
                    break;

                default:
                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // ...
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        // change back to:
        // Satellite satellite = new Satellite(args[0], args[1], args[2]);
        // after finished developing
        Satellite satellite = new Satellite("../../config/Satellite.Earth.properties", "../../config/WebServer.properties", "../../config/Server.properties");
        satellite.run();
    }
}
