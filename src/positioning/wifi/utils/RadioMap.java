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

public class RadioMap {
    private List<RadioEntry> radioMap;
//    private Map<GeoPosition, Map<MACAddress, Double>> radioMap;

    public RadioMap(List<TraceEntry> measurements) {
        Map<GeoPosition, Map<MACAddress, List<Double>>> geoPositionMapToSignal = new HashMap<>();
        radioMap = new LinkedList<>();
        for(TraceEntry te : measurements) {
            GeoPosition gp = te.getGeoPosition();
            if(!geoPositionMapToSignal.containsKey(gp)) {
                geoPositionMapToSignal.put(gp, new HashMap<MACAddress, List<Double>>());
            }
            SignalStrengthSamples samples = te.getSignalStrengthSamples();
            for(MACAddress mac : samples.getSortedAccessPoints()) {
                samples.getSignalStrengthValues(mac);

                Map<MACAddress, List<Double>> signals = geoPositionMapToSignal.get(gp);
                if(!signals.containsKey(mac)) {
                    signals.put(mac, new LinkedList<Double>());
                }

                signals.get(mac).add(samples.getFirstSignalStrength(mac));
            }
        }

        for(Map.Entry<GeoPosition, Map<MACAddress, List<Double>>> entry : geoPositionMapToSignal.entrySet()) {
            GeoPosition currentPos = entry.getKey();
            Map<MACAddress, List<Double>> value = entry.getValue();

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
