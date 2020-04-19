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

    public LoadManager() {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName) {
        
        // add satellite name to array list
        satellites.add(satelliteName);
        
        // increment last index of array list
        lastSatelliteIndex += 1;
    }


    public String nextSatellite() throws Exception {
        
        int numberSatellites = lastSatelliteIndex + 1;
        String satelliteReturn = "";
        ListIterator iter = satellites.listIterator();
        synchronized (satellites) {
            // implement policy that returns the satellite name according to a round robin methodology
            satelliteReturn = iter.toString();
        }

        return satelliteReturn; // name of satellite who is supposed to take job
    }
}
