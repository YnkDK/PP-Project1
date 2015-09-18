package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.NearestNeighbour;
import positioning.wifi.utils.RadioMapEmpirical;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * empirical_FP_NN
 */
public class EmpiricalFPNN {
    public static void main(String[] args) {
        String offlinePath = "data/MU.1.5meters.offline.trace", onlinePath = "data/MU.1.5meters.online.trace";

        // Construct parsers
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);

        // Parse file
        ArrayList<TraceEntry> offlineTrace;
        ArrayList<TraceEntry> onlineTrace;

        //Construct trace generator
        TraceGenerator tg;

        try {
            int offlineSize = 25; // For each position, generate 25 measurements. Each measurement consist of
                                  // a single signal strength measurement to an AP.
            int onlineSize = 5;
            tg = new TraceGenerator(offlineParser, onlineParser, offlineSize, onlineSize);

            tg.generate();

            offlineTrace = tg.getOffline();
            onlineTrace = tg.getOnline();

            RadioMapEmpirical rm = new RadioMapEmpirical(offlineTrace);
            NearestNeighbour nn = new NearestNeighbour(rm.getEntries());

            // Print to file
            PrintWriter writer = new PrintWriter("Empirical_FP_1_NN", "UTF-8");

            // Loop through all online traces and find nearest neighbour based on the offline
            // measurements. Write estimated position based on k-nearest neighbours and the real
            // position to a file.
            for(TraceEntry traceEntry : onlineTrace) {
                Map<MACAddress, Double> sample = new HashMap<>();

                // A sample is a map with reachable APs and the signal strength to the APs
                for(MACAddress mac : traceEntry.getSignalStrengthSamples().getSortedAccessPoints()) {
                    sample.put(mac, traceEntry.getSignalStrengthSamples().getFirstSignalStrength(mac));
                }

                GeoPosition estimatedPosition = Statistics.avgPosition(nn.findNN(sample, 1));
                GeoPosition realPosition = traceEntry.getGeoPosition();

                writer.println(realPosition.  getX() + " " + realPosition.getY() + " " + realPosition.getZ() + " " + estimatedPosition.getX() + " " + estimatedPosition.getY() + " " + estimatedPosition.getZ());
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.exit(1);
        }
    }
}
