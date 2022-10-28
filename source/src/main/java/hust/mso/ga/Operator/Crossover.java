package hust.mso.ga.Operator;

import java.util.ArrayList;

import hust.mso.ga.Parameter;
import hust.mso.ga.Util;

public class Crossover {
    // A large value of eta_c gives a higher probability for creating
    // ‘near-parent’ solutions (thereby allowing a focussed search)
    // and a small value of η c allows distant solutions to be selected
    // as offspring (thereby allowing to make diverse search)
    protected static final int eta_c = 4; // distribution index (SBX)
    protected static final double EPS = 1.0e-14; // EPS defines the minimum difference allowed between real values 

    protected static final double alpha = 0.67; // BLX


    public static ArrayList<ArrayList<Double>> execute(ArrayList<Double> p1, ArrayList<Double> p2) {
        // return two_point(p1, p2);
        return SBX(p1, p2);
        // return uniform(p1, p2);
        // return BLX_alpha(p1, p2);
        // return PBX_alpha(p1, p2);
    }

    protected static ArrayList<ArrayList<Double>> SBX(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);
        chromosomes.add(new ArrayList<Double>(p1));
        chromosomes.add(new ArrayList<Double>(p2));

        final double lower_bound = Parameter.LOWER_BOUND, upper_bound = Parameter.UPPER_BOUND;
        double u;
        double y1, y2; // y1: min parent, y2: max parent
        double c1, c2;
        double alpha, beta, betaq;
        double valueX1, valueX2;

        // int l = 0, r = Parameter.GENE_SIZE - 1;
        int l = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        int r = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        while (r == l) r = Parameter.rand.nextInt(Parameter.GENE_SIZE);

        if (r < l) {
            r = r + l;
            l = r - l;
            r = r - l;
        }

        for (int i = l; i <= r; i++) {
            valueX1 = p1.get(i);
            valueX2 = p2.get(i);

            if (Math.abs(valueX1 - valueX2) > EPS) {
                y1 = Double.min(valueX1, valueX2);
                y2 = Double.max(valueX1, valueX2);

                u = Parameter.rand.nextDouble();

                beta = 1 + (2 * (y1 - lower_bound) / (y2 - y1));
                alpha = 2 - Math.pow(beta, -(eta_c + 1));

                if (u <= 1.0 / alpha) {
                    betaq = Math.pow(u * alpha, 1.0 / (eta_c + 1));
                } else {
                    betaq = Math.pow(1.0 / (2 - u * alpha), 1.0 / (eta_c + 1));
                }
                c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

                beta = 1 + (2 * (upper_bound - y2) / (y2 - y1));
                alpha = 2 - Math.pow(beta, -(eta_c + 1));

                if (u <= 1.0 / alpha) {
                    betaq = Math.pow(u * alpha, 1.0 / (eta_c + 1));
                } else {
                    betaq = Math.pow(1.0 / (2 - u * alpha), 1.0 / (eta_c + 1));
                }
                c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                c1 = Util.repair_sol_in_bound(c1, lower_bound, upper_bound);
                c2 = Util.repair_sol_in_bound(c2, lower_bound, upper_bound);

                if (Parameter.rand.nextDouble() < 0.5) {
                    double tmp = c1;
                    c1 = c2;
                    c2 = tmp;
                }

            } else {
                c1 = valueX1;
                c2 = valueX2;
            }

            chromosomes.get(0).set(i, c1);
            chromosomes.get(1).set(i, c2);
        }

        return chromosomes;
    }

    protected static ArrayList<ArrayList<Double>> PMX(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);

        return chromosomes;
    }

    protected static ArrayList<ArrayList<Double>> uniform(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);
        chromosomes.add(new ArrayList<Double>());
        chromosomes.add(new ArrayList<Double>());

        double allele_p1, allele_p2;
        double c1, c2;
        int rnd;
        for (int i = 0; i < Parameter.GENE_SIZE; i++) {
            allele_p1 = p1.get(i);
            allele_p2 = p2.get(i);

            rnd = Parameter.rand.nextInt(2);
            if (rnd == 1) {
                c1 = allele_p1;
                c2 = allele_p2;
            } else {
                c1 = allele_p2;
                c2 = allele_p1;
            }

            chromosomes.get(0).add(c1);
            chromosomes.get(1).add(c2);
        }

        return chromosomes;
    }

    protected static ArrayList<ArrayList<Double>> BLX_alpha(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);
        chromosomes.add(new ArrayList<Double>(p1));
        chromosomes.add(new ArrayList<Double>(p2));

        double allele_p1, allele_p2;
        double c1, c2;
        double rnd;
        double min_range, max_range, range, min, max;

        int l, r;
        l = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        r = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        while (r == l) r = Parameter.rand.nextInt(Parameter.GENE_SIZE);

        if (r < l) {
            r = r + l;
            l = r - l;
            r = r - l;
        }

        // for (int i = 0; i < Parameter.GENE_SIZE; i++) {
        for (int i = l; i <= r; i++) {
            allele_p1 = p1.get(i);
            allele_p2 = p2.get(i);

            min = Math.min(allele_p1, allele_p2);
            max = Math.max(allele_p1, allele_p2);

            range = max - min;
            min_range = min - range * alpha;
            max_range = max + range * alpha;

            rnd = Parameter.rand.nextDouble();
            c1 =  min_range + rnd * (max_range - min_range);

            rnd = Parameter.rand.nextDouble();
            c2 =  min_range + rnd * (max_range - min_range);

            c1 = Util.repair_sol_in_bound(c1, Parameter.LOWER_BOUND, Parameter.UPPER_BOUND);
            c2 = Util.repair_sol_in_bound(c2, Parameter.LOWER_BOUND, Parameter.UPPER_BOUND);

            chromosomes.get(0).set(i, c1);
            chromosomes.get(1).set(i, c2);
        }

        return chromosomes;
    }

    protected static ArrayList<ArrayList<Double>> PBX_alpha(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);
        chromosomes.add(new ArrayList<Double>());
        chromosomes.add(new ArrayList<Double>());

        double allele_p1, allele_p2;
        double c1, c2;
        double rnd;
        double min_range, max_range, range; 
        for (int i = 0; i < Parameter.GENE_SIZE; i++) {
            allele_p1 = p1.get(i);
            allele_p2 = p2.get(i);

            range = Math.abs(allele_p1 - allele_p2);
            min_range = allele_p1 - range * alpha;
            max_range = allele_p1 + range * alpha;

            rnd = Parameter.rand.nextDouble();
            c1 =  min_range + rnd * (max_range - min_range);

            min_range = allele_p2 - range * alpha;
            max_range = allele_p2 + range * alpha;
            rnd = Parameter.rand.nextDouble();
            c2 =  min_range + rnd * (max_range - min_range);

            c1 = Util.repair_sol_in_bound(c1, Parameter.LOWER_BOUND, Parameter.UPPER_BOUND);
            c2 = Util.repair_sol_in_bound(c2, Parameter.LOWER_BOUND, Parameter.UPPER_BOUND);

            chromosomes.get(0).add(c1);
            chromosomes.get(1).add(c2);
        }

        return chromosomes;
    }

    protected static ArrayList<ArrayList<Double>> two_point(ArrayList<Double> p1, ArrayList<Double> p2) {
        ArrayList<ArrayList<Double>> chromosomes = new ArrayList<>(2);
        chromosomes.add(new ArrayList<Double>(p1));
        chromosomes.add(new ArrayList<Double>(p2));

        int l = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        int r = Parameter.rand.nextInt(Parameter.GENE_SIZE);
        while (r == l) r = Parameter.rand.nextInt(Parameter.GENE_SIZE);

        if (r < l) {
            r = r + l;
            l = r - l;
            r = r - l;
        }

        // for (int i = 0; i < Parameter.GENE_SIZE; i++) {
        double tmp;
        for (int i = l; i <= r; i++) {
            tmp = chromosomes.get(0).get(i);
            chromosomes.get(0).set(i, chromosomes.get(1).get(i));
            chromosomes.get(1).set(i, tmp);
        }

        return chromosomes;
    }
}
