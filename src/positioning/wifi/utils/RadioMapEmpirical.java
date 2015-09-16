package positioning.wifi.utils;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RadioMapEmpirical {
    private List<RadioEntry> radioMap;
//    private Map<GeoPosition, Map<MACAddress, Double>> radioMap;

    public RadioMapEmpirical(List<TraceEntry> traceEntries) {

        // For each position we create a map that has a list with signal strengths for each AP.
        Map<GeoPosition, Map<MACAddress, List<Double>>> geoPositionMapToSignal = new HashMap<>();
        radioMap = new LinkedList<>();

        // Fill the map: geoPositionMapToSignal
        for(TraceEntry te : traceEntries) {
            GeoPosition gp = te.getGeoPosition();
            if(!geoPositionMapToSignal.containsKey(gp)) {
                geoPositionMapToSignal.put(gp, new HashMap<MACAddress, List<Double>>());
            }

            SignalStrengthSamples samples = te.getSignalStrengthSamples();
            // Loop through all AP's which is reachable from the position.
            for(MACAddress mac : samples.getSortedAccessPoints()) {
                Map<MACAddress, List<Double>> signals = geoPositionMapToSignal.get(gp);
                if(!signals.containsKey(mac)) {
                    signals.put(mac, new LinkedList<Double>());
                }
                // Save signal strengths to a certain AP.
                signals.get(mac).add(samples.getFirstSignalStrength(mac));
            }
        }

        // Loop through all the entries in the map created above.
        for(Map.Entry<GeoPosition, Map<MACAddress, List<Double>>> entry : geoPositionMapToSignal.entrySet()) {
            GeoPosition currentPos = entry.getKey();
            Map<MACAddress, List<Double>> value = entry.getValue();

            // For each AP, find the average of all the signal strengths to that AP.
            Map<MACAddress, Double> avgSignal = new HashMap<>();
            for(Map.Entry<MACAddress, List<Double>> macStrength : value.entrySet()) {
                avgSignal.put(macStrength.getKey(), Statistics.avg(macStrength.getValue()));
            }

            radioMap.add(new RadioEntry(currentPos, avgSignal));
        }
    }


    public List<RadioEntry> getEntries() {
        return radioMap;
    }
}
