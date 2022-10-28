package hust.mso.ga;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import hust.mso.ga.Operator.Decode;

/**
 * Unit test for simple App.
 */
public class OpTest {
    private final String dir_test = "./mock/sample";
    private final String EXT_SAMPLE = ".test";

    @Before
    public void bootstrap() {
        final String input1 = "spanning_tree_rnd";
        String path = String.format("%s/%s%s", dir_test, input1, EXT_SAMPLE);

        Parameter.set_seed(10);
        Task.init(path);
    }

    @Test
    public void init() {
        Individual ind = new Individual();
        ind.init_random();

        Individual cand = new Individual();
        cand.init_random();

        System.out.println(Individual.check_same(ind, cand));
    }
}
