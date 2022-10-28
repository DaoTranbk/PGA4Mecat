package hust.mso.ga;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import hust.mso.ga.Operator.Encode;

public class EncodeTest {
    private final String dir_test = "./mock/sample";
    private final String EXT_SAMPLE = ".test";

    @Before
    public void init() {
        final String input1 = "spanning_tree_rnd";
        String path = String.format("%s/%s%s", dir_test, input1, EXT_SAMPLE);

        Parameter.set_seed(10);
        Task.init(path);
    }

    @Test
    public void heuristic_rand() {
        ArrayList<Double> uss_genes = Encode.heuristic_rand();
        assertEquals(19, uss_genes.size());
    }

    @Test
    public void random_netkeys() {
        ArrayList<Double> uss_genes = Encode.random_netkeys();
    }

}