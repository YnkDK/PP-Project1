import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Casper on 12-09-2015.
 */
public class APParser {
    Map<MACAddress, GeoPosition> apMap = new HashMap<MACAddress, GeoPosition>();

    public APParser(File f) throws Exception {
        Scanner sc = new Scanner(f);

        // Skip first line
        sc.nextLine();

        // Parse APs and add to apMap
        while(sc.hasNext()) {
            String macAddress = sc.next();
            Double x = Double.parseDouble(sc.next());
            Double y = Double.parseDouble(sc.next());
            Double z = Double.parseDouble(sc.next());

            apMap.put(MACAddress.parse(macAddress), new GeoPosition(x, y, z));
        }
    }

    public Map<MACAddress, GeoPosition> getAPMap() {
        return apMap;
    }
}
