
/**
 *
 * A structure of a maze represented by a 2D array of characters.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 12, 2025
 *
 */

public class Maze {
    private final String name;
    private final char[][] grid;
    private final int[] start;
    private final int[] end;
    private int[][] path;

    public Maze(String name, char[][] grid, int[] start, int[] end) {
        this.name = name;
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.path = null;
    }

    public String getName() {
        return name;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int[] getStart() {
        return start;
    }

    public int[] getEnd() {
        return end;
    }

    public int[][] getPath() {
        return path;
    }

    public void setPath(int[][] path) {
        this.path = path;
    }

    public String pathString() {
        if (this.path == null) {
            return "No path found.";
        }
        StringBuilder pathStr = new StringBuilder("Start\n");
        for (int[] step : this.path) {
            pathStr.append(String.format("%d-%d%n", step[0], step[1]));
        }
        pathStr.append("End\n");
        return String.format("%s%nMoves: %d%n%s", this.name, this.path.length, pathStr);
    }

    public String toString() {
        String startEnd =
                String.format("Start: %d-%d%nEnd: %d-%d", this.start[0], this.start[1], this.end[0], this.end[1]);

        StringBuilder gridStr = new StringBuilder();
        for (char[] row : this.grid) {
            StringBuilder rowStr = new StringBuilder();
            for (char cell : row) {
                rowStr.append(cell).append(',');
            }
            rowStr.deleteCharAt(rowStr.length() - 1); // Remove trailing comma
            gridStr.append(rowStr).append("\n");
        }

        return String.format("%s%n%s%n%s", this.name, startEnd, gridStr);
    }
}
