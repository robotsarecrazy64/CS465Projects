package appserver.server;

import appserver.comm.ConnectivityInfo;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class SatelliteManager {

    // (the one) hash table that contains the connectivity information of all satellite servers
    static private Hashtable<String, ConnectivityInfo> satellites = null;

    public SatelliteManager() {
        // ..
    }

    public void registerSatellite(ConnectivityInfo satelliteInfo) {
        satellites.put(satelliteInfo.getName(), satelliteInfo);
    }

    public ConnectivityInfo getSatelliteForName(String satelliteName) {
    	//Enumeration<String> enumeration = satellites.keys();
        return satellites.get(satelliteName);
    }
}
