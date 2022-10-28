package hust.mso.ga;

import java.util.ArrayList;

import hust.mso.ga.Operator.Crossover;
import hust.mso.ga.Operator.Initialization;
import hust.mso.ga.Operator.Mutation;
import hust.mso.ga.Operator.Selection;

/*
    Ref: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.33.7291&rep=rep1&type=pdf
    SBX Operator, Polymonial Mutation
*/

public class Population {
    protected ArrayList<Individual> members;

    public void init(int size) {
        // members = Initialization.random(size);
        // members = Initialization.heuristic_rand(size);
        // members = Initialization.mixed_rand(size);
        members = Initialization.random_with_local_search(size);
    }

    public void selection(boolean replacable) {
        if (replacable) members = Selection.elistic_with_replace(members);
        else members = Selection.elistic(members);
        // Parameter.best_ind = members.get(0);
    }

    public Individual mutation(Individual parent) {
        Individual child = new Individual();
        ArrayList<Double> chromosome = Mutation.execute(parent.chromo);
        child.set_chromosome(chromosome);

        child.created_by = 2;

        return child;
    }

    public ArrayList<Individual> crossover(Individual parent_1, Individual parent_2) {
        ArrayList<Individual> offsprings = new ArrayList<>();
        ArrayList<ArrayList<Double>> chromosomes = Crossover.execute(parent_1.chromo, parent_2.chromo);
        for (ArrayList<Double> chromo : chromosomes) {
            Individual ind = new Individual();
            ind.set_chromosome(chromo);

            ind.created_by = 1;
            offsprings.add(ind);
        }

        return offsprings;
    }

    public int size() {
        return members.size();
    }
}
