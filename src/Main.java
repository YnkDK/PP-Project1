import positioning.wifi.Empirical_FP_NN;
import positioning.wifi.Model_FP_NN;
import positioning.wifi.SignalStrengthNearestNeighbors;

import java.io.*;

/**
 * Created by mys on 9/20/15.
 */
public class Main {
    public static void main(String[] args) {
        String data = null;
        int k = 0, num_runs = 0;
        SignalStrengthNearestNeighbors ssnn = null;
        try {
            data = args[0];
            k = Integer.parseInt(args[1]);
            num_runs = Integer.parseInt(args[2]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            printUsage();
            System.exit(1);
        }

        // Initialize the SS_NN type
        if("empirical".compareToIgnoreCase(data) == 0) {
            ssnn = new Empirical_FP_NN();
        } else if ("model".compareToIgnoreCase(data) == 0) {
            ssnn = new Model_FP_NN();
        } else {
            printUsage();
            System.exit(1);
        }

        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output/" + data + "_FP_NN", true)))) {
            System.out.println("Running " + data + " " + num_runs + " times with k = "+ k +"...");
            for(int i = 0; i < num_runs; i++) {
                ssnn.run(k, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java Main [empirical|model]:string k:int num_runs:int");
    }
}
