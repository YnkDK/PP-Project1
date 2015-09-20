package positioning.wifi.utils;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Casper on 12-09-2015.
 */
public class RadioMapModel {
    APParser apParser;

    public RadioMapModel() throws Exception {
        // Data file
        String apPath =  "data/MU.AP.positions";
        apParser = new APParser(new File(apPath));
    }

    public List<RadioEntry> constructRadioMap(List<GeoPosition> positions) {
        List<RadioEntry> radioMap  = new LinkedList<>();

        double threshold = -100; // SignalStrength
        // Iterate all positions and calculate signal to all AP, discard those under the threshold
        int removed =0;
        for(GeoPosition pos : positions) {
            Map<MACAddress, Double> signalMap = new HashMap<>();

            Map<MACAddress, GeoPosition> apMap = apParser.getAPMap();
            for(MACAddress ap_mac : apMap.keySet()) {
                double distance = apMap.get(ap_mac).distance(pos);
                double signalStrength = computeSignalStrength(distance);
                if(signalStrength > threshold) {
                    signalMap.put(ap_mac, signalStrength);
                } else {
                    removed++;
                }

            }
            radioMap.add(new RadioEntry(pos, signalMap));
        }
        System.out.println("Size: " + radioMap.size() + " Removed: " + removed);
        return radioMap;
    }

    private double computeSignalStrength(double distance) {
        double d0 = 1.0;
        double p_d0 = -33.77; // Minimum signal strength
        double n = 3.415; // Rate which the path loss increases with distance

        return p_d0 - 10 * n * Math.log10(distance / d0);
    }
}
