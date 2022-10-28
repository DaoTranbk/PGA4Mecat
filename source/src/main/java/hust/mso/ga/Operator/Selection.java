package hust.mso.ga.Operator;

import java.util.ArrayList; import java.util.Arrays; import java.util.Collections;

import hust.mso.ga.Individual;
import hust.mso.ga.Parameter;

public class Selection {
    // ** for crossover-mutation **
    protected static Individual choose_better(Individual p1, Individual p2) {
        // If 1st ind's scalarFitness ? 2nd ind's scalarFitness
        if (p1.compareTo(p2) == -1)
            return p1;
        return p2;
    }

    // Chon 4 ca the ngau nhien roi lay ra 2 ca the tot nhat trong so do
    public static ArrayList<Individual> tourament(ArrayList<Individual> individuals) {
        ArrayList<Individual> parents = new ArrayList<>();
        int bound = individuals.size();
        boolean[] choosen = new boolean[bound];
        Arrays.fill(choosen, false);
        int[] parent_id = new int[4];

        int i = 0;
        int val;
        while (i < 4) {
            val = Parameter.rand.nextInt(bound);
            if (!choosen[val]) {
                parent_id[i] = val;
                choosen[val] = true;
                i++;
            }
        }

        parents.add(choose_better(individuals.get(parent_id[0]), individuals.get(parent_id[1])));
        parents.add(choose_better(individuals.get(parent_id[2]), individuals.get(parent_id[3])));

        return parents;
    }

    public static ArrayList<Individual> random(ArrayList<Individual> individuals) {
        ArrayList<Individual> parents = new ArrayList<>();
        int length = individuals.size();
        int a = Parameter.rand.nextInt(length);
        int b = Parameter.rand.nextInt(length);
        while (a == b) {
            b = Parameter.rand.nextInt(length);
        }

        parents.add(individuals.get(a));
        parents.add(individuals.get(b));
        return parents;
    }

    // ** for population selection **
    public static ArrayList<Individual> elistic(ArrayList<Individual> individuals) {
        Collections.sort(individuals);
        ArrayList<Individual> selected_members = new ArrayList<>();
        for (int i = 0; i < individuals.size(); i++) {
            selected_members.add(individuals.get(i));
            if (selected_members.size() >= Parameter.POPULATION_SIZE)
                break;
        }

        // individuals = selected_members;
        Parameter.best_ind = selected_members.get(0);
        return selected_members;
    }

    public static ArrayList<Individual> elistic_with_replace(ArrayList<Individual> individuals) {
        Collections.sort(individuals);
        ArrayList<Individual> selected_members = new ArrayList<>();
        int no_elistic = (int) (Parameter.POPULATION_SIZE * (1 - Parameter.replace_rate));
        for (int i = 0; i < no_elistic; i++) selected_members.add(individuals.get(i));

        Parameter.best_ind = selected_members.get(0);
        ArrayList<Individual> heur_members = Initialization.heuristic_rand(Parameter.POPULATION_SIZE - no_elistic);
        selected_members.addAll(heur_members);

        // individuals = selected_members;
        return selected_members;
    }
}
