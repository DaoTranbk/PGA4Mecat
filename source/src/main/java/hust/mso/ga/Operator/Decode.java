package hust.mso.ga.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import hust.mso.ga.Graph;
import hust.mso.ga.Node;
import hust.mso.ga.Parameter;
import hust.mso.ga.Task;
import hust.mso.ga.Heuristic.Utils;

public class Decode {
    // the decoding method based on Prim's AL
    public static ArrayList<Node> create_data_aggregation_tree(ArrayList<Double> weight) {
        ArrayList<Node> da_tree = create_spanning_tree(weight);
        calculate_send_and_recv_packets(da_tree);
        return da_tree;
    }

    public static ArrayList<Node> create_spanning_tree(ArrayList<Double> weight) {
        Graph G = Task.G;
        ArrayList<Node> da_tree = new ArrayList<>(G.V);

        for (Node node : G.vertices) {
            node.key = -Parameter.INF;
            node.parent = -1;
            da_tree.add(node.id, new Node(node));
        }

        boolean[] visited = new boolean[G.V];
        Arrays.fill(visited, false);
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(in_queue, false);
        da_tree.get(0).key = Parameter.INF;

        PriorityQueue<Node> Q = new PriorityQueue<>();
        Q.add(da_tree.get(0));
        in_queue[0] = true;

        while (!Q.isEmpty()) {
            Node u = Q.poll();
            visited[u.id] = true;

            for (Integer v_id : G.adj.get(u.id)) {
                Node v = da_tree.get(v_id);
                int uv_id = G.label[u.id][v.id];
                if (!visited[v.id] && weight.get(uv_id) > v.key) {
                    if (in_queue[v.id])
                        Q.remove(v); // O(log(n))

                    v.parent = u.id;
                    v.key = weight.get(uv_id);
                    Q.add(v); // O(log(n))
                    in_queue[v.id] = true;
                }
            }
        }

        return da_tree;
    }

    public static void calculate_send_and_recv_packets(ArrayList<Node> da_tree) {
        Graph G = Task.G;
        ArrayList<ArrayList<Integer>> children = new ArrayList<>(G.V);
        for (int i = 0; i < G.V; i++) {
            children.add(new ArrayList<Integer>());
        }

        for (Node u : da_tree) {
            if (u.parent != -1)
                children.get(u.parent).add(u.id);
        }

        // calculate aggregation report size
        Stack<Integer> S = new Stack<>();
        S.push(0);
        int u;
        boolean[] visited = new boolean[G.V];
        Arrays.fill(visited, false);

        while (true) {
            u = S.peek();

            if (children.get(u).isEmpty()) {
                // if u is a leaf
                S.pop();
                visited[u] = true;
                Node u_node = da_tree.get(u);
                da_tree.get(u_node.parent).recv_report_size(u_node.aggregation_report_size);
            } else {
                boolean all_child_visited = true;
                for (Integer v : children.get(u)) {
                    if (!visited[v]) {
                        all_child_visited = false;
                        S.push(v);
                    }
                }

                if (all_child_visited) {
                    // if u is an internal node but all its descendants was visited
                    S.pop();
                    visited[u] = true;
                    Node u_node = da_tree.get(u);
                    if (u_node.parent != -1) { // if u_node is not the sink node
                        da_tree.get(u_node.parent).recv_report_size(u_node.aggregation_report_size);
                    }
                }
            }

            if (S.isEmpty())
                break;
        }
    }

    // the decoding method based on Kruskal's AL
    public static ArrayList<Node> kruskal(final ArrayList<Double> weight) {
        Graph G = Task.G;
        int[] tree_edge_list = new int[G.V - 1];

        ArrayList<Integer> sorted_edge_list = new ArrayList<>();
        for (int i = 0; i < G.E; i++)
            sorted_edge_list.add(i);
        Collections.sort(sorted_edge_list, new Comparator<Integer>() {
            @Override
            public int compare(Integer one, Integer other) {
                if (weight.get(one) == weight.get(other))
                    return 0;
                else
                    return weight.get(one) < weight.get(other) ? 1 : -1;
            }
        });

        int[] root = new int[G.V];
        int[] rank = new int[G.V];

        for (int u = 0; u < G.V; u++) {
            Utils.make_set(root, rank, u);
        }

        int edge_label;
        int[] uv;
        int counter = 0;
        for (int i = 0; i < G.E; i++) {
            edge_label = sorted_edge_list.get(i);
            uv = G.get_edge(edge_label);

            if (Utils.find(root, uv[0]) != Utils.find(root, uv[1])) {
                Utils.union(root, rank, uv[0], uv[1]);
                tree_edge_list[counter++] = edge_label;
            }
        }

        ArrayList<Node> da_tree = BFS(tree_edge_list);
        calculate_send_and_recv_packets(da_tree);

        return da_tree;
    }

    public static ArrayList<Node> BFS(int[] tree_edge_list) {
        ArrayList<Node> tree = new ArrayList<>();
        Graph G = Task.G;

        for (Node node : G.vertices) {
            node.key = 1;
            node.parent = -1;
            tree.add(node.id, new Node(node));
        }

        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < G.V; i++)
            adj.add(new ArrayList<Integer>());

        int[] uv;
        for (int edge_label : tree_edge_list) {
            uv = G.get_edge(edge_label);
            adj.get(uv[0]).add(uv[1]);
            adj.get(uv[1]).add(uv[0]);
        }

        boolean[] visited = new boolean[G.V];
        Arrays.fill(visited, false);

        Queue<Node> Q = new LinkedList<>();
        Q.add(tree.get(0));

        Node u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u.id] = true;

            for (Integer v_id : adj.get(u.id)) {
                Node v = tree.get(v_id);
                if (!visited[v.id]) {
                    v.parent = u.id;
                    Q.add(v);
                }
            }
        }

        return tree;
    }
}
