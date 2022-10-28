package hust.mso.ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Test {
    static String path;
    protected static void init() {
        String filename = "lnoend_500_4_10";
        path = String.format("./a.dungz/large/%s/%s/best.log", "mecmin", filename);
    }

    public static int[] create_best_ind(int taskId) {
        init();
        Graph G = Task.G;
        int[] tree_edge_list = null; 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            line = reader.readLine();
            int best_node = Integer.parseInt(line);

            line = reader.readLine();
            int V = Integer.parseInt(line);

            int counter = 0;
            int[][] tree = new int[V][V];

            tree_edge_list = new int[V-1];

            while (counter < V) {
                Arrays.fill(tree[counter], 0);
                reader.readLine();
                counter++;
            }

            int E = Integer.parseInt(reader.readLine()); // 2*E

            String[] str;
            counter = 0;
            for (int i = 0; i < E; i++) {
                str = reader.readLine().split(" ");
                int u = Integer.parseInt(str[0]);
                int v = Integer.parseInt(str[1]);
                if (tree[u][v] == 0) {
                    tree[u][v] = 1;
                    tree[v][u] = 1;
                    tree_edge_list[counter++] = G.label[u][v];
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tree_edge_list;
    }
}
