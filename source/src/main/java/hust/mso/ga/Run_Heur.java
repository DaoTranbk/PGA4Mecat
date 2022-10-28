package hust.mso.ga;

import java.util.ArrayList;

import hust.mso.ga.Heuristic.Heuristic;
import hust.mso.ga.Operator.Decode;
import hust.mso.ga.Operator.Encode;

public class Run_Heur {
    static final String BASE_PATH = "./cache/aTuan/test";
    static final String MOCK_PATH = "./mock";
    static final String EXT_SAMPLE = ".test";
    static final String EXT_OUT = ".csv";

    public static void main(String[] args) {
        double avg = 0;
        for (int r = 10; r <= 20; r += 5) {
        for (int i = 100; i <= 190; i += 10) {
                final String filename = String.format("m%d_4_%d", i, r);
                String path = String.format("%s/%s%s", BASE_PATH, filename, EXT_SAMPLE);

                Task.init(path);

                int[] spanning_tree; 
                // = Heuristic.redundant_packet_min();

                // ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
                // ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
                // String str = String.format("RPMSPT: %.0f %.0f %.0f\n", Task.energy_consumption_min(da_tree), Math.abs(Task.min_energy_left_max(da_tree)), Task.max_energy_consumption_min(da_tree));
                // String str = String.format("%.0f, ", Task.min_energy_left_max(da_tree));

                // spanning_tree = Heuristic.balance_load();
                // weight = Encode.from_spt_to_netkeys(spanning_tree);
                // da_tree = Decode.create_data_aggregation_tree(weight);
                // str += String.format("BLSPT: %.0f %.0f %.0f\n", Task.energy_consumption_min(da_tree), Math.abs(Task.min_energy_left_max(da_tree)), Task.max_energy_consumption_min(da_tree));
                // str += String.format("%.0f, ", Task.min_energy_left_max(da_tree));

                avg = 0;
                for (int j = 0; j < 30; j++) {
                    Parameter.set_seed(j+1);
                    spanning_tree = Heuristic.redundant_packet_min();
                    ArrayList<Double> weight = Encode.from_spt_to_netkeys(spanning_tree);
                    ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);
                    avg += Task.energy_consumption_min(da_tree);
                }
                String str = String.format("%.2f", avg/30);
                System.out.println(str);
                // str += String.format("SPT: %.0f %.0f %.0f\n", Task.energy_consumption_min(da_tree), Math.abs(Task.min_energy_left_max(da_tree)), Task.max_energy_consumption_min(da_tree));
                
                // spanning_tree = Heuristic.ec();
                // weight = Encode.from_spt_to_netkeys(spanning_tree);
                // da_tree = Decode.create_data_aggregation_tree(weight);
                // str += String.format("EC: %.0f %.0f %.0f\n", Task.energy_consumption_min(da_tree), Math.abs(Task.min_energy_left_max(da_tree)), Task.max_energy_consumption_min(da_tree));
            }
        }
    }

    static void on_start_up(String[] args) {
        // if (args.length < 3) {
        // System.err.println("Args format: input1 input2 seed");
        // System.exit(-1);
        // }

        String path = String.format("%s/%s%s", BASE_PATH, args[0], EXT_SAMPLE);
        int seed = Integer.parseInt(args[1]);
        double pc = Double.parseDouble(args[2]);
        double pm = Double.parseDouble(args[3]);

        Task.init(path);
        Parameter.init(pc, pm);
        Parameter.set_seed(seed);
    }
}
