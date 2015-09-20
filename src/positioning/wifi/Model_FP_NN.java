package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.NearestNeighbour;
import positioning.wifi.utils.RadioEntry;
import positioning.wifi.utils.RadioMapModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mys on 9/20/15.
 */
public class Model_FP_NN implements SignalStrengthNearestNeighbors {
    private static int run_number;

    private final TraceGenerator tg;
    private final NearestNeighbour nn;


    public Model_FP_NN() {
        Model_FP_NN.run_number = 0;
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

        // Generate positions based on Model (-24 <= x <= 33, -19 <= y <= 13, 1 meter between each) based on AP positions
        final List<GeoPosition> positions = new ArrayList<>();
        for(int x = -24; x <= 33; x++) {
            for(int y = -19; y <= 13; y++) {
                positions.add(new GeoPosition(x, y, 0));
            }
        }

        // Construct RadioMapModel
        RadioMapModel radioMapModel = null;
        try {
            radioMapModel = new RadioMapModel();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        // Generate radio map based on model
        List<RadioEntry> radioMap = radioMapModel.constructRadioMap(positions);

        // NearNeighbour
        nn = new NearestNeighbour(radioMap);
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
        Model_FP_NN.run_number++;
        // Generate new traces
        this.tg.generate();

        for(TraceEntry traceEntry : this.tg.getOnline()) {
            // Prepare the query
            Map<MACAddress, Double> query = new HashMap<>();
            for (MACAddress mac : traceEntry.getSignalStrengthSamples().getSortedAccessPoints()) {
                query.put(mac, traceEntry.getSignalStrengthSamples().getFirstSignalStrength(mac));
            }
            // Find the estimated position using k-nearest neighbour search
            GeoPosition estimatedPosition = Statistics.avgPosition(this.nn.findNN(query, k));
            // Get the real position
            GeoPosition realPosition = traceEntry.getGeoPosition();

            // Print to the file
            pw.print(realPosition.getX() + " " + realPosition.getY() + " " + realPosition.getZ() + " ");
            pw.print(estimatedPosition.getX() + " " + estimatedPosition.getY() + " " + estimatedPosition.getZ() + " ");
            pw.println(k + " " + Model_FP_NN.run_number);
        }
    }
}
