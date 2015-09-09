package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.Parser;

import java.io.File;
import java.util.Map;

public class ModelFPKNN {
    public static void main(String[] args) {

        String onlinePath = "data/MU.1.5meters.online.trace";
        String offlinePath = "data/MU.1.5meters.offline.trace";
        int k;

        if (args.length == 1)
            k = Integer.parseInt(args[0]);
        else {
            System.out.println("Only 1 argument allowed");
            System.exit(0);
        }

        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);

        // Build radio map:
        //  - Define a grid map
        //  - For each point in the grid, calculate signal strength to each AP (save if ss is above some threshold)

        // Trace generator?

    }

    public double computeSignalStrength(double distance) {

        double d0 = 1;
        double p_d0 = -33.77; // Minimum signal strength
        double n = 3.415; // Rate which the path loss increases with distance

        return p_d0 - 10 * n * Math.log10(distance/d0);
    }
}
