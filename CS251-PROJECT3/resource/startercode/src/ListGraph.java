import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;

public class ListGraph {

    LinkedList[] adjList;

    public ListGraph(int n) {
        adjList = new LinkedList[n];
    }

    public static class LinkedList {
        Node head;

        public static class Node {
            int data;
            int weight;
            Node next;

            Node (int d, int w) {
                this.data = d;
                this.weight = w;
                this.next = null;
            }
        }

        public void insert(int data, int weight) {
            Node newNode = new Node(data, weight);
            if (this.head == null) {
                this.head = newNode;
                return;
            }
            Node last = this.head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
        }

        public void insert(int data) {
            insert(data, 1);
        }

        public int length() {
            if (this.head == null) {
                return 0;
            }
            int length = 1;
            Node last = this.head;
            while (last.next != null) {
                length++;
                last = last.next;
            }
            return length;
        }
    }

    /*Read a non-weighted graph*/
    public static ListGraph read(String filepath) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
        int n = Integer.parseInt(br.readLine());
        ListGraph adjList = new ListGraph(n);
        for (int i = 0; i < n; i++) {
            LinkedList vertexEdgeList = new LinkedList();
            String line = br.readLine();
            if (line.equals("")) {
                adjList.adjList[i] = vertexEdgeList;
                continue;
            }
            String[] nums = line.split(" ");
            for (String num : nums) {
                vertexEdgeList.insert(Integer.parseInt(num));
            }
            adjList.adjList[i] = vertexEdgeList;
        }
        return adjList;
    }


    /*Read a weighted graph*/
    public static ListGraph readWeighted(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
        int n = Integer.parseInt(br.readLine());
        ListGraph adjList = new ListGraph(n);
        String[] weights = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            LinkedList vertexEdgeList = new LinkedList();
            String line = br.readLine();
            if (line.equals("")) {
                adjList.adjList[i] = vertexEdgeList;
                continue;
            }
            String[] nums = line.split(" ");
            for (String num : nums) {
                vertexEdgeList.insert(Integer.parseInt(num), Integer.parseInt(weights[Integer.parseInt(num)]));
            }
            adjList.adjList[i] = vertexEdgeList;
        }
        return adjList;
    }
}


