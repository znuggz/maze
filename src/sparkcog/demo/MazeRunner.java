package sparkcog.demo;

/**
 *
 * @author Gabe Jimenez
 * date: April 21, 2019
 *
 * for: SparkCognition test demo
 * this class loads mazes from a file and creates maze objects, the maze objects are then passed on
 * to the Maze Solver class.
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MazeRunner {

    public static void main(String[] args) {
        // try to load the maze file
        try (Stream<String> mazeStreams = Files.lines(Paths.get("../../mazes.txt")))
        {
            // a maze solver class to solve each maze object
            MazeSolver solver = new MazeSolver();
            // maze container
            List<Maze> mazeList = new ArrayList<>();
            // copy the maze strings from the streams to a list
            mazeStreams.forEach(s -> mazeList.add(new Maze(s)));
            for (Maze maze : mazeList) {
                // maze.printStructure();
                System.out.println();
                // maze.printMaze();
                // solve
                solver.solve(maze,3);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
