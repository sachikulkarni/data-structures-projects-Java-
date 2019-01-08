package graph;

import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestShortestPaths {
    class TestSP extends SimpleShortestPaths {
        TestSP(Graph g, int source, int dest) {
            super(g, source, dest);
        }

        @Override
        protected double getWeight(int u, int v) {
            if (u == 1 && v == 2) {
                return 1;
            } else if (u == 1 && v == 4) {
                return 2;
            } else if (u == 2 && v == 4) {
                return 4;
            } else if (u == 4 && v == 5) {
                return 1;
            } else if (u == 2 && v == 3) {
                return 2;
            } else if (u == 3 && v == 5) {
                return 5;
            } else {
                return 4;
            }
        }

    }
    @Test
    public void shortestPathTest() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 3);
        g.add(4, 1);
        g.add(4, 2);
        g.add(4, 5);
        g.add(5, 3);
        g.add(2, 5);
        TestSP path = new TestSP(g, 1, 5);
        path.setPaths();
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(4);
        test.add(5);
        assertEquals(test, path.pathTo(5));
    }
}
