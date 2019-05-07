package sparkcog.demo;
/**
 * @author Gabe Jimenez
 * date: April 21, 2019
 *
 * the cell is the most basic element of a maze, it contains features that indicate the connections to other cells
 * and other state information such as its location on a grid, whether it contains a mine, or whether it is a starging
 * or ending cell.
 */

import java.util.HashMap;
import java.util.Map;

public class Cell {
    public int code;                                    // compact representation of cell features
    public int h;                                       // height - vertical location of cell on grid
    public int w;                                       // width - horizontal location of cell on grid
    public Map<String,Integer> features;                // a map of all the features in this cell

    public Cell (int code, int h, int w) {
        this.code = code;
        this.h  = h;
        this.w = w;
        this.features = getFeatures(code);
    }

    /**
     * takes in a code that is a binary representation of the features in this cell
     *
     * @param code
     * @return a reference to a hashmap
     */
    public Map<String,Integer> getFeatures(int code) {
        // using a map for quicker access
        Map<String,Integer> features = new HashMap<>();

        if ((code & 64) > 0) {
            features.put("MINE",1);
        }

        if ((code & 32) > 0) {
            features.put("END",1);
        }

        if ((code & 16) > 0) {
            features.put("START",1);
        }

        if ((code & 8) > 0) {
            features.put("LEFT",1);
        }

        if ((code & 4) > 0) {
            features.put("DOWN",1);
        }

        if ((code & 2) > 0) {
            features.put("RIGHT",1);
        }

        if ((code & 1) > 0) {
            features.put("UP",1);
        }

        return features;
    }
}
