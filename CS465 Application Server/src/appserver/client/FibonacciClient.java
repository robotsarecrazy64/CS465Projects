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
public class FibonacciClient {
    
    String host = null;
    int port;

    Properties properties;

    public FibonacciClient(String serverPropertiesFile, int fibNum) 
    {
        try 
        {
            properties = new PropertyHandler(serverPropertiesFile);
            host = properties.getProperty("HOST");
            System.out.println("[FibonacciClient.FibonacciClient] Host: " + host);
            port = Integer.parseInt(properties.getProperty("PORT"));
            System.out.println("[FibonacciClient.FibonacciClient] Port: " + port);
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void run() 
    {
        try { 
            
            
        } 
        catch (Exception ex) 
        {
            System.err.println("[PlusOneClient.run] Error occurred");
            ex.printStackTrace();
        }
    }
        
    private class FibonacciClientThread extends Thread 
    {
        

        @Override
        public void run() 
        {

        }
        }
   

    public static void main(String[] args) {
        
        for(int iter = 46; iter > 0; iter--)
        {
            (new FibonacciClient("../../config/Server.properties", iter)).start();
        }
    }  
    
}
