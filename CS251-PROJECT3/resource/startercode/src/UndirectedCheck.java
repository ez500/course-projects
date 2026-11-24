public class UndirectedCheck {
    /*Check whether the given undirected graph is a tree*/
    public static boolean treeCheck(ListGraph a) {
        boolean[] visited = new boolean[a.adjList.length];
        boolean isCyclic = isCyclic(0, -1, visited, a);
        for (boolean b : visited) {
            if (!b) {
                return false;
            }
        }
        return !isCyclic;
    }

    public static boolean isCyclic(int v, int parent, boolean[] visited, ListGraph graph) {
        visited[v] = true;
        ListGraph.LinkedList.Node node = graph.adjList[v].head;
        for (int i = 0; i < graph.adjList[v].length(); i++) {
            if (i != 0) {
                node = node.next;
            }
            int edge = node.data;
            if (!visited[edge]) {
                if (isCyclic(edge, v, visited, graph)) {
                    return true;
                }
            } else if (edge != parent) {
                return true;
            }
        }
        return false;
    }

    /*Count the distinct number of triangles for an undirected graph using adjacency matrix representation*/
    public static int countTriangles(MatrixGraph a) {
        int n = 0;
        for (int i = 0; i < a.adjMat.length; i++) {
            for (int j = i; j < a.adjMat.length; j++) {
                if (a.adjMat[i][j] == 1) {
                    for (int k = j; k < a.adjMat.length; k++) {
                        if (k != i && k != j) {
                            if (a.adjMat[i][k] == 1 && a.adjMat[j][k] == 1) {
                                n++;
                            }
                        }
                    }
                }
            }
        }
        return n;
    }

    /*Clustering coefficient of the given vertex u*/
    public static double vertexClusterCoeff(ListGraph a, int u) {
        int deg = a.adjList[u].length();
        if (deg == 0 || deg == 1) {
            return 0.0;
        }
        int n = 0;
        boolean[] adjMat = new boolean[a.adjList.length];
        ListGraph.LinkedList.Node directNeighbor = a.adjList[u].head;
        for (int i = 0; i < deg; i++) {
            directNeighbor = (i == 0) ? directNeighbor : directNeighbor.next;
            for (int j = 0; j < a.adjList.length; j++) {
                adjMat[j] = false;
            }
            ListGraph.LinkedList.Node neighborNeighbor = a.adjList[directNeighbor.data].head;
            for (int j = 0; j < a.adjList[directNeighbor.data].length(); j++) {
                neighborNeighbor = (j == 0) ? neighborNeighbor : neighborNeighbor.next;
                adjMat[neighborNeighbor.data] = true;
            }
            ListGraph.LinkedList.Node nextNeighbors = directNeighbor.next;
            for (int j = i + 1; j < deg; j++) {
                nextNeighbors = (j == i + 1) ? nextNeighbors : nextNeighbors.next;
                if (adjMat[nextNeighbors.data]) {
                    n++;
                }
            }
        }
        return 2.0 * n / (deg * (deg - 1));
    }


    /*Clustering coefficient of the entire graph*/
    public static double graphClusterCoeff(ListGraph a) {
        double sumCoeff = 0;
        for (int i = 0; i < a.adjList.length; i++) {
            sumCoeff += vertexClusterCoeff(a, i);
        }
        return sumCoeff / a.adjList.length;
    }
}
