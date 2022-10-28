package hust.mso.ga.Operator;

import java.util.ArrayList;
import java.util.Arrays;

import hust.mso.ga.Graph;
import hust.mso.ga.Individual;
import hust.mso.ga.Node;
import hust.mso.ga.Parameter;
import hust.mso.ga.Task;
import java.util.LinkedList;
import java.util.Queue;

public class Initialization {
    public static ArrayList<Individual> random(int size) {
        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Individual ind = new Individual();
            ind.init_random();
            individuals.add(ind);
        }

        return individuals;
    }

    public static ArrayList<Individual> heuristic_rand(int size) {
        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Individual ind = new Individual();
            ind.init_heuristic_random();
            individuals.add(ind);
        }

        return individuals;

    }

    public static ArrayList<Individual> mixed_rand(int size) {
        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Individual ind = new Individual();
            if (Parameter.rand.nextDouble() < 0.6) {
                ind.init_heuristic_random();
            } else {
                ind.init_random();
            }

            individuals.add(ind);
        }

        return individuals;
    }

    public static ArrayList<Individual> random_with_local_search(int size) {
        ArrayList<Individual> individuals = new ArrayList<>();
        boolean same_ind = false;
        for (int i = 0; i < size; i++) {
            Individual ind = new Individual();
            // if (Parameter.rand.nextDouble() < 0.6) {
                // ind.init_heuristic_random();
            // } else {
                ind.init_random();
            // }

            for (Individual cand: individuals) {
                same_ind |= Individual.check_same(ind, cand);
                if (same_ind) break;
            }

            if (same_ind) same_ind = false;
            else individuals.add(ind);
        }

        if (individuals.size() < size) {
            Individual seed_ind;
            ArrayList<Individual> local_search_inds = new ArrayList<>();
            // while (individuals.size() < size) {
            while (local_search_inds.size() < size - individuals.size()) {
                seed_ind = individuals.get(Parameter.rand.nextInt(individuals.size()));
                Individual new_ind = local_searchv_v2(seed_ind);

                for (Individual cand: individuals) {
                    same_ind |= Individual.check_same(new_ind, cand);
                    if (same_ind) break;
                }

                if (same_ind) same_ind = false;
                else {
                    new_ind.calculate_fitness();
                    if (Parameter.best_fitness > new_ind.fitness) {
                        Parameter.best_fitness = new_ind.fitness;
                        Parameter.best_ind = new_ind;
                    }

                    // System.out.println(seed_ind.fitness + " " + new_ind.fitness);

                    local_search_inds.add(new_ind);
                    // individuals.add(new_ind);
                }
            }

            individuals.addAll(local_search_inds);
        }

        // for (Individual ind: individuals) System.out.println(ind.fitness);
        // System.out.println(individuals.size());

        return individuals;
    }

    protected static Individual local_search(Individual seed_ind) {
        Individual ind = new Individual(); 
        ArrayList<Node> spanning_tree = Decode.create_spanning_tree(seed_ind.chromo);

        ArrayList<ArrayList<Integer>> children = new ArrayList<>();
        for (int i = 0; i < spanning_tree.size(); i++) {
            children.add(new ArrayList<Integer>());
        }

        // int i = 0;
        int[] new_spt = new int[Task.G.V - 1];;
        Node cut_node;
        Graph G = Task.G;
        boolean[] visited = new boolean[G.V];

        ArrayList<Integer> cut_off = new ArrayList<>();
        ArrayList<Integer> nodes_in_subtree = new ArrayList<>();
        Queue<Integer> Q = new LinkedList<>();

        // Get a node for disconnecting from the tree, this node must be an internal node 
        cut_node = spanning_tree.get(1 + Parameter.rand.nextInt(spanning_tree.size() - 1));

        for (Node u: spanning_tree) {
            if (u.parent != -1) {
                children.get(u.parent).add(u.id);
                new_spt[u.id - 1] = G.label[u.parent][u.id];
            }
        }

        // nodes_in_subtree.clear();
        // Q.clear();

        Arrays.fill(visited, false);
        // nodes_in_subtree.clear();

        Q.add(cut_node.id);

        int u;
        while (!Q.isEmpty()) {
            u = Q.poll();
            nodes_in_subtree.add(u);
            visited[u] = true;
            for (Integer v: children.get(u)) Q.add(v);
        }

        for (Integer v: nodes_in_subtree) {
            for (Integer x: G.adj.get(v)) {
                if (v == cut_node.id && x == cut_node.parent) continue;
                if (!visited[x]) cut_off.add(G.label[v][x]);
            }
        }

        Node prev = cut_node;
        Node next = spanning_tree.get(prev.parent);
        while (cut_off.isEmpty() && next.id != 0) {
            nodes_in_subtree.clear();

            Q.add(next.id);
            while (!Q.isEmpty()) {
                u = Q.poll();
                nodes_in_subtree.add(u);
                visited[u] = true;
                for (Integer v: children.get(u)) {
                    if (v != prev.id) Q.add(v);
                }
            }

            for (Integer v: nodes_in_subtree) {
                for (Integer x: G.adj.get(v)) {
                    if (v == next.id && x == next.parent) continue;
                    if (!visited[x]) cut_off.add(G.label[v][x]);
                }
            }

            prev = next;
            next = spanning_tree.get(prev.parent);
        }

        new_spt[cut_node.id - 1] = cut_off.get(Parameter.rand.nextInt(cut_off.size()));
        ind.set_chromosome(Encode.from_spt_to_netkeys(new_spt));

        return ind;
    }

    public static Individual local_searchv_v2(Individual seed_ind) {
        Individual ind = new Individual(); 
        ArrayList<Node> spanning_tree = Decode.create_spanning_tree(seed_ind.chromo);

        ArrayList<ArrayList<Integer>> children = new ArrayList<>();
        for (int i = 0; i < spanning_tree.size(); i++) {
            children.add(new ArrayList<Integer>());
        }

        for (Node u: spanning_tree) {
            if (u.parent != -1) children.get(u.parent).add(u.id);
        }

        int i = 0;
        int[] new_spt = new int[Task.G.V - 1];;
        Node cut_node;
        Graph G = Task.G;
        boolean[] visited = new boolean[G.V];

        ArrayList<Integer> cut_off = new ArrayList<>();
        ArrayList<Integer> nodes_in_subtree = new ArrayList<>();
        Queue<Integer> Q = new LinkedList<>();
        while (cut_off.isEmpty()) {
            // Get a node for disconnecting from the tree, this node must be an internal node 
            cut_node = spanning_tree.get(1 + Parameter.rand.nextInt(spanning_tree.size() - 1));

            i = 0;
            for (Node u: spanning_tree) {
                if (u.parent != -1 && u.id != cut_node.id) new_spt[i++] = G.label[u.parent][u.id];
            }

            nodes_in_subtree.clear();
            Q.clear();

            Arrays.fill(visited, false);
            Q.add(cut_node.id);

            int u;
            while (!Q.isEmpty()) {
                u = Q.poll();
                nodes_in_subtree.add(u);
                visited[u] = true;
                for (Integer v: children.get(u)) Q.add(v);
            }

            for (Integer v: nodes_in_subtree) {
                for (Integer x: G.adj.get(v)) {
                    if (v == cut_node.id && x == cut_node.parent) continue;
                    if (!visited[x]) cut_off.add(G.label[v][x]);
                }
            }
        }

        new_spt[i] = cut_off.get(Parameter.rand.nextInt(cut_off.size()));
        ind.set_chromosome(Encode.from_spt_to_netkeys(new_spt));

        return ind;
    }

}
