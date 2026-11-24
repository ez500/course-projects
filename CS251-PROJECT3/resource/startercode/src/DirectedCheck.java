public class DirectedCheck {
    /*Compute Indegree of every vertex in the graph*/
    public static int[] computeInDegrees(ListGraph a) {
        int[] inDeg = new int[a.adjList.length];
        for (int i = 0; i < a.adjList.length; i++) {
            ListGraph.LinkedList.Node node = a.adjList[i].head;
            for (int j = 0; j < a.adjList[i].length(); j++) {
                node = (j == 0) ? node : node.next;
                inDeg[node.data]++;
            }
        }
        return inDeg;
    }

    /*DAG Check - Determine if a given graph is a DAG*/
    public static int[] dagCheck(ListGraph a) {
        int[] inDeg = computeInDegrees(a);
        int[] sourceSink = new int[2];
        boolean[] visited = new boolean[a.adjList.length];
        boolean[] recStack = new boolean[a.adjList.length];
        for (int i = 0; i < inDeg.length; i++) {
            if (inDeg[i] == 0) {
                if (isCyclic(i, visited, recStack, a)) {
                    return new int[]{-1, -1};
                }
                sourceSink[0]++;
            }
        }
        for (int i = 0; i < a.adjList.length; i++) {
            if (a.adjList[i].length() == 0) {
                sourceSink[1]++;
            }
        }
        if (sourceSink[0] == 0 || sourceSink[1] == 0) {
            return new int[]{-1, -1};
        }
        return sourceSink;
    }

    public static boolean isCyclic(int value, boolean[] visited, boolean[] recStack, ListGraph graph) {
        if (recStack[value]) {
            return true;
        }
        if (visited[value]) {
            return false;
        }
        visited[value] = true;
        recStack[value] = true;
        ListGraph.LinkedList.Node checkNode = graph.adjList[value].head;
        for (int i = 0; i < graph.adjList[value].length(); i++) {
            checkNode = (i == 0) ? checkNode : checkNode.next;
            if (isCyclic(checkNode.data, visited, recStack, graph)) {
                return true;
            }
        }
        recStack[value] = false;
        return false;
    }
}
