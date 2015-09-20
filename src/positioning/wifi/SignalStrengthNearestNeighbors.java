package positioning.wifi;


import java.io.PrintWriter;

/**
 * Interface for running a Singal-Strength Nearest Neighbor for a given implementation
 */
public interface SignalStrengthNearestNeighbors {

    /**
     * Runs the algorithm for a given k and writes the output on format:
     * realPosition.x realPosition.y realPosition.z estimatedPosition.x estimatedPosition.y estimatedPosition.z k run_number
     *
     * @param k The number of nearest neighbors to be selected
     * @param pw The initialized PrintWriter for writing the output
     */
    void run(int k, PrintWriter pw);
}
