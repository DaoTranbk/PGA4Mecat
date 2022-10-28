package hust.mso.ga;

import java.util.ArrayList;

import hust.mso.ga.Operator.Selection;

public class GA {
    private Population pop = new Population(); 
    int nic = 0, gen = 0;
    public Individual run() {
        pop.init(Parameter.POPULATION_SIZE);
        Parameter.FES += pop.size();
        Parameter.save_generation(gen);
        gen++;
        int replace_select = 10;
        int counter_loop = 0;

        int child_pop_size;
        while (Parameter.FES < Parameter.Max_FES && gen < Parameter.MAX_GENERATION) {
            ArrayList<Individual> child = new ArrayList<>();
            child_pop_size = (Parameter.Max_FES - Parameter.FES) > Parameter.POPULATION_SIZE ? Parameter.POPULATION_SIZE
                    : (Parameter.Max_FES - Parameter.FES);
            while (child.size() < child_pop_size) {
                child.addAll(crossover_mutation_v2());
            }

            Parameter.FES += child.size();

            pop.members.addAll(child);
            if (counter_loop < replace_select) {
                pop.selection(false);
                counter_loop++;
            } else {
                counter_loop = 0;
                pop.selection(true);
            }

            Parameter.save_generation(gen);

            // check_local();

            gen++;
        }

        return Parameter.best_ind;
    }

    public void check_local() {
        // double prev_best_fitness = Parameter.gen_best.get(gen - 1);
        // double curr_best_fitness = Parameter.gen_best.get(gen);
        // if (curr_best_fitness < prev_best_fitness) {
            // nic = 0;
        // } else {
            // nic++;
        // }
    }

    public ArrayList<Individual> crossover_mutation() {
        ArrayList<Individual> offsprings = new ArrayList<>();
        ArrayList<Individual> parents;
        // if (Parameter.rand.nextDouble() < 0.1) {
            parents = Selection.tourament(pop.members);
        // } else {
            // parents = Selection.random(pop.members);
        // }

        Individual parent1 = parents.get(0);
        Individual parent2 = parents.get(1);
        double rnd = Parameter.rand.nextDouble();
        if (rnd < Parameter.pc) {
            offsprings = pop.crossover(parent1, parent2);
        } else if (rnd < Parameter.pm) {
            offsprings.add(pop.mutation(parent1));
            offsprings.add(pop.mutation(parent2));

        }

        for (Individual ind : offsprings) {
            ind.calculate_fitness();
        }

        return offsprings;
    }

    public ArrayList<Individual> crossover_mutation_v2() {
        ArrayList<Individual> offsprings = new ArrayList<>();
        ArrayList<Individual> parents;
        // if (Parameter.rand.nextDouble() < 0.3) {
            // parents = Selection.tourament(pop.members);
        // } else {
            parents = Selection.random(pop.members);
        // }

        Individual parent1 = parents.get(0);
        Individual parent2 = parents.get(1);
        double rnd = Parameter.rand.nextDouble();
        if (rnd < Parameter.pc) {
            offsprings = pop.crossover(parent1, parent2);
            int i = 0;
            for (Individual ind: offsprings) {
                rnd = Parameter.rand.nextDouble();
                if (rnd < Parameter.pm) {
                    offsprings.set(i, pop.mutation(ind));
                    i++;
                }
            }
        }

        for (Individual ind : offsprings) {
            ind.calculate_fitness();
        }

        return offsprings;
    }
}
