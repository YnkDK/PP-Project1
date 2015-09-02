package positioning.wifi.utils;


import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class APParser {
    private final File file;

    public APParser(File file) {
        if (file == null)
            throw new IllegalArgumentException("file cannot be null");
        this.file = file;
    }

    public HashMap<MACAddress, GeoPosition> parse() throws IOException, NumberFormatException{
        HashMap<MACAddress, GeoPosition> hashMap = new HashMap<>();

        String line;

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            while ((line = in.readLine()) != null) {
                if(line.startsWith("#")) continue;
                String[] components = line.split(" ");
                if(components.length != 4) {
                    throw new IOException("Expected four components, found: " + components.length);
                }
                hashMap.put(
                        MACAddress.parse(components[0]),
                        new GeoPosition(
                                Double.parseDouble(components[1]),
                                Double.parseDouble(components[2]),
                                Double.parseDouble(components[3])
                        )
                );
            }
        }
        return hashMap;
    }
}
