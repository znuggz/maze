package sparkcog.demo;

/**
 * @author Gabe Jimenez
 * date: April 21, 2019
 *
 * The maze object holds the dimensions of the maze, the starting and ending cells and the cell structure itself.
 * once instantiated every maze object parses the compact text maze representation to get its values. Finally, each maze
 * has some helper methods that print its structural and visual representations.
 *
 * each maze has its origin at the upper left hand of the grid. The grid is zero based, with height increasing downwards
 * in the vertical and width in the horizontal.
 */

public class Maze {

    public int height;                      // maze height
    public int width;                       // maze width
    public Cell[][] structure;              // maze structure with decoded cells
    public Cell startCell;                  // start of the maze
    public Cell endCell;                    // end of the maze

    Maze(String compactMaze) {
        String dims = parseDims(compactMaze);
        this.height = getDim(dims, "height");
        this.width = getDim(dims, "width");
        this.structure = parseStructure(compactMaze);
    }

    /**
     * parses the dimensions of the maze from its compact representation
     *
     * @param dims
     * @param dim
     * @return
     */
    private int getDim(String dims, String dim) {
        dims = dims.replaceAll("[()]","");
        String dimsArr[] = dims.split(",");
        int dimension = 0;

        switch (dim) {
            case "height":
                dimension = Integer.parseInt(dimsArr[0]);
                break;
            case "width":
                dimension = Integer.parseInt(dimsArr[1]);
                break;
            default:
                System.out.println("unkown dimension");
        }

        return dimension;
    }


    /**
     * separates the dimensions from the structure from the maze compact representation
     *
     * @param compactMaze
     * @return
     */
    private String parseDims(String compactMaze) {
        String[] output = compactMaze.split("-");
        String dims = output[0];
        return dims;
    }

    /**
     * parses the structure of the maze itself
     *
     * @param compactMaze
     * @return
     */
    private Cell[][] parseStructure(String compactMaze){
        String[] output = compactMaze.split("-");
        String stringArr = output[1];
        stringArr = stringArr.replaceAll("\\[", "").replaceAll("\\]","");
        String[] stringCellValues = stringArr.split(",");
        Cell[][] cells = new Cell[height][width];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(Integer.parseInt(stringCellValues[idx]),i,j);
                // check for the starting and ending cells/locations
                if (cells[i][j].features.containsKey("START")) {
                    this.startCell = cells[i][j];
                }
                if (cells[i][j].features.containsKey("END")) {
                    this.endCell = cells[i][j];
                }
                idx++;
            }
        }
        return cells;
    }

    /**
     * helper method, prints a visual representation of the maze
     */
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            if (i == 0) {
                System.out.print('+');
                // print the top wall
                for (int j = 0; j < width; j++) {
                        System.out.print(" - - - +");
                }
                System.out.println();
            }

            // print the cell values and left and right walls
            for (int j = 0; j < width; j++) {
                // left wall
                if (j == 0) {
                    System.out.print("| ");
                } else if (structure[i][j].features.get("LEFT") == null) {
                    System.out.print("| ");
                } else {
                    System.out.print("  ");
                }

                // cell content, if any
                if (structure[i][j].features.get("MINE") != null) {
                    System.out.print("  * ");
                } else if (structure[i][j].features.get("END") != null) {
                    System.out.print("  E ");
                } else if (structure[i][j].features.get("START") != null) {
                    System.out.print("  S ");
                } else {
                    System.out.print("    ");
                }

                if (j == width-1) {
                    System.out.print("  |");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();

            // print bottom wall
            System.out.print('+');
            for (int j = 0; j < width; j++) {
                if (structure[i][j].features.get("DOWN") == null) {
                    System.out.print(" - - - +");
                } else {
                    System.out.print("       +");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    /**
     * helper method, prints the maze as a grid with its coded values.
     */
    public void printStructure() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(structure[i][j].code + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
