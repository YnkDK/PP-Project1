package positioning.wifi.utils;


import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.util.*;

public class NearestNeighbour {
    private List<RadioEntry> radioMapEntries;
    public NearestNeighbour(List<RadioEntry> radioMapEntries) {
        this.radioMapEntries = radioMapEntries;
    }

    /**
     * Finds the nearest neighbour to the given argument using the radio map
     *
     * @param sample The sample to which the nearest neighbour should be found
     * @return The best guess, i.e. nearest neighbour
     */
    public GeoPosition findNN(Map<MACAddress, Double> sample) {
        double best = Double.MAX_VALUE;
        RadioEntry bestEntry = null;
        for(RadioEntry re : radioMapEntries) {
            double d = re.distance(sample);
            if(d < best) {
                best = d;
                bestEntry = re;
            }
        }
        assert bestEntry != null;
        return bestEntry.getPosition();
    }

    /**
     * Finds the k-nearest neighbour to the given argument using the radio map
     *
     * @param sample The sample to which the nearest neighbour should be found
     * @param k The number of nearest neighbors to find
     * @return The k nearest neighbors (sorted in closest proximity)
     */
    public GeoPosition[] findNN(Map<MACAddress, Double> sample, int k) {
        GeoPosition[] result = new GeoPosition[k];
        Queue<DistanceToRadioEntryTuple> distances = new PriorityQueue<>(radioMapEntries.size(), new CompareTuple());
        for(RadioEntry re : radioMapEntries) {
            double d = re.distance(sample);
            // Since we are using a priority queue and CompareTuple,
            // the add methods ensures that the nearest neighbors are
            // in the top of the queue
            distances.add(new DistanceToRadioEntryTuple(re, d));
        }
        for(int i = 0; i < k; i++) {
            result[i] = (distances.remove().x.getPosition());
        }
        return result;
    }

    private class DistanceToRadioEntryTuple {
        public final RadioEntry x;
        public final double y;
        public DistanceToRadioEntryTuple(RadioEntry x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private class CompareTuple implements Comparator<DistanceToRadioEntryTuple> {

        @Override
        public int compare(DistanceToRadioEntryTuple t0, DistanceToRadioEntryTuple t1) {
            return (int) (t0.y - t1.y);
        }
    }
}
