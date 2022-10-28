package hust.mso.ga;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import hust.mso.ga.Operator.Decode;

/**
 * Unit test for simple App.
 */
public class FitnessTest {
    /**
     * Rigorous Test :-)
     */

    @Test
    public void init() {
        String path = String.format("%s/sample/da_tree.test", Main.MOCK_PATH);
        Task.init(path);

        ArrayList<Double> weight = new ArrayList<>(Arrays.asList(
                1.0, // 0-1
                1.0, // 0-2
                1.0, // 1-3
                1.0, // 1-4
                1.0, // 1-5
                0.5, // 2-5
                0.5, // 2-6
                0.4, // 3-7
                0.5, // 3-8
                0.6, // 4-8
                0.8, // 4-9
                0.3 // 8-9
        ));
        ArrayList<Node> da_tree = Decode.create_data_aggregation_tree(weight);

        assertEquals(0, Task.G.label[0][1]);
        assertEquals(1, Task.G.label[0][2]);
        assertEquals(2, Task.G.label[1][3]);
        assertEquals(3, Task.G.label[1][4]);
        assertEquals(4, Task.G.label[1][5]);
        assertEquals(5, Task.G.label[2][5]);
        assertEquals(6, Task.G.label[2][6]);
        assertEquals(7, Task.G.label[3][7]);
        assertEquals(8, Task.G.label[3][8]);
        assertEquals(9, Task.G.label[4][8]);
        assertEquals(10, Task.G.label[4][9]);
        assertEquals(11, Task.G.label[8][9]);

        for(Node node: da_tree) {
            System.out.println(node.id + ": " + node.parent + " " + node.aggregation_report_size);
        }

        double obj = Task.energy_consumption_min(da_tree);
        System.out.println(obj);
    }
}
