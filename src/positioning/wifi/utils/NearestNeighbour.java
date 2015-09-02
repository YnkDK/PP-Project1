package positioning.wifi.utils;


import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.TraceEntry;

import java.util.*;

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
        assert bestEntry != null;
        return bestEntry.getPosition();
    }

    public List<GeoPosition> findNN(Map<MACAddress, Double> sample, int k) {
        List<GeoPosition> result = new ArrayList<>(k);
        List<RadioEntry> radioEntries = radioMap.getEntries();
        Queue<Tuple> distances = new PriorityQueue<>(radioEntries.size(), new CompareTuple());
        for(RadioEntry re : radioEntries) {
            double d = re.distance(sample);
            distances.add(new Tuple(re, d));
        }
        for(int i = 0; i < k; i++) {
            result.add(distances.remove().x.getPosition());
        }
        return result;
    }

    private class Tuple<X, Y> {
        public final RadioEntry x;
        public final double y;
        public Tuple(RadioEntry x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private class CompareTuple implements Comparator<Tuple> {

        @Override
        public int compare(Tuple t0, Tuple t1) {
            return (int) (t0.y - t1.y);
        }
    }
}
