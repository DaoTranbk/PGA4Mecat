package hust.mso.ga;

import java.util.ArrayList;

import hust.mso.ga.Operator.Encode;
import hust.mso.ga.Operator.Decode;

public class Individual implements Comparable<Individual> {
    public double fitness;
    public ArrayList<Double> chromo;

    public int created_by = 0; // 0: init, 1: crossover, 2: mutation

    public void calculate_fitness() {
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(chromo);
        fitness = Task.energy_consumption_min(da_tree); // ecmin
        // fitness = Task.max_energy_consumption_min(da_tree); // mecmin
        // fitness = Task.min_energy_left_max(da_tree); // melmax
    }

    public void init_heuristic_random() {
        chromo = Encode.heuristic_rand();
        calculate_fitness();
        if (Parameter.best_fitness > fitness) {
            Parameter.best_fitness = fitness;
            Parameter.best_ind = this;
        }
    }

    public void init_random() {
        chromo = Encode.random_netkeys();
        // chromo = Encode.chaotic_netkeys();
        calculate_fitness();
        if (Parameter.best_fitness > fitness) {
            Parameter.best_fitness = fitness;
            Parameter.best_ind = this;
        }
    }

    public void set_chromosome(ArrayList<Double> chromo) {
        this.chromo = chromo;
    }

    public void set_fitness(double val) {
        fitness = val;
    }

    static public boolean check_same(Individual ind, Individual cand) {
        ArrayList<Node> spt_1 = Decode.create_spanning_tree(ind.chromo);
        ArrayList<Node> spt_2 = Decode.create_spanning_tree(cand.chromo);

        Node u, v;
        for (int i = 0; i < spt_1.size(); i++) {
            u = spt_1.get(i); v = spt_2.get(i);
            // System.out.println(u.parent + " " + v.parent);
            if (u.parent != v.parent) return false;
        }

        return true;
    }

    @Override
    // Increase Order
    public int compareTo(Individual ind) {
        if (fitness < ind.fitness)
            return -1;
        if (fitness > ind.fitness)
            return 1;
        return 0;
    }
}
