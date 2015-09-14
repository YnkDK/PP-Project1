import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.io.File;
import java.util.HashMap;
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

    public Map<GeoPosition, Map<MACAddress, Double>> constructRadioMap(List<GeoPosition> positions) {
        Map<GeoPosition, Map<MACAddress, Double>> radioMap = new HashMap<>();

        double threshold = 25; // in meter
        // Iterate all positions and calculate signal to all AP, discard those under the threshold
        for(GeoPosition pos : positions) {
            Map<MACAddress, Double> signalMap = new HashMap<>();

            Map<MACAddress, GeoPosition> apMap = apParser.getAPMap();
            for(MACAddress ap_mac : apMap.keySet()) {
                double distance = apMap.get(ap_mac).distance(pos);
                double signalStrength = computeSignalStrength(distance);

                if(distance < threshold) {
                    signalMap.put(ap_mac, signalStrength);
                }
            }
            radioMap.put(pos, signalMap);
        }

        return radioMap;
    }

    private double computeSignalStrength(double distance) {
        double d0 = 1.0;
        double p_d0 = -33.77; // Minimum signal strength
        double n = 3.415; // Rate which the path loss increases with distance

        return p_d0 - 10 * n * Math.log10(distance / d0);
    }
}
