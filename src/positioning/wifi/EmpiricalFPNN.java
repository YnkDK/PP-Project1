package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.NearestNeighbour;
import positioning.wifi.utils.RadioMap;

import java.io.File;
import java.io.IOException;
import java.util.*;

// empirical_FP_NN
public class EmpiricalFPNN {
    public static void main(String[] args) {
        File file = new File("data/MU.1.5meters.offline.trace");
        Parser fileParser = new Parser(file);
        ArrayList<TraceEntry> measurements = null;
        try {
            measurements = fileParser.parse();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        RadioMap rm = new RadioMap(measurements);
        NearestNeighbour nn = new NearestNeighbour(rm);

        GeoPosition best = nn.findNN(rm.getEntries().get(0).getEntries());
        System.out.println("best = " + best);
    }
}
