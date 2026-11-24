import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;

public class MatrixGraph {

    int[][] adjMat;

    public MatrixGraph(int order) {
        adjMat = new int[order][order];
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                adjMat[i][j] = 0;
            }
        }
    }

    /*Read the graph from the file*/
    public static MatrixGraph read(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));

        int n = Integer.parseInt(br.readLine());

        MatrixGraph adjMat = new MatrixGraph(n);
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            if (line.equals("")) {
                continue;
            }
            String[] edges = line.split(" ");
            for (String edge : edges) {
                adjMat.adjMat[i][Integer.parseInt(edge)] = 1;
            }
        }
        return adjMat;
    }


}
