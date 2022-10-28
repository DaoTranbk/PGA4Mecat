package hust.mso.ga;

import java.util.ArrayList;
import java.util.Random;

public class Parameter {
    public final static int MAX_GENERATION = 1000;
    public final static int POPULATION_SIZE = 100;
    public final static int PATIENCE_GENERATION = 1000;
    public static int FES = 0; // current number of function evalutions
    public static int Max_FES = 100000; // Maximum number of evaluations to perform

    public static double replace_rate = 0.05;
    public static int GENE_SIZE;

    public static double pc = 0.7;
    public static double pm = 0.1;

    public static Random rand;
    public static int seed = 0;

    public static int INF = (int) 1e8;

    public static final int UPPER_BOUND = 1;
    public static final int LOWER_BOUND = 0;

    public static double runtime = 0;
    public static ArrayList<String> gen_best_log; // for each generation, using this to save the best objectives

    public static Individual best_ind;
    public static double best_fitness = INF;

    public static void init(double pc, double pm) {
        Parameter.pc = pc;
        Parameter.pm = pm;

        gen_best_log = new ArrayList<>(MAX_GENERATION);
        for (int i = 0; i < MAX_GENERATION; i++) {
            String str = String.format("%d, ", i + 1);
            gen_best_log.add(str);
        }
    }

    public static void set_seed(int _seed) {
        seed = _seed;
        rand = new Random(seed);
        best_fitness = INF;
        FES = 0;
        runtime = 0;
    }

    public static void save_generation(int gen) {
        String str = String.format("%.0f ", best_ind.fitness);
        gen_best_log.set(gen, String.format("%s%s", gen_best_log.get(gen), str));
        // System.out.println(gen + " " + str);
    }
}
