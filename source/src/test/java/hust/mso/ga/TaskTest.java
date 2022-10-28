package hust.mso.ga;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import hust.mso.ga.Operator.Decode;

/**
 * Unit test for simple App.
 */
public class TaskTest {
    /**
     * Rigorous Test :-)
     */

    public void init() {
        String[] args = { "sample.test", "1" };
        Main.main(args);
    }

    public void show_da_tree() {
        Individual best_fit = Parameter.best_ind;
        System.out.println(best_fit);
        ArrayList<Double> weight = best_fit.chromo;
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);

        int V = Task.G.V;
        ArrayList<ArrayList<Integer>> child = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            child.add(i, new ArrayList<Integer>());
        }

        for (Node node : da_tree) {
            if (node.parent != -1) {
                child.get(node.parent).add(node.id);
            }
        }

        int i = 0;
        for (ArrayList<Integer> _child : child) {
            System.out.println(i + ": " + _child);
            i++;
        }
    }
}
