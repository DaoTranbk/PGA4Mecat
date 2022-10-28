package hust.mso.ga;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import hust.mso.ga.Heuristic.Heuristic;
import hust.mso.ga.Operator.Decode;
import hust.mso.ga.Operator.Encode;

public class HeuristicTest {
    // private final String dir_test = "./cache/aTuan/test";
    private final String dir_test = "./cache";
    private final String EXT_SAMPLE = ".test";

    @Before
    public void init() {
        // final String filename = "l100_4_10";
        final String filename = "lnoend_100_4_20";
        String path = String.format("%s/%s%s", dir_test, filename, EXT_SAMPLE);

        Parameter.set_seed(6);
        Task.init(path);
    }

    @Test
    public void random() {
        int[] spanning_tree = Heuristic.bfs_random();

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("rand: " + Task.energy_consumption_min(da_tree) + " " + Task.min_energy_left_max(da_tree)
                + " " + Task.max_energy_consumption_min(da_tree));
    }

    @Test
    public void redundant_packet() {
        for (int i = 0; i < 1; i++) {
            int[] spt = Heuristic.redundant_packet_min();

            ArrayList<Double> weight = Encode.from_spt_to_netkeys(spt);
            ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
            System.out.println("redundant: " + Task.energy_consumption_min(da_tree) + " "
                    + Task.min_energy_left_max(da_tree) + " " + Task.max_energy_consumption_min(da_tree));
        }
    }

    @Test
    public void kruskal_rand() {
        int[] spanning_tree = Heuristic.kruskal_random();

        // int[] uv;
        // for (int edge_label: spanning_tree) {
        // uv = G.get_edge(edge_label);
        // System.out.println(uv[0] + " " + uv[1]);
        // }

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("kruskal_rand: " + Task.energy_consumption_min(da_tree) + " "
                + Task.min_energy_left_max(da_tree) + " " + Task.max_energy_consumption_min(da_tree));
    }

    @Test
    public void balance_load() {
        int[] spanning_tree = Heuristic.balance_load();

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("load: " + Task.energy_consumption_min(da_tree) + " " + Task.min_energy_left_max(da_tree)
                + " " + Task.max_energy_consumption_min(da_tree));

    }

    @Test
    public void balance_energy_left() {
        int[] spanning_tree = Heuristic.balance_energy_left();

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("left: " + Task.energy_consumption_min(da_tree) + " " + Task.min_energy_left_max(da_tree)
                + " " + Task.max_energy_consumption_min(da_tree));
    }

    @Test
    public void ec() {
        int[] spanning_tree = Heuristic.ec();

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("EC: " + Task.energy_consumption_min(da_tree) + " " + Task.min_energy_left_max(da_tree)
                + " " + Task.max_energy_consumption_min(da_tree));
    }

    @Test
    public void spt() {
        int[] spanning_tree = Heuristic.SPT();

        ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
        System.out.println("SPT: " + Task.energy_consumption_min(da_tree) + " " + Task.min_energy_left_max(da_tree)
                + " " + Task.max_energy_consumption_min(da_tree));
    }
}
