package hust.mso.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import hust.mso.ga.Operator.Decode;

public class Logger {
    public static void log(Individual sol, String solver, String input) {
        String info = get_info(sol);
        System.out.print(info);

        int[][] tree;
        Graph G = Task.G;
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(sol.chromo);
        tree = new int[G.V][G.V];
        for (int i = 0; i < G.V; i++) {
            Arrays.fill(tree[i], 0);
        }

        for (Node node : da_tree) {
            if (node.parent != -1) {
                tree[node.parent][node.id] = 1;
                tree[node.id][node.parent] = 1;
            }
        }

        String log = String.format("%f\n%d\n", sol.fitness, G.V);
        for (int i = 0; i < G.V; i++) {
            for (int j = 0; j < G.V; j++) {
                if (tree[i][j] != 0)
                    log += String.format("%d %d\n", i, j);
            }
        }

        try {
            String logger_dir = String.format("./log/%s", solver);
            File dir = new File(logger_dir);
            dir.mkdirs();

            FileWriter writer = new FileWriter(String.format("%s/%s.log", logger_dir, input)); 
            writer.write(log);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String get_info(Individual ind) {
        String info = String.format("%d, %.2f, ", Parameter.seed, Parameter.runtime);
        String format = "%.0f\n";
        info += String.format(format, Math.abs(ind.fitness));

        return info;
    }
}
