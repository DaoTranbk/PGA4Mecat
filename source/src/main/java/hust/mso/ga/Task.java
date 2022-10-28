package hust.mso.ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task {
    public static double Tx, Rx;
    public static int q;
    public static Graph G;

    public static void init(String path) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            q = Integer.parseInt(line);

            String[] str = reader.readLine().split(" ");
            Tx = Double.parseDouble(str[0]);
            Rx = Double.parseDouble(str[1]);

            line = reader.readLine();
            int V = Integer.parseInt(line);
            G = new Graph(V);

            for (int i = 0; i < V; i++) {
                str = reader.readLine().split(" ");
                int id = Integer.parseInt(str[0]);
                Double x = Double.parseDouble(str[1]);
                Double y = Double.parseDouble(str[2]);
                Double r = Double.parseDouble(str[3]);
                Double init_energy = Double.parseDouble(str[4]);
                int data_size = Integer.parseInt(str[5]);
                // System.out.println(String.format("%d, %f, %f, %f, %f, %d", id, x, y, r,
                // init_energy, data_size));
                G.add_vertex(new Node(id, x, y, r, init_energy, data_size));
            }

            line = reader.readLine();
            int E = Integer.parseInt(line); // so canh 2*E
            Parameter.GENE_SIZE = E / 2;
            G.init_edge_mapping(E / 2);

            int label = 0;
            for (int i = 0; i < E; i++) {
                str = reader.readLine().split(" ");
                int u = Integer.parseInt(str[0]);
                int v = Integer.parseInt(str[1]);

                if (G.add_edge(u, v, label))
                    label++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double energy_consumption_min(ArrayList<Node> da_tree) {
        double total_energy_consumption = 0;
        for (Node node : da_tree) {
            if (node.id != 0)
                // total_energy_consumption += node.get_energy_consumption();
                total_energy_consumption += node.energy_comsumption();
        }

        return total_energy_consumption;
    }

    public static double max_energy_consumption_min(ArrayList<Node> da_tree) {
        double max_energy_consumption = 0;
        Graph G = Task.G;

        for (Integer nodeId : G.adj.get(0)) {
            Node u = da_tree.get(nodeId);
            double energy_consumption = u.get_energy_consumption();
            if (energy_consumption > max_energy_consumption) 
                max_energy_consumption = energy_consumption;
        }

        return max_energy_consumption;
    }

    public static double min_energy_left_max(ArrayList<Node> da_tree) {
        double min_energy_left = Parameter.INF;
        double tmp;
        for (Node node : da_tree) {
            if (node.id != 0) {
                tmp = node.get_energy_left();
                if (tmp < min_energy_left)
                    min_energy_left = tmp;
            }
        }

        return -min_energy_left;
    }
}
