package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.APParser2;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 *  Make a plot relating signal strength at various points to the distance from the point to the measured APs.
 */
public class SignalStrengthPlot {

    public static void main(String[] args) {

        String offlinePath = "data/MU.1.5meters.offline.trace", onlinePath = "data/MU.1.5meters.online.trace", apPath = "data/MU.AP.positions";

        //Construct parsers
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);

        //Construct trace generator
        TraceGenerator tg;
        try {
            int offlineSize = 25;
            int onlineSize = 5;
            tg = new TraceGenerator(offlineParser, onlineParser, offlineSize, onlineSize);

            //Generate traces from parsed files
            tg.generate();


                // Get AP
            APParser2 apParser = new APParser2(new File(apPath));
            Map<MACAddress, GeoPosition> apMap = apParser.getAPMap();

            // Print to file
            PrintWriter writer = new PrintWriter("signalStrengthAP", "UTF-8");

            for (TraceEntry traceEntry : tg.getOffline()) {
                System.out.println(traceEntry.toString());
                for (MACAddress macAddress : traceEntry.getSignalStrengthSamples().keySet()) {
                    System.out.println(traceEntry.getSignalStrengthSamples().size());
                    System.out.println(traceEntry.getSignalStrengthSamples().getSignalStrengthValues(macAddress).size());

                    double signalAvg = traceEntry.getSignalStrengthSamples().getAverageSignalStrength(macAddress);
                    GeoPosition ap = apMap.get(macAddress);
                    if(ap != null) {
                        double distance = traceEntry.getGeoPosition().distance(ap);
                        writer.println(distance + "," + signalAvg);
                    }

                }
                System.exit(0);
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
