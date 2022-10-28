package hust.mso.ga.Operator;

import java.util.ArrayList;
import java.util.Arrays;

import hust.mso.ga.Heuristic.Heuristic;
import hust.mso.ga.Parameter;

public class Encode {
    /*
     * @Param: spanning_tree: the tree 's edge list, taskId
     * @Return: uss_gene
     */
    public static ArrayList<Double> from_spt_to_netkeys(int[] spanning_tree) {
        int E = Parameter.GENE_SIZE;
        ArrayList<Double> netkeys = new ArrayList<>(E);
        for (int i = 0; i < E; i++)
            netkeys.add(0.0);

        double divide_point = 0.5 + Parameter.rand.nextDouble() * 0.5;

        double rnd;
        boolean[] visited_edge = new boolean[E];
        Arrays.fill(visited_edge, false);

        for (int edge_label : spanning_tree) {
            rnd = Parameter.rand.nextDouble() * (1 - divide_point) + divide_point;
            netkeys.set(edge_label, rnd);
            visited_edge[edge_label] = true;
        }

        for (int i = 0; i < E; i++) {
            if (!visited_edge[i]) {
                rnd = Parameter.rand.nextDouble() * divide_point;
                netkeys.set(i, rnd);
            }
        }

        return netkeys;
    }

    public static ArrayList<Double> heuristic_rand() {
        ArrayList<Double> chromo;

        // double delta = 1.0 / 4;
        // double u = Parameter.rand.nextDouble();
        int[] spanning_tree;

        // if (u < delta) spanning_tree = Heuristic.balance_load();
        // else if (u < 3*delta) spanning_tree = Heuristic.redundant_packet_min();
        // else spanning_tree = Heuristic.random();
        spanning_tree = Heuristic.redundant_packet_min();

        chromo = from_spt_to_netkeys(spanning_tree);
        return chromo;
    }

    // random netkeys using the uniform distribution in range [0, 1)
    public static ArrayList<Double> random_netkeys() {
        int size = Parameter.GENE_SIZE;

        ArrayList<Double> genes;
        double uL = Parameter.LOWER_BOUND;
        double uR = Parameter.UPPER_BOUND;

        genes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            genes.add(i, uL + Parameter.rand.nextDouble() * (uR - uL));
        }

        return genes;
    }
}
