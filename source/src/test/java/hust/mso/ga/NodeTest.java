package hust.mso.ga;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class NodeTest {
    /**
     * Rigorous Test :-)
     * 
     * @throws CloneNotSupportedException
     */

    @Test
    public void priorityTest() {
        ArrayList<Node> da_tree = new ArrayList<>();
        da_tree.add(new Node(1, 0.5));
        da_tree.add(new Node(2, 0.2));
        da_tree.add(new Node(3, 0.9));
        da_tree.add(new Node(4, 0.9));

        PriorityQueue<Node> Q = new PriorityQueue<>();
        Q.addAll(da_tree);

        assertEquals(4, Q.size());

        // Chu y muon update node thi chi update khi da remove khoi Q, sau do add lai
        Node node = da_tree.get(2);
        node.key = 0.3;
        Q.remove(node);
        Q.add(node);

        Node another = Q.poll();

        assertEquals(da_tree.get(3), another);
    }

    // @Test
    public void treeSetTest() {
        ArrayList<Node> da_tree = new ArrayList<>();
        da_tree.add(new Node(1, 0.5));
        da_tree.add(new Node(2, 0.2));
        da_tree.add(new Node(3, 0.9));
        da_tree.add(new Node(4, 0.9));

        TreeSet<Node> Q = new TreeSet<>();
        Q.addAll(da_tree);

        Node node = da_tree.get(2);
        assertEquals(node, Q.first());

        // Khong the them node co cung measure value. Each element in treeSet has an unique measure value
        assertEquals(4, Q.size());

        // Chu y muon update node thi chi update khi da remove khoi Q, sau do add lai
        Q.remove(node);
        node.key = 0.2;
        Q.add(node);

        Node another = Q.pollFirst();
        assertEquals(da_tree.get(0), another);
    }
}
