package positioning.wifi;


import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.PositioningError;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScoreNN {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: ...");
            System.exit(1);
        }

        List<PositioningError> positioningErrors = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] values = line.split(" ");
                assert values.length == 6;
                GeoPosition realPosition = new GeoPosition(
                        Double.parseDouble(values[0]),
                        Double.parseDouble(values[1]),
                        Double.parseDouble(values[2])
                );
                GeoPosition estimatedPosition = new GeoPosition(
                        Double.parseDouble(values[3]),
                        Double.parseDouble(values[4]),
                        Double.parseDouble(values[5])
                );
                positioningErrors.add(new PositioningError(realPosition, estimatedPosition));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Collections.sort(positioningErrors);

        try(PrintWriter writer = new PrintWriter(args[0] + ".distribution", "UTF-8")) {
            for (int i = 0; i < positioningErrors.size(); i++) {
                PositioningError pe = positioningErrors.get(i);
                writer.println(pe.getPositioningError() + " " + (i+1.0)/positioningErrors.size());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
