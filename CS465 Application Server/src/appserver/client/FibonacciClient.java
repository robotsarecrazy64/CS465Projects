/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.client;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import appserver.job.Job;
import appserver.job.Tool;
import appserver.satellite.Satellite;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Jessica
 */
public class FibonacciClient extends Thread{
    
    String host = null;
    int port;
    Integer fibNum;

    Properties properties;

    public FibonacciClient(String serverPropertiesFile, int fibNumParam) 
    {
        try 
        {
            properties = new PropertyHandler(serverPropertiesFile);
            host = properties.getProperty("HOST");
            System.out.println("[FibonacciClient.FibonacciClient] Host: " + host);
            port = Integer.parseInt(properties.getProperty("PORT"));
            System.out.println("[FibonacciClient.FibonacciClient] Port: " + port);
            fibNum = fibNumParam;
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void run() 
    {
        try { 
            
            // connect to application server
            Socket server = new Socket(host, port);
            // hard-coded string of class, aka tool name ... plus one argument
            String classString = "appserver.job.impl.Fibonacci";
            //Integer number = new Integer(42);
            
            // create job and job request message
            Job job = new Job(classString, fibNum);
            Message message = new Message(JOB_REQUEST, job);
            
            // sending job out to the application server in a message
            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            writeToNet.writeObject(message);
            
            // reading result back in from application server
            // for simplicity, the result is not encapsulated in a message
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());
            Integer result = (Integer) readFromNet.readObject();
            System.out.println("RESULT: " + result);
        } 
        
        catch (Exception ex) 
        {
            System.err.println("[FibonacciClient.run] Error occurred");
            ex.printStackTrace();
        }
    }
   

    public static void main(String[] args) {
        
        for(int iter = 46; iter > 0; iter--)
        {
            (new FibonacciClient("../../config/Server.properties", iter)).start();
        }
    }  
    
}
