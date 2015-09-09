package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.PositioningError;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.NearestNeighbour;
import positioning.wifi.utils.RadioMap;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * Main for Empirical
 */
public class EmpiricalFKPNN {
    public static void main(String args[]) {
        String offlinePath = "data/MU.1.5meters.offline.trace", onlinePath = "data/MU.1.5meters.online.trace";
        int k = 0;

        // Read arguments
        if(args.length == 1) {
            k = Integer.parseInt(args[0]);
        } else {
            System.out.println("Usage: java EmpiricalFKPNN [k]");
            System.exit(1);
        }

        // Construct parsers
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);

        // Parse file
        ArrayList<TraceEntry> offlineTrace = null;
        ArrayList<TraceEntry> onlineTrace = null;
        try {
            offlineTrace = offlineParser.parse();
            onlineTrace = onlineParser.parse();

            // Build RadioMap
            RadioMap radioMap = new RadioMap(offlineTrace);


            NearestNeighbour nn = new NearestNeighbour(radioMap);

            // Print to file
            PrintWriter writer = new PrintWriter("Empirical_FP_" + k + "_NN", "UTF-8");

            for(TraceEntry traceEntry : onlineTrace) {
                Map<MACAddress, Double> sample = new HashMap<>();

                for(MACAddress mac : traceEntry.getSignalStrengthSamples().getSortedAccessPoints()) {
                    sample.put(mac, traceEntry.getSignalStrengthSamples().getFirstSignalStrength(mac));
                }

                GeoPosition estimatedPosition = Statistics.avgPosition(nn.findNN(sample, k));
                GeoPosition realPosition = traceEntry.getGeoPosition();

                writer.println(realPosition.getX() + " " + realPosition.getY() + " " + realPosition.getZ() + " " + estimatedPosition.getX() + " " + estimatedPosition.getY() + " " + estimatedPosition.getZ());
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.exit(1);
        }
    }
}
