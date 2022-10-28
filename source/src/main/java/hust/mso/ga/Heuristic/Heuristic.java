package hust.mso.ga.Heuristic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import hust.mso.ga.Graph;
import hust.mso.ga.Parameter;
import hust.mso.ga.Task;

public class Heuristic {
    // @return: min depth random spanning tree (an edge list)
    public static int[] random() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        final int[] depth = new int[G.V];
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        for (int i = 0; i < G.V; i++) {
            parents.add(new ArrayList<Integer>());
            depth[i] = Parameter.INF;
        }

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        depth[0] = 0;
        parent[0] = -1;

        int[] aggr = new int[G.V];
        for (int i = 0; i < G.V; i++) {
            aggr[i] = G.vertices.get(i).report_size;
            parents.add(new ArrayList<Integer>());
        }

        PriorityQueue<Integer> Q = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer u, Integer v) {
                if (depth[u] == depth[v]) {
                    return G.vertices.get(v).report_size - G.vertices.get(u).report_size;
                } else
                    return depth[u] - depth[v];
            }
        });
        Q.add(0);

        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v] && !(depth[u] + 1 > depth[v])) {
                    if (depth[u] + 1 < depth[v]) {
                        parents.get(v).clear();
                        parents.get(v).add(u);
                        depth[v] = depth[u] + 1;
                    } else { // equal parents.get(v).add(u);
                        parents.get(v).add(u);
                    }

                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0) {
                int parent_counter = parents.get(u).size();
                parent[u] = parents.get(u).get(Parameter.rand.nextInt(parent_counter));
                // Collections.shuffle(parents.get(u), Parameter.rand);
                // parent[u] = parents.get(u).get(0);
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    public static int[] balance_load() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        Arrays.fill(parent, -1);
        final int[] depth = new int[G.V];
        Arrays.fill(depth, Parameter.INF);
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        int[] aggr = new int[G.V];
        for (int i = 0; i < G.V; i++) {
            aggr[i] = G.vertices.get(i).report_size;
            parents.add(new ArrayList<Integer>());
        }

        depth[0] = 0;

        // Queue<Integer> Q = new LinkedList<>();

        PriorityQueue<Integer> Q = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer u, Integer v) {
                if (depth[u] == depth[v]) {
                    return G.vertices.get(v).report_size - G.vertices.get(u).report_size;
                } else
                    return depth[u] - depth[v];
            }
        });

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        Q.add(0);
        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v] && !(depth[u] + 1 > depth[v])) {
                    if (depth[u] + 1 < depth[v]) {
                        parents.get(v).clear();
                        parents.get(v).add(u);
                        depth[v] = depth[u] + 1;
                    } else { // equal parents.get(v).add(u);
                        parents.get(v).add(u);
                    }

                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0) {
                int curr_parent = parents.get(u).get(0);
                int one_sub_root, other_sub_root;

                for (int i = 1; i < parents.get(u).size(); i++) {
                    one_sub_root =  curr_parent;
                    other_sub_root = parents.get(u).get(i);

                    while (parent[one_sub_root] != parent[other_sub_root]) {
                        one_sub_root = parent[one_sub_root];
                        other_sub_root = parent[other_sub_root];
                    }

                    if (aggr[one_sub_root] > aggr[other_sub_root]) {
                        curr_parent = parents.get(u).get(i);
                    }
                    // if (aggr[curr_parent]> aggr[other_sub_root]) {
                        // curr_parent = other_sub_root;
                    // }
                }

                parent[u] = curr_parent;
                while (curr_parent != 0) {
                    aggr[curr_parent] += aggr[u];
                    curr_parent = parent[curr_parent];
                }
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    public static int[] balance_energy_left() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        final int[] depth = new int[G.V];

        Arrays.fill(parent, -1);
        Arrays.fill(depth, Parameter.INF);

        final double[] energy_left = new double[G.V];
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();
        int[] aggr = new int[G.V];
        for (int i = 0; i < G.V; i++) {
            if (i != 0) {
                energy_left[i] = G.vertices.get(i).get_energy_left();
                aggr[i] = G.vertices.get(i).report_size;
            }

            parents.add(new ArrayList<Integer>());
        }

        PriorityQueue<Integer> Q = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer one, Integer other) {
                if (depth[one] == depth[other]) {
                    if (energy_left[one] == energy_left[other])
                        return 0;
                    else
                        return energy_left[one] < energy_left[other] ? 1 : -1;
                } else
                    return depth[one] - depth[other];
            }
        });

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        energy_left[0] = Parameter.INF;
        depth[0] = 0;
        Q.add(0);

        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;
            for (Integer v : G.adj.get(u)) {
                if (!visited[v] && !(depth[u] + 1 > depth[v])) {
                    if (depth[u] + 1 < depth[v]) {
                        parents.get(v).clear();
                        parents.get(v).add(u);
                        depth[v] = depth[u] + 1;
                    } else { // equal parents.get(v).add(u);
                        parents.get(v).add(u);
                    }

                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0 && parent[u] != 0) {
                int curr_parent = parents.get(u).get(0);
                int one_sub_root, other_sub_root;

                for (int i = 1; i < parents.get(u).size(); i++) {
                    one_sub_root =  curr_parent;
                    other_sub_root = parents.get(u).get(i);

                    while (parent[one_sub_root] != parent[other_sub_root]) {
                        one_sub_root = parent[one_sub_root];
                        other_sub_root = parent[other_sub_root];
                    }

                    if (energy_left[one_sub_root] < energy_left[other_sub_root]) {
                        curr_parent = parents.get(u).get(i);
                    }
                }

                parent[u] = curr_parent;
                int curr_node = u;
                int send_packets, recv_packets;
                while (curr_node != 0) {
                    aggr[curr_parent] += aggr[u];
                    send_packets = (int) Math.ceil((double) aggr[curr_parent] / Task.q);
                    recv_packets = (int) Math.ceil((double) aggr[curr_node] / Task.q);

                    energy_left[curr_parent] = G.vertices.get(curr_parent).init_energy - Task.Tx * send_packets
                            - Task.Rx * recv_packets;
                    curr_node = curr_parent;
                    curr_parent = parent[curr_parent];
                }
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    public static int[] redundant_packet_min() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        Arrays.fill(parent, -1);
        final int[] depth = new int[G.V];
        Arrays.fill(depth, Parameter.INF);
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        int[] aggr = new int[G.V];
        for (int i = 0; i < G.V; i++) {
            aggr[i] = G.vertices.get(i).report_size;
            parents.add(new ArrayList<Integer>());
        }

        depth[0] = 0;

        // Queue<Integer> Q = new LinkedList<>();
        PriorityQueue<Integer> Q = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer u, Integer v) {
                if (depth[u] == depth[v]) {
                    return G.vertices.get(v).report_size - G.vertices.get(u).report_size;
                } else {
                    return depth[u] - depth[v];
                }
            }
        });

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        Q.add(0);
        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v] && !(depth[u] + 1 > depth[v])) {
                    if (depth[u] + 1 < depth[v]) {
                        parents.get(v).clear();
                        parents.get(v).add(u);
                        depth[v] = depth[u] + 1;
                    } else { // equal parents.get(v).add(u);
                        parents.get(v).add(u);
                    }

                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0) {
                int min_aggr = Parameter.INF;
                int min_redudant = Parameter.INF;

                int parent_u = parents.get(u).get(0);
                int curr_p;
                int no_redundants; 
                int[] redundants = new int[parents.get(u).size()];
                int p;
                for (int i = 0; i < parents.get(u).size(); i++) {
                    p = parents.get(u).get(i);
                    curr_p = p;
                    no_redundants = 0;
                    while (curr_p != 0) {
                        if ((aggr[u] + aggr[curr_p]) % Task.q != 0)
                            no_redundants++;
                        curr_p = parent[curr_p];
                    }
                    redundants[i] = no_redundants;

                    if (min_redudant > no_redundants) {
                        min_aggr = aggr[p];
                        min_redudant = no_redundants;
                        parent_u = p;
                    } else {
                        if (min_redudant == no_redundants) {
                            if (min_aggr > aggr[p]) {
                                min_aggr = aggr[p];
                                parent_u = p;
                            }
                        }
                    }
                }

                ArrayList<Integer> parents_with_same_redundant = new ArrayList<>();
                for (int i = 0; i < redundants.length; i++) {
                    if (redundants[i] == min_redudant) {
                        parents_with_same_redundant.add(parents.get(u).get(i));
                    }
                }

                // parent[u] = parent_u;
                parent[u] = parents_with_same_redundant.get(Parameter.rand.nextInt(parents_with_same_redundant.size()));
                curr_p = parent[u];
                while (curr_p != 0) {
                    aggr[curr_p] += aggr[u];
                    curr_p = parent[curr_p];
                }
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    // @return a kruskal random spanning tree (an edge list)
    public static int[] kruskal_random() {
        Graph G = Task.G;
        int[] tree_edge_list = new int[G.V - 1];

        int[] root = new int[G.V];
        int[] depth = new int[G.V];

        for (int u = 0; u < G.V; u++) {
            Utils.make_set(root, depth, u);
        }

        int edge_label;
        int[] uv;
        int counter = 0;
        while (true) {
            edge_label = Parameter.rand.nextInt(G.E);
            uv = G.get_edge(edge_label);
            if (Utils.find(root, uv[0]) != Utils.find(root, uv[1])) {
                tree_edge_list[counter++] = edge_label;
                Utils.union(root, depth, uv[0], uv[1]);
                if (counter == G.V - 1)
                    break;
            }
        }

        return tree_edge_list;
    }

    public static int[] bfs_random() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        Arrays.fill(parent, -1);
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        for (int i = 0; i < G.V; i++) {
            parents.add(new ArrayList<Integer>());
        }

        Queue<Integer> Q = new LinkedList<>();

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        Q.add(0);
        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v]) {
                    parents.get(v).add(u);
                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0) {
                int parent_counter = parents.get(u).size();
                parent[u] = parents.get(u).get(Parameter.rand.nextInt(parent_counter));
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    public static int[] SPT() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        int[] depth = new int[G.V];
        Arrays.fill(parent, -1);
        Arrays.fill(depth, Parameter.INF);
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        for (int i = 0; i < G.V; i++) {
            parents.add(new ArrayList<Integer>());
        }

        Queue<Integer> Q = new LinkedList<>();

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        Q.add(0);
        depth[0] = 0;
        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v]) {
                    if (depth[v] > depth[u] + 1) {
                        depth[v] = depth[u] + 1;
                        parent[v] = u;
                    }
                    
                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }

    public static int[] ec() {
        final Graph G = Task.G;

        int[] parent = new int[G.V];
        Arrays.fill(parent, -1);
        final int[] depth = new int[G.V];
        Arrays.fill(depth, Parameter.INF);
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

        int[] aggr = new int[G.V];
        int[] no_recvs = new int[G.V];
        for (int i = 0; i < G.V; i++) {
            aggr[i] = G.vertices.get(i).report_size;
            parents.add(new ArrayList<Integer>());
            no_recvs[i] = 0;
        }

        depth[0] = 0;

        // Queue<Integer> Q = new LinkedList<>();
        PriorityQueue<Integer> Q = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer u, Integer v) {
                if (depth[u] == depth[v]) {
                    return G.vertices.get(v).report_size - G.vertices.get(u).report_size;
                } else {
                    return depth[u] - depth[v];
                }
            }
        });

        boolean[] visited = new boolean[G.V];
        boolean[] in_queue = new boolean[G.V];
        Arrays.fill(visited, false);
        Arrays.fill(in_queue, false);

        Q.add(0);
        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            visited[u] = true;

            for (Integer v : G.adj.get(u)) {
                if (!visited[v] && !(depth[u] + 1 > depth[v])) {
                    if (depth[u] + 1 < depth[v]) {
                        parents.get(v).clear();
                        parents.get(v).add(u);
                        depth[v] = depth[u] + 1;
                    } else { // equal parents.get(v).add(u);
                        parents.get(v).add(u);
                    }

                    if (!in_queue[v]) {
                        Q.add(v);
                        in_queue[v] = true;
                    }
                }
            }

            if (u != 0) {
                int curr_p, parent_u;
                int v = parents.get(u).get(0);
                int one_sub_root, other_sub_root, prev_one, prev_other;
                int ec_one, ec_other;
                for (int i = 1; i < parents.get(u).size(); i++) {
                    one_sub_root = v;
                    other_sub_root = parents.get(u).get(i);
                    prev_one = -1; prev_other = -1;
                    ec_one = 0; ec_other = 0;
                    while (parent[one_sub_root] != parent[other_sub_root]) {
                        if (prev_one != -1 && prev_other != -1) {
                            ec_one += (no_recvs[one_sub_root] - (int) Math.ceil(aggr[prev_one] / Task.q) +  (int) Math.ceil((aggr[prev_one] + aggr[u]) / Task.q)) * Task.Rx + ((int) Math.ceil((aggr[one_sub_root] + aggr[u]) / Task.q)) * Task.Tx;
                            ec_other += (no_recvs[other_sub_root] - (int) Math.ceil(aggr[prev_one] / Task.q) +  ((int) Math.ceil((aggr[prev_other] + aggr[u]) / Task.q))) * Task.Rx + ((int) Math.ceil((aggr[other_sub_root] + aggr[u]) / Task.q)) * Task.Tx;
                        } else {
                            ec_one += no_recvs[one_sub_root] * Task.Rx + ((int) Math.ceil((aggr[one_sub_root] + aggr[u]) / Task.q)) * Task.Tx;
                            ec_other += no_recvs[other_sub_root] * Task.Rx + ((int) Math.ceil((aggr[other_sub_root] + aggr[u]) / Task.q)) * Task.Tx;
                        }

                        if (parent[one_sub_root] == parent[other_sub_root]) {
                            ec_one += ((int) Math.ceil((aggr[one_sub_root] + aggr[u]) / Task.q) - (int) Math.ceil(aggr[one_sub_root] / Task.q)) * Task.Rx;
                            ec_other += ((int) Math.ceil((aggr[other_sub_root] + aggr[u]) / Task.q) - (int) Math.ceil(aggr[other_sub_root] / Task.q)) * Task.Rx;
                        }

                        prev_one = one_sub_root; prev_other = other_sub_root;
                        one_sub_root = parent[one_sub_root]; other_sub_root = parent[other_sub_root];
                    }

                    if (ec_one > ec_other) {
                        v = parents.get(u).get(i);
                    }
                }

                parent[u] = v;
                curr_p = parent[u];
                int prev_p = u;
                while (curr_p != 0) {
                    aggr[curr_p] += aggr[u];
                    no_recvs[curr_p] += ((int) Math.ceil(aggr[prev_p] / Task.q) - (int) Math.ceil((aggr[prev_p] - aggr[u]) / Task.q));
                    prev_p = curr_p;
                    curr_p = parent[curr_p];
                }
            }
        }

        int[] tree_edge_list = new int[G.V - 1];
        for (int i = 1; i < G.V; i++) {
            tree_edge_list[i - 1] = G.label[parent[i]][i];
        }

        return tree_edge_list;
    }
}
