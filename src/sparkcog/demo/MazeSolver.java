package sparkcog.demo;

/**
 * @author Gabe Jimenez
 * date: April 21, 2019
 *
 * the maze solver class takes in a maze and uses the Breadth First Search algorithm to traverse the maze. Once the
 * algorithm completes, we simply "walk back" from the end to the start of the maze.
 *
 * I chose BFS over A* because BFS uses a queue while A* uses a priority queue. Usually, queues are much faster than
 * priority queues (eg. Dequeue() is O(1) vs O(log n)). Normally A* expands far fewer nodes than BFS, but if that is
 * not the case, BFS will be faster.
 *
 * That can happen if the heuristic used is not very good, or if the graph is very sparse or small, or if the heuristic
 * fails for a given graph.
 *
 * Source: Introduction to Algorithms by Charles E. Leiserson, Clifford Stein, Ronald Rivest, and Thomas H. Cormen
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    /**
     * generic solve in case there is a need to expand to different solving methods
     *
     * @param maze
     * @param numLives
     * @return
     */
    public String solve(Maze maze, int numLives) {
        return solvebyBreadthFisrtSearch(maze, numLives);
    }

    /**
     * traverses the maze in breadth first order to try to find the shortest distance from the starting cell to the
     * ending cell. Stops once it runs out of cells to process.
     *
     * @param maze
     * @param numLives
     * @return
     */
    private String solvebyBreadthFisrtSearch(Maze maze, int numLives) {
        // need to get a way to keep track of shortest path
        // create a visited structure to keep track of visited cells
        Boolean[][] visited = new Boolean[maze.height][maze.width];
        for (int h = 0; h < maze.height; h++) {
            Arrays.fill(visited[h], Boolean.FALSE);
        }
        // keep track of the distance from starting cell to all other cells
        int[][] distance = new int[maze.height][maze.width];
        // create a queue
        Queue<Cell> q = new LinkedList<>();
        // add the starging cell to the queue
        q.add(maze.startCell);
        // mark cell as visited
        visited[maze.startCell.h][maze.startCell.w] = true;
        // start looking for the shortest path
        while(!q.isEmpty()) {
            // get the top element in the queue
            Cell vistingCell = q.remove();
            int currentDist = distance[vistingCell.h][vistingCell.w];
            if (vistingCell.features.containsKey("END")) {
                break;
            }

            // look at connected neighbors
            if (vistingCell.features.containsKey("UP")) {
                visit(maze, q, visited, distance, vistingCell.h - 1, vistingCell.w, currentDist);
            }

            if (vistingCell.features.containsKey("LEFT")) {
                visit(maze, q, visited, distance, vistingCell.h, vistingCell.w - 1, currentDist);
            }

            if (vistingCell.features.containsKey("DOWN")) {
                visit(maze, q, visited, distance, vistingCell.h + 1, vistingCell.w, currentDist);
            }

            if (vistingCell.features.containsKey("RIGHT")) {
                visit(maze, q, visited, distance, vistingCell.h, vistingCell.w + 1, currentDist);
            }
        }

        return walkBack(maze, distance, numLives);
    }

    /**
     * a helper function to process visited cells
     *
     * @param maze
     * @param q
     * @param visited
     * @param distance
     * @param h
     * @param w
     * @param currentDist
     */
    private void visit(Maze maze, Queue<Cell> q, Boolean[][] visited, int[][] distance, int h, int w, int currentDist) {
        if (!visited[h][w]) {
            Cell next = maze.structure[h][w];
            q.add(next);
            visited[h][w] = true;
            distance[h][w] = currentDist + 1;
        }
    }

    /**
     * goes from ending cell to starting cell, while there are more than 1 lives it just keeps stepping on mines,
     * once it reaches 1 life, it looks for alternate, unvisited paths, if none exist, it fails. If it reaches the
     * starting cell it prints and returns the directions to get from the starting to the ending cell.
     *
     * @param maze
     * @param distance
     * @param numLives
     * @return
     */
    private String walkBack(Maze maze, int[][] distance, int numLives) {
        List<String> shortestPath = new ArrayList<>();
        // in case of not enough lives
        Boolean[][] visited = new Boolean[maze.height][maze.width];
        for (int h = 0; h < maze.height; h++) {
            Arrays.fill(visited[h], Boolean.FALSE);
        }
        // get the distance from the end
        int currDist = distance[maze.endCell.h][maze.endCell.w];
        Cell currentCell = maze.endCell;
        visited[maze.endCell.h][maze.endCell.w] = true;
        boolean mineFlag = false;
        int altPathCount = 0;
        while(currDist != 0) {

            if (numLives > 1) {
                // check each direction for an opening and the smaller distance
                if (currentCell.features.containsKey("UP")) {
                    if (distance[currentCell.h - 1][currentCell.w] < currDist) {
                        shortestPath.add("DOWN");
                        currDist = distance[currentCell.h - 1][currentCell.w];
                        currentCell = maze.structure[currentCell.h - 1][currentCell.w];
                        visited[currentCell.h][currentCell.w] = true;
                        if (currentCell.features.containsKey("MINE")) {
                            numLives = numLives - 1;
                        }
                    }
                }

                if (currentCell.features.containsKey("LEFT")) {
                    if (distance[currentCell.h][currentCell.w - 1] < currDist) {
                        shortestPath.add("RIGHT");
                        currDist = distance[currentCell.h][currentCell.w - 1];
                        currentCell = maze.structure[currentCell.h][currentCell.w - 1];
                        visited[currentCell.h][currentCell.w] = true;
                        if (currentCell.features.containsKey("MINE")) {
                            numLives = numLives - 1;
                        }
                    }
                }

                if (currentCell.features.containsKey("DOWN")) {
                    if (distance[currentCell.h + 1][currentCell.w] < currDist) {
                        shortestPath.add("UP");
                        currDist = distance[currentCell.h + 1][currentCell.w];
                        currentCell = maze.structure[currentCell.h + 1][currentCell.w];
                        visited[currentCell.h][currentCell.w] = true;
                        if (currentCell.features.containsKey("MINE")) {
                            numLives = numLives - 1;
                        }
                    }
                }

                if (currentCell.features.containsKey("RIGHT")) {
                    if (distance[currentCell.h][currentCell.w + 1] < currDist) {
                        shortestPath.add("LEFT");
                        currDist = distance[currentCell.h][currentCell.w + 1];
                        currentCell = maze.structure[currentCell.h][currentCell.w + 1];
                        visited[currentCell.h][currentCell.w] = true;
                        if (currentCell.features.containsKey("MINE")) {
                            numLives = numLives - 1;
                        }
                    }
                }
            } else {
                // we are down to one life, have to look for alternate paths
                if (currentCell.features.containsKey("UP")) {
                    if (mineFlag) {
                        if (!visited[currentCell.h - 1][currentCell.w] && !maze.structure[currentCell.h - 1][currentCell.w].features.containsKey("MINE")) {
                            shortestPath.add("DOWN");
                            currDist = distance[currentCell.h - 1][currentCell.w];
                            currentCell = maze.structure[currentCell.h - 1][currentCell.w];
                            visited[currentCell.h][currentCell.w] = true;
                            mineFlag = false;
                            altPathCount = 0;
                        } else {
                            altPathCount++;
                        }
                    } else {
                        if (!visited[currentCell.h - 1][currentCell.w]) {
                            if (!maze.structure[currentCell.h - 1][currentCell.w].features.containsKey("MINE")) {
                                if (distance[currentCell.h - 1][currentCell.w] < currDist) {
                                    shortestPath.add("DOWN");
                                    currDist = distance[currentCell.h - 1][currentCell.w];
                                    currentCell = maze.structure[currentCell.h - 1][currentCell.w];
                                    visited[currentCell.h][currentCell.w] = true;
                                }
                            } else {
                                mineFlag = true;
                            }
                        }
                    }
                }

                if (currentCell.features.containsKey("LEFT")) {
                    if (mineFlag) {
                        if (!visited[currentCell.h][currentCell.w - 1] && !maze.structure[currentCell.h][currentCell.w - 1].features.containsKey("MINE")) {
                            shortestPath.add("RIGHT");
                            currDist = distance[currentCell.h][currentCell.w - 1];
                            currentCell = maze.structure[currentCell.h][currentCell.w - 1];
                            visited[currentCell.h][currentCell.w] = true;
                            mineFlag = false;
                            altPathCount = 0;
                        } else {
                            altPathCount++;
                        }
                    } else {
                        if (!visited[currentCell.h][currentCell.w - 1]) {
                            if (!maze.structure[currentCell.h][currentCell.w - 1].features.containsKey("MINE")) {
                                if (distance[currentCell.h][currentCell.w - 1] < currDist) {
                                    shortestPath.add("RIGHT");
                                    currDist = distance[currentCell.h][currentCell.w - 1];
                                    currentCell = maze.structure[currentCell.h][currentCell.w - 1];
                                    visited[currentCell.h][currentCell.w] = true;
                                }
                            } else {
                                mineFlag = true;
                            }
                        }
                    }
                }

                if (currentCell.features.containsKey("DOWN")) {
                    if (mineFlag) {
                        if (!visited[currentCell.h + 1][currentCell.w] && !maze.structure[currentCell.h + 1][currentCell.w].features.containsKey("MINE")) {
                            shortestPath.add("UP");
                            currDist = distance[currentCell.h + 1][currentCell.w];
                            currentCell = maze.structure[currentCell.h + 1][currentCell.w];
                            visited[currentCell.h][currentCell.w] = true;
                            mineFlag = false;
                            altPathCount = 0;
                        } else {
                            altPathCount++;
                        }
                    } else {
                        if (!visited[currentCell.h + 1][currentCell.w]) {
                            if (!maze.structure[currentCell.h + 1][currentCell.w].features.containsKey("MINE")) {
                                if (distance[currentCell.h + 1][currentCell.w] < currDist) {
                                    shortestPath.add("UP");
                                    currDist = distance[currentCell.h + 1][currentCell.w];
                                    currentCell = maze.structure[currentCell.h + 1][currentCell.w];
                                    visited[currentCell.h][currentCell.w] = true;
                                }
                            } else {
                                mineFlag = true;
                            }
                        }
                    }
                }

                if (currentCell.features.containsKey("RIGHT")) {
                    if (mineFlag) {
                        if (!visited[currentCell.h][currentCell.w + 1] && !maze.structure[currentCell.h][currentCell.w + 1].features.containsKey("MINE")) {
                            shortestPath.add("LEFT");
                            currDist = distance[currentCell.h][currentCell.w + 1];
                            currentCell = maze.structure[currentCell.h][currentCell.w + 1];
                            visited[currentCell.h][currentCell.w] = true;
                            mineFlag = false;
                            altPathCount = 0;
                        } else {
                            altPathCount++;
                        }
                    } else {
                        if (!visited[currentCell.h][currentCell.w + 1]) {
                            if (!maze.structure[currentCell.h][currentCell.w + 1].features.containsKey("MINE")) {
                                if (distance[currentCell.h][currentCell.w + 1] < currDist) {
                                    shortestPath.add("LEFT");
                                    currDist = distance[currentCell.h][currentCell.w + 1];
                                    currentCell = maze.structure[currentCell.h][currentCell.w + 1];
                                    visited[currentCell.h][currentCell.w] = true;
                                }
                            } else {
                                mineFlag = true;
                            }
                        }
                    }
                }

                // one of the neigbors started the counter and no alternative path was found.
                if (altPathCount > 3) {
                    System.out.println("no path found");
                    return "no path found";
                }

            }
        }

        return stringifyPath(shortestPath);
    }

    /**
     * helper function to print the instruction in the required format
     *
     * @param shortestPath
     * @return
     */
    private String stringifyPath(List<String> shortestPath) {
        String path = "[";
        if (!shortestPath.isEmpty()) {
            for (int i = shortestPath.size() - 1; i > 0; i--) {
                path += "'" + shortestPath.get(i) + "', ";
            }
            path += "'" + shortestPath.get(0) + "']";
        } else {
            path += "]";
        }

        System.out.println(path);
        System.out.println();

        return path;
    }
}
