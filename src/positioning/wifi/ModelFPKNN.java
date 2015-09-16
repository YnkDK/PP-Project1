package positioning.wifi;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.Parser;
import positioning.wifi.utils.RadioMapModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Casper on 12-09-2015.
 */
public class ModelFPKNN {

    public static void main(String args[]) {
        // Data files
        String offlinePath = "data/MU.1.5meters.offline.trace";
        String onlinePath = "data/MU.1.5meters.online.trace";

        int k = 0;

        // Read arguments
        if(args.length == 1) {
            k = Integer.parseInt(args[0]);
        } else {
            System.out.println("Usage: java ModelFKPNN [k]");
            System.exit(1);
        }

        // Construct parsers
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);


        try {
            // Construct RadioMapModel
            RadioMapModel radioMapModel = new RadioMapModel();

            // Generate positions based on Model (-24 <= x <= 33, -19 <= y <= 13, 1 meter between each) based on AP positions
            List<GeoPosition> positions = new ArrayList<>();
            for(int x = -24; x <= 33; x++) {
                for(int y = -19; y <= 13; y++) {
                    positions.add(new GeoPosition(x, y, 0));
                }
            }

            // Generate radio map based on model
            Map<GeoPosition, Map<MACAddress, Double>> radioMap = radioMapModel.constructRadioMap(positions);

            // NearNeighbour
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
