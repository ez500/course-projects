import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 *
 * Solves a maze by BFS.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 12, 2025
 *
 */

public class MazeSolver {
    private Maze maze;

    public MazeSolver() {
        this.maze = null;
    }

    private int[][] toArrayOfCoordinates(List<List<Integer>> coordList) {
        return coordList.stream()
                .map(coord -> new int[]{coord.getFirst(), coord.get(1)})
                .toArray(int[][]::new);
    }

    private List<Integer> coordinateAsList(int[] coord) {
        return new ArrayList<>(Arrays.asList(coord[0], coord[1]));
    }

    private boolean isPath(List<Integer> coord) {
        return maze.getGrid()[coord.get(0)][coord.get(1)] == 'P';
    }

    private List<List<List<Integer>>> getCardinalNeighborPaths(List<List<Integer>> current) {
        return Stream.of(List.of(1, 0), List.of(-1, 0), List.of(0, 1), List.of(0, -1))
                .map(direction -> {
                    List<Integer> neighbor = new ArrayList<>(Arrays.asList(
                            current.getLast().get(0) + direction.get(0),
                            current.getLast().get(1) + direction.get(1)
                    ));
                    if (neighbor.get(0) >= 0 && neighbor.get(0) < maze.getGrid().length &&
                            neighbor.get(1) >= 0 && neighbor.get(1) < maze.getGrid()[0].length &&
                            isPath(neighbor)) {
                        List<List<Integer>> newPath = new ArrayList<>(current);
                        newPath.add(neighbor);
                        return newPath;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private int[][] findPath() {
        List<List<Integer>> visited = new ArrayList<>();
        List<List<List<Integer>>> frontierPaths = new ArrayList<>();
        // base case: start
        frontierPaths.add(new ArrayList<>(List.of(this.coordinateAsList(maze.getStart()))));
        frontierPaths.getFirst().getFirst().add(0); // depth

        while (!frontierPaths.isEmpty()) {
            List<List<Integer>> currentPath = frontierPaths.removeFirst();
            List<Integer> currentCoords = currentPath.getLast();
            int depth = currentCoords.removeLast();
            if (visited.contains(currentCoords)) {
                continue;
            }
            visited.add(currentCoords);
            if (currentCoords.equals(this.coordinateAsList(maze.getEnd()))) {
                return this.toArrayOfCoordinates(currentPath);
            }

            frontierPaths.addAll((this.getCardinalNeighborPaths(currentPath).stream().peek(
                    toNeighborPath -> toNeighborPath.getLast().add(depth + 1) // depth
            )).toList());
        }

        return null;
    }

    public void readMaze(String fileName) throws InvalidMazeException, IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (lines.size() < 3) {
            throw new InvalidMazeException("Invalid maze format");
        }
        String name = lines.removeFirst();
        if (name.isEmpty()) {
            throw new InvalidMazeException("Invalid maze format");
        }

        String coordFormat = "^\\d+-\\d+$";

        String start = lines.removeFirst();
        String end = lines.removeFirst();
        if (!start.contains("Start:") || !end.contains("End:")) {
            throw new InvalidMazeException("Invalid maze format");
        }

        start = start.substring(start.indexOf(" ") + 1);
        end = end.substring(end.indexOf(" ") + 1);
        if (!start.matches(coordFormat) || !end.matches(coordFormat)) {
            throw new InvalidMazeException("Invalid maze format");
        }

        int[] startCoords = Arrays.stream(start.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] endCoords = Arrays.stream(end.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        if (
                startCoords[0] < 0 || startCoords[1] < 0 || endCoords[0] < 0 || endCoords[1] < 0 ||
                lines.stream().anyMatch(line -> Arrays.stream(line.split(","))
                        .anyMatch(cell -> (!cell.equals("W") && !cell.equals("P")))) ||
                lines.stream().mapToInt(line -> String.join("", line.split(",")).length())
                        .distinct().toArray().length != 1 ||
                lines.size() <= startCoords[0] || lines.getFirst().split(",").length <= startCoords[1] ||
                lines.size() <= endCoords[0] || lines.getFirst().split(",").length <= endCoords[1]
        ) {
            throw new InvalidMazeException("Invalid maze format");
        }

        char[][] grid = lines.stream().map(
                line -> String.join("", line.split(",")).toCharArray()
        ).toArray(char[][]::new);

        this.maze = new Maze(name, grid, startCoords, endCoords);
    }

    public void solveMaze() {
        this.maze.setPath(this.findPath());
    }

    public void writeSolution(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(this.maze.pathString());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InvalidMazeException, IOException {
        MazeSolver solver = new MazeSolver();

        solver.readMaze("StarterMaze.txt");
        solver.solveMaze();
    }
}
