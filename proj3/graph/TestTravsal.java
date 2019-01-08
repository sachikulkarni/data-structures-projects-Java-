package graph;

import org.junit.Test;

public class TestTravsal {
    @Test
    public void testTraversal() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 5);
        g.add(1, 6);
        g.add(2, 4);
        g.add(4, 3);
        g.add(6, 5);
        g.add(4, 5);
        Traversal x = new DepthFirstTraversal(g);
        Traversal y = new BreadthFirstTraversal(g);
        x.traverse(1);
        y.traverse(1);
    }
    @Test
    public void testTraversal2() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 1);
        g.add(1, 5);
        g.add(1, 6);
        g.add(6, 1);
        g.add(2, 4);
        g.add(4, 3);
        g.add(6, 5);
        g.add(4, 5);
        Traversal x = new DepthFirstTraversal(g);
        Traversal y = new BreadthFirstTraversal(g);
        x.traverse(1);
        y.traverse(1);
    }
}
