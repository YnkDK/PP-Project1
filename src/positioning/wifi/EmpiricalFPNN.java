package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import positioning.wifi.utils.NearestNeighbour;
import positioning.wifi.utils.RadioMap;

import java.io.File;
import java.io.IOException;
import java.util.*;

// empirical_FP_NN
public class EmpiricalFPNN {
    public static void main(String[] args) {
        int k = -1;
        File traceFile = null;
        GeoPosition guess;
        Map<MACAddress, Double> query;
        try {
            if (args.length == 1) {
                k = 1;
                traceFile = new File(args[0]);
            } else if (args.length == 2) {
                traceFile = new File(args[0]);
                k = Integer.parseInt(args[1]);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            printUsage();
            System.exit(1);
        }
        Parser fileParser = new Parser(traceFile);
        ArrayList<TraceEntry> measurements = null;
        try {
            measurements = fileParser.parse();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        RadioMap rm = new RadioMap(measurements);
        NearestNeighbour nn = new NearestNeighbour(rm);

        // TODO: How to get fingerprint of query?
        // Should it be a simple cross validation that runs by default?
        // I.e. introduce a rm.sample() method, which splits the radio map
        // into e.g. 90% training and 10% testing data
        query = rm.getEntries().get(0).getEntries();

        // Find guess using NNSS
        if(k == 1) {
            guess = nn.findNN(query);
        } else {
            GeoPosition[] best = nn.findNN(query, k);
            guess = Statistics.avgPosition(best);
        }
        System.out.print("Using the trace file " + args[1] + " and k = " + k + " the position is: ");
        System.out.println(guess);
        System.out.println("Ground truth was: " + rm.getEntries().get(0).getPosition());
    }

    private static void printUsage() {
        System.out.println("Usage: java EmpiricalFPNN traceFile [k]");
    }
}
