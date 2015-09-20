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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mys on 9/20/15.
 */
public class Empirical_FP_NN implements SignalStrengthNearestNeighbors {


    private final TraceGenerator tg;
    private static int run_number;

    public Empirical_FP_NN() {
        Empirical_FP_NN.run_number = 0;
        String OFFLINE_PATH = "data/MU.1.5meters.offline.trace";
        File offlineFile = new File(OFFLINE_PATH);
        Parser offlineParser = new Parser(offlineFile);
        int offlineSize = 25;

        String ONLINE_PATH = "data/MU.1.5meters.online.trace";
        File onlineFile = new File(ONLINE_PATH);
        Parser onlineParser = new Parser(onlineFile);
        int onlineSize = 5;
        this.tg = getTg(onlineParser, offlineParser, offlineSize, onlineSize);
        assert this.tg != null;
    }

    private TraceGenerator getTg(Parser onp, Parser ofp, int ofs, int ons) {
        try {
            return new TraceGenerator(onp, ofp, ofs, ons);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run(int k, PrintWriter pw) {
        // Increase the number of runs by 1
        Empirical_FP_NN.run_number++;
        // Generate new traces
        this.tg.generate();
        // Get the offline and online traces to be used in this run
        List<TraceEntry> offlineTrace = this.tg.getOffline();
        List<TraceEntry> onlineTrace = this.tg.getOnline();
        // Build the radio map
        RadioMapEmpirical radioMapEmpirical = new RadioMapEmpirical(offlineTrace);
        // Prepare the nearest neighbour search
        NearestNeighbour nn = new NearestNeighbour(radioMapEmpirical.getEntries());
        // Find nearest neighbours for each online trace
        for(TraceEntry traceEntry : onlineTrace) {
            // Prepare the query
            Map<MACAddress, Double> query = new HashMap<>();

            for(MACAddress mac : traceEntry.getSignalStrengthSamples().getSortedAccessPoints()) {
                query.put(mac, traceEntry.getSignalStrengthSamples().getAverageSignalStrength(mac));
            }
            // Find the estimated position using k-nearest neighbour search
            GeoPosition estimatedPosition = Statistics.avgPosition(nn.findNN(query, k));
            // Get the real position
            GeoPosition realPosition = traceEntry.getGeoPosition();
            // Print to the file
            pw.print(realPosition.getX() + " " + realPosition.getY() + " " + realPosition.getZ() + " ");
            pw.print(estimatedPosition.getX() + " " + estimatedPosition.getY() + " " + estimatedPosition.getZ() + " ");
            pw.println(k + " " + Empirical_FP_NN.run_number);
        }
    }
}
