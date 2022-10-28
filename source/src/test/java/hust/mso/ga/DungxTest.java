package hust.mso.ga;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import hust.mso.ga.Node;
import hust.mso.ga.Operator.Decode;

public class DungxTest {
    protected double expected;
    protected int best_node;
    protected String task = "mecmin";
    ArrayList<Node> da_tree;
    protected String BASE_PATH = "./cache";

    @Before
    public void init() {
        String filename = "lnoend_500_4_10";
        String sources = String.format("%s/%s.test", BASE_PATH, filename);
        Task.init(sources);

        String path = String.format("./a.dungz/large/%s/%s/best.log", task, filename);
        // update_adj_matrix(path);
        // da_tree = create_da_tree();
    }

    protected void update_adj_matrix(String source) {
        Graph G = Task.G;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            String line = reader.readLine();
            expected = Double.parseDouble(line);

            line = reader.readLine();
            best_node = Integer.parseInt(line);

            reader.readLine();

            int counter = 0;
            while (counter < G.V) {
                reader.readLine();
                counter++;
            }

            G.init_adj_matrix();
            int E = Integer.parseInt(reader.readLine()); // 2*E
            G.init_edge_mapping(E / 2);

            String[] str;
            int label = 0;
            for (int i = 0; i < E; i++) {
                str = reader.readLine().split(" ");
                int u = Integer.parseInt(str[0]);
                int v = Integer.parseInt(str[1]);

                if (G.add_edge(u, v, label)) {
                    // System.out.println(String.format("Add %d %d", u, v));
                    label++;
                }
            }

            // G.show_adj_mtx();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ArrayList<Node> create_da_tree() {
        ArrayList<Node> da_tree = new ArrayList<>();
        Graph G = Task.G;

        for (Node node : G.vertices) {
            node.key = 1;
            node.parent = -1;
            da_tree.add(node.id, new Node(node));
        }

        boolean[] visited = new boolean[G.V];
        Arrays.fill(visited, false);

        Queue<Node> Q = new LinkedList<>();
        Q.add(da_tree.get(0));

        while (!Q.isEmpty()) {
            Node u = Q.poll();
            visited[u.id] = true;

            for (Integer v_id : G.adj.get(u.id)) {
                Node v = da_tree.get(v_id);
                if (!visited[v.id]) {
                    v.parent = u.id;
                    Q.add(v);
                }
            }
        }

        Decode.calculate_send_and_recv_packets(da_tree);
        return da_tree;
    }

    protected void show_da_tree() {
        Graph G = Task.G;
        ArrayList<ArrayList<Integer>> child = new ArrayList<>(G.V);
        for (int i = 0; i < G.V; i++) {
            child.add(new ArrayList<Integer>());
        }

        for (Node u : da_tree) {
            // System.out.println(String.format("%d: %.2f", u.id, u.get_energy_consumption(0)));
            if (u.parent != -1)
                child.get(u.parent).add(u.id);
        }

        for (int i = 0; i < G.V; i++) {
            System.out.println(i + ": " + child.get(i));
        }
    }

    // @Test
    public void mel_max() {
        System.out.println("Task: " + task);
        // show_da_tree();
        double actual = -Task.min_energy_left_max(da_tree);
        assertEquals(expected, actual, 0.01);
    }

    // @Test
    public void mec_min() {
        System.out.println("Task: " + task);
        show_da_tree();
        double actual = Task.max_energy_consumption_min(da_tree);
        assertEquals(expected, actual, 0.01);
    }

    // @Test
    public void ecmin() {
        System.out.println("Task: " + task);
        // show_da_tree();
        double actual = Task.energy_consumption_min(da_tree);
        assertEquals(expected, actual, 0.01);
    }
}
