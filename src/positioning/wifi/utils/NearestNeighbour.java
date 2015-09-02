package positioning.wifi.utils;


import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.util.List;
import java.util.Map;

public class NearestNeighbour {
    private RadioMap radioMap;
    public NearestNeighbour(RadioMap rm) {
        radioMap = rm;
    }

    public GeoPosition findNN(Map<MACAddress, Double> sample) {
        double best = Double.MAX_VALUE;
        RadioEntry bestEntry = null;
        for(RadioEntry re : radioMap.getEntries()) {
            double d = re.distance(sample);
            if(d < best) {
                best = d;
                bestEntry = re;
            }
        }
        System.out.println(best);
        return bestEntry.getPosition();
    }
}
