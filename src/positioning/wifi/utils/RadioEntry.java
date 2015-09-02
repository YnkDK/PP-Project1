package positioning.wifi.utils;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.util.Map;

/**
 * Created by mys on 9/2/15.
 */
public class RadioEntry {
    private GeoPosition position;
    private Map<MACAddress, Double> macAddressDoubleMap;

    public RadioEntry(GeoPosition position, Map<MACAddress, Double> macAddressDoubleMap) {
        this.position = position;
        this.macAddressDoubleMap = macAddressDoubleMap;
    }

    public Map<MACAddress, Double> getEntries() {
        return macAddressDoubleMap;
    }

    public GeoPosition getPosition() {
        return position;
    }

    public double distance(Map<MACAddress, Double> other) {
        double dist = 0;
        for(MACAddress mac : other.keySet()) {
            if(!macAddressDoubleMap.containsKey(mac)) {
                continue;
            }
            dist += Math.pow(other.get(mac) - macAddressDoubleMap.get(mac), 2);
        }
        return Math.sqrt(dist);
    }
}
