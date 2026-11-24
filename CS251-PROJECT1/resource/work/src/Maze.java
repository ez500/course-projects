/**
 * CS 251: Data Structures and Algorithms
 * Project 1: Part 2
 *
 * @author Eugene Zhang
 * @username zhan4713
 * @sources none
 *
 *
 *
 */
public class Maze {

    private final char WALL = '#';
    private final char END = '%';
    private int numSpaces = 0;
    private final CyclicDoubleQueue<String> unvisited = new CyclicDoubleQueue<>();
    private CyclicDoubleQueue<String> path = new CyclicDoubleQueue<>();
    private boolean[][] visited;
    private int x = 0;
    private int y = 0;

    /**
     * Finds the path using CyclicDoubleQueue as a traditional stack
     * Returns the path and number of spaces checked as {@code String}.
     *
     * @param  map a {@code char[][]} provide.
     * @return the path and number of spaces checked as {@code String}
     */
    public String solveWithStack(char[][] map) {
        visited = new boolean[map.length][map[0].length];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) { visited[i][j] = false; }
        }
        unvisited.enqueueBack("(0,0)");
        while (!unvisited.isEmpty() && path != null) {
            try {
                String[] coordinates = unvisited.peekBack().split(",");
                x = Integer.parseInt(coordinates[0].substring(1));
                y = Integer.parseInt(coordinates[1].substring(0, coordinates[1].length() - 1));
                if (map[x][y] == END) {
                    return path.listAll() + " " + numSpaces;
                }
            } catch (Exception e) {
                System.out.println("(Unvisited is empty) " + e.getMessage());
                break;
            }
            path = moveOnce(map);
        }
        return "no way";
    }

    public CyclicDoubleQueue<String> moveOnce(char[][] map) {
        try {
            // update unvisited, visited, path, numSpaces
            unvisited.dequeueBack();
            path.enqueueBack("(" + x + "," + y + ")");
            visited[x][y] = true;
            numSpaces++;
            // detect dead end, erase paths until neighbor of the next unvisited path
            if (isDeadEnd(map, x, y)) {
                while (!hasUnvisitedNeighbor(map, x, y)) {
                    try {
                        path.dequeueBack();
                        String[] updatedCoordinates = path.peekBack().split(",");
                        x = Integer.parseInt(updatedCoordinates[0].substring(1));
                        y = Integer.parseInt(updatedCoordinates[1].substring(0, updatedCoordinates[1].length() - 1));
                    } catch (Exception e) {
                        System.out.println("(Path is empty) " + e.getMessage());
                        break;
                    }
                }
            }
            // check crystal
            if (checkCrystal(map, x, y)) { return path; }
            // check directions
            if (downPath(map, x, y)) { unvisited.enqueueBack("(" + (x + 1) + "," + y + ")"); }
            if (rightPath(map, x, y)) { unvisited.enqueueBack("(" + x + "," + (y + 1) + ")"); }
            if (upPath(map, x, y)) { unvisited.enqueueBack("(" + (x - 1) + "," + y + ")"); }
            if (leftPath(map, x, y)) { unvisited.enqueueBack("(" + x + "," + (y - 1) + ")"); }
            return path;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean hasUnvisitedNeighbor(char[][] map, int x, int y) {
        return downPath(map, x, y) || rightPath(map, x, y) || upPath(map, x, y) || leftPath(map, x, y);
    }
    public boolean isDeadEnd(char[][] map, int x, int y) {
        int deadEnds = 0;
        if (!downPath(map, x, y)) { deadEnds++; }
        if (!rightPath(map, x, y)) { deadEnds++; }
        if (!upPath(map, x, y)) { deadEnds++; }
        if (!leftPath(map, x, y)) { deadEnds++; }
        return deadEnds == 4;
    }
    public boolean checkCrystal(char[][] map, int x, int y) {
        if (downPath(map, x, y)) {
            if (map[x + 1][y] == END) {
                unvisited.enqueueBack("(" + (x + 1) + "," + y + ")");
                path.enqueueBack("(" + (x + 1) + "," + y + ")");
                return true;
            }
        } else if (rightPath(map, x, y)) {
            if (map[x][y + 1] == END) {
                unvisited.enqueueBack("(" + x + "," + (y + 1) + ")");
                path.enqueueBack("(" + x + "," + (y + 1) + ")");
                return true;
            }
        } else if (upPath(map, x, y)) {
            if (map[x - 1][y] == END) {
                unvisited.enqueueBack("(" + (x - 1) + "," + y + ")");
                path.enqueueBack("(" + (x - 1) + "," + y + ")");
                return true;
            }
        } else if (leftPath(map, x, y)) {
            if (map[x][y - 1] == END) {
                unvisited.enqueueBack("(" + x + "," + (y - 1) + ")");
                path.enqueueBack("(" + x + "," + (y - 1) + ")");
                return true;
            }
        }
    return false;
    }
    public boolean downPath(char[][] map, int x, int y) { return x != map.length - 1 && map[x + 1][y] != WALL && !visited[x + 1][y]; }
    public boolean rightPath(char[][] map, int x, int y) { return y != map[0].length - 1 && map[x][y + 1] != WALL && !visited[x][y + 1]; }
    public boolean upPath(char[][] map, int x, int y) { return x != 0 && map[x - 1][y] != WALL && !visited[x - 1][y]; }
    public boolean leftPath(char[][] map, int x, int y) { return y != 0 && map[x][y - 1] != WALL && !visited[x][y - 1]; }
}
