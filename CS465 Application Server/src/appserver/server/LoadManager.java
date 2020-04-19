package appserver.server;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager {

    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;
    static int currentSatellite = 0;

    public LoadManager() {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName) {
        
        // add satellite name to array list
        satellites.add(satelliteName);
        
        // increment last index of array list
        lastSatelliteIndex += 1;
    }


    public String nextSatellite() throws Exception 
    {
    	// Declare variables
        String nextSatellite;
        
        synchronized(satellites)
        {
        	// Set next satellite's name
	        nextSatellite = satellites.get(currentSatellite).toString();
	        
	        // Keep satellite counter on track, make sure it looks
	        if(currentSatellite == lastSatelliteIndex)
	        {
	        	currentSatellite = 0;
	        }
	        
	        // Increase index to next satellite if not at all
	        else
	        {
	        	currentSatellite++;
	        }
        }
        
        return nextSatellite; // name of satellite who is supposed to take job
    }
}
