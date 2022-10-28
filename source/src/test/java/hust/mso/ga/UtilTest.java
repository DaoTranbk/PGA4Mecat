package hust.mso.ga;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UtilTest {
    /**
     * Rigorous Test :-)
     */

    @Test
    public void argmin() {
        double[] arr = {3, 5, 2, 4};
        assertEquals(2, Util.argmin(arr));
    }

    @Test
    public void is_single_value_arr() {
        double[] arr = {1, 1, 1, 1};
        assertEquals(true, Util.is_single_value_arr(arr));
    }

    @Test
    public void copy() {
        ArrayList<ArrayList<Integer>> ls = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ls.add(new ArrayList<Integer>());
        }

        ArrayList<ArrayList<Integer>> y = new ArrayList<>(ls);
        int i = 0;
        for (ArrayList<Integer> e: y) {
            y.set(i++, new ArrayList<>(e));
        }
        y.get(0).add(10);

        ArrayList<Integer> m = new ArrayList<>();
        m.add(1);
        m.add(2);

        ArrayList<Integer> n = new ArrayList<>(m);
        n.set(0, 1);
    }
}
