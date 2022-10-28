package hust.mso.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static String BASE_PATH = "./cache/data";
    static final String MOCK_PATH = "./mock";
    static final String EXT_SAMPLE = ".test";
    static final String EXT_OUT = ".csv";
    static final String EXT_LOG = ".txt";
    static final String target = "ecmin";
    static final int MAX_SEED = 30;

    static final String prog = "rand-init-sbx4-local-search-mut-select10";

    public static void main(String[] args) {
        on_start_up(args);

        String outdir = String.format("%s/quynq/%s-100000evals-1000Gen/%s", MOCK_PATH, prog, target);
        String outdir_rs = String.format("%s/result", outdir);
        String outdir_log = String.format("%s/log", outdir);

        String filename_rs = String.format("%s%s", args[0], EXT_OUT);
        String filename_log = String.format("%s%s", args[0], EXT_LOG);

        File f_rs = new File(outdir_rs);
        File f_log = new File(outdir_log);

        f_rs.mkdirs(); f_log.mkdirs();

        String header = "seed, runtime(s), ecmin\n";

        try {
            FileWriter writer_rs = new FileWriter(String.format("%s/%s", outdir_rs, filename_rs));
            writer_rs.write(header);

            for (int seed = 1; seed < MAX_SEED + 1; seed++) {
                Parameter.set_seed(seed);
                GA solver = new GA();
                double start = System.currentTimeMillis();
                Individual best_solution = solver.run();
                Parameter.runtime = (System.currentTimeMillis() - start) / 1000;

                String info = String.format("%d, %.2f, ", Parameter.seed, Parameter.runtime);
                String format = "%.0f\n";
                info += String.format(format, Math.abs(best_solution.fitness));

                // System.out.print(info);
                writer_rs.write(info);
            }


            FileWriter writer_log = new FileWriter(String.format("%s/%s", outdir_log, filename_log));
            for (String gen: Parameter.gen_best_log) {
                writer_log.write(gen + "\n");
            }

            writer_rs.close();
            writer_log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void on_start_up(String[] args) {
        // if (args.length < 3) {
        // System.err.println("Args format: input1 input2 seed");
        // System.exit(-1);
        // }

        // int seed = Integer.parseInt(args[1]);
        double pc = Double.parseDouble(args[1]);
        double pm = Double.parseDouble(args[2]);

        if (args.length >= 4) BASE_PATH = args[3];
        // int seed = Integer.parseInt(args[4]);

        String path = String.format("%s/%s%s", BASE_PATH, args[0], EXT_SAMPLE);

        Task.init(path);
        // Parameter.init(seed, pc, pm);
        Parameter.init(pc, pm);
        // Parameter.set_seed(seed);
    }
}
