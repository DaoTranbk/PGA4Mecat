package hust.mso.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {
    public int V, E; // number of vertices, number of edges
    public int[][] label; // [u][v]: label of edge u-v
    public int[][] edge; // [i]: edge(from, to) has label which is i
    public ArrayList<Node> vertices;
    public ArrayList<ArrayList<Integer>> adj;

    public Graph(int V) {
        this.V = V;
        this.vertices = new ArrayList<Node>(V);
        init_adj_matrix();
    }

    public boolean add_edge(int u, int v, int id) {
        if (u > V || v > V) {
            System.err.println("Fail when adding edge (" + u + ", " + v + ")");
            return false;
        }

        if (!contain_edge(u, v)) {
            this.adj.get(u).add(v);
            this.adj.get(v).add(u);

            this.label[u][v] = id;
            this.label[v][u] = id;

            this.edge[id][0] = u;
            this.edge[id][1] = v;

            return true;
        }

        return false;
    }

    public boolean contain_edge(int u, int v) {
        return (this.label[u][v] != -1) || (this.label[v][u] != -1);
    }

    public void add_vertex(Node vertex) {
        this.vertices.add(vertex.id, vertex);
    }

    public int[] get_report_sizes() {
        int[] report_sizes = new int[V];
        for (Node u: vertices) {
            report_sizes[u.id] = u.report_size;
        }

        return report_sizes;
    }

    public void init_edge_mapping(int E) {
        this.E = E;
        this.edge = new int[E][2];
    }

    public int[] get_edge(int label) {
        return edge[label];
    }

    public void init_adj_matrix() {
        this.adj = new ArrayList<>();
        this.label = new int[V][V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(this.label[i], -1);
            this.adj.add(new ArrayList<Integer>());
        }
    }

    public void show_adj_mtx() {
        for (int i = 0; i < V; i++) {
            System.out.println(i + ": " + adj.get(i));
        }
    }

    // return the cut-off after removing an edge from tree
    public static ArrayList<Integer> cutoff_from_tree(Node cut_node, ArrayList<ArrayList<Integer>> children) {
        Graph G = Task.G;
        boolean[] visited = new boolean[G.V];
        Arrays.fill(visited, false);

        ArrayList<Integer> cut_off = new ArrayList<>();
        ArrayList<Integer> nodes_in_subtree = new ArrayList<>();
        Queue<Integer> Q = new LinkedList<>();
        Q.add(cut_node.id);

        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            nodes_in_subtree.add(u);
            visited[u] = true;
            for (Integer v: children.get(u)) Q.add(v);
        }

        for (Integer v: nodes_in_subtree) {
            for (Integer x: G.adj.get(v)) {
                if (v == cut_node.id && x == cut_node.parent) continue;
                if (!visited[x]) cut_off.add(G.label[v][x]);
            }
        }

        return cut_off;
    }

    @Override
    public String toString() {
        String info = String.format("V: %d\n", V);

        for (int i = 0; i < V; i++) {
            info += i + ": " + adj.get(i) + "\n";
        }

        return info;
    }
}
