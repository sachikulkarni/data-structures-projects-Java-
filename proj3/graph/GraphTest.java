package graph;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Sachi Kulkarni
 */
public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }
    @Test
    public void testVerSizeandMaxVer() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(g.vertexSize(), 4);
        assertEquals(g.maxVertex(), 4);
        g.remove(1);
        assertEquals(g.vertexSize(), 3);
        assertEquals(g.maxVertex(), 4);

    }
    @Test
    public void testEdges() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(3, 4);
        g.add(4, 2);
        assertEquals(g.edgeSize(), 4);
        g.remove(4);
        assertFalse(g.contains(3, 4));
        assertFalse(g.contains(4, 2));
        assertEquals(2, g.edgeSize());
        g.add(5, 2);
        g.add(3, 2);
        assertEquals(4, g.edgeSize());
        g.remove(5, 2);
        assertFalse(g.contains(5, 2));
        assertEquals(3, g.edgeSize());
    }
    @Test
    public void testDegreeandVerticesDirected() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(3, 4);
        g.add(4, 2);
        assertTrue(g.contains(1));
        assertFalse(g.contains(0));
        g.remove(5);
        assertFalse(g.contains(5));
        g.add();
        assertTrue(g.contains(5));
        assertEquals(g.outDegree(1), 2);
        assertEquals(g.inDegree(2), 2);
        assertEquals(g.outDegree(4), 1);
        ArrayList<Integer> vertices = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        Iteration<Integer> vert = g.vertices();
        while (vert.hasNext()) {
            int val = vert.next();
            vertices.add(val);
        }
        assertEquals(test, vertices);
        g.remove(5);
        vertices.clear();
        vert = g.vertices();
        while (vert.hasNext()) {
            int val = vert.next();
            vertices.add(val);
        }
        test.remove(4);
        assertEquals(test, vertices);
    }
    @Test
    public void testDegreeandVerticesUndirected() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(1, 3);
        u.add(3, 2);
        u.add(2, 4);
        u.add(3, 5);
        assertEquals(u.outDegree(1), 2);
        assertEquals(u.inDegree(5), 1);
        u.remove(4);
        assertFalse(u.contains(4));
        u.add();
        assertTrue(u.contains(4));
    }
    @Test
    public void testSuccess() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(3, 4);
        g.add(4, 2);
        Iteration<Integer> success = g.successors(1);
        ArrayList<Integer> suc = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        test.add(2);
        test.add(3);
        while (success.hasNext()) {
            int val = success.next();
            suc.add(val);
        }
        assertEquals(test, suc);
        test.clear();
        test.add(4);
        Iteration<Integer> success2 = g.successors(3);
        ArrayList<Integer> suc2 = new ArrayList<>();
        while (success2.hasNext()) {
            int val = success2.next();
            suc2.add(val);
        }
        assertEquals(test, suc2);
        test.clear();
        test.add(1);
        test.add(4);
    }
    @Test
    public void testPreds() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(3, 4);
        g.add(4, 2);
        Iteration<Integer> preds = g.predecessors(2);
        ArrayList<Integer> pre = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        ArrayList<int[]> test2 = new ArrayList<>();
        test.clear();
        test.add(1);
        test.add(4);
        while (preds.hasNext()) {
            int val = preds.next();
            pre.add(val);
        }
        assertEquals(test, pre);
        int [] one = {1, 2};
        int [] two = {1, 3};
        int [] three = {3, 4};
        int [] four = {4, 2};
        test2.add(one);
        test2.add(two);
        test2.add(three);
        test2.add(four);
        g.remove(1);
        preds = g.predecessors(3);
        test.clear();
        pre.clear();
        while (preds.hasNext()) {
            int val = preds.next();
            pre.add(val);
        }
        assertEquals(test, pre);
    }
    @Test
    public void testEdges2() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(3, 4);
        g.add(4, 2);
        Iteration<int[]> edge = g.edges();
        ArrayList<int[]> edg = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        ArrayList<int[]> test2 = new ArrayList<>();
        test.clear();
        test.add(1);
        test.add(4);
        int [] one = {1, 2};
        int [] two = {1, 3};
        int [] three = {3, 4};
        int [] four = {4, 2};
        test2.add(one);
        test2.add(two);
        test2.add(three);
        test2.add(four);
        while (edge.hasNext()) {
            int[] val = edge.next();
            edg.add(val);
        }
        assertArrayEquals(test2.get(0), edg.get(0));
        assertArrayEquals(test2.get(1), edg.get(1));
        assertEquals(test2.size(), edg.size());
    }
    @Test
    public void testEdgesSuccess2() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(1, 3);
        u.add(3, 2);
        u.add(2, 4);
        u.add(3, 5);
        Iteration<Integer> success = u.successors(3);
        ArrayList<Integer> suc = new ArrayList<>();
        Iteration<int[]> edge = u.edges();
        ArrayList<int[]> edg = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        ArrayList<int[]> test2 = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(5);
        while (success.hasNext()) {
            int val = success.next();
            suc.add(val);
        }
        assertEquals(test, suc);
        u.remove(1);
        success = u.successors(3);
        test.clear();
        suc.clear();
        test.add(2);
        test.add(5);
        while (success.hasNext()) {
            int val = success.next();
            suc.add(val);
        }
        assertEquals(test, suc);
    }
    @Test
    public void testEdgesPreds2() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(1, 3);
        u.add(3, 2);
        u.add(2, 4);
        u.add(3, 5);
        Iteration<Integer> preds = u.predecessors(2);
        ArrayList<Integer> pre = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(5);
        test.clear();
        test.add(1);
        test.add(3);
        test.add(4);
        while (preds.hasNext()) {
            int val = preds.next();
            pre.add(val);
        }
        assertEquals(test, pre);
    }
    @Test
    public void testEdges3() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(1, 3);
        u.add(3, 2);
        u.add(2, 4);
        u.add(3, 5);
        Iteration<int[]> edge = u.edges();
        ArrayList<int[]> edg = new ArrayList<>();
        ArrayList<Integer> test = new ArrayList<>();
        ArrayList<int[]> test2 = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(5);
        test.clear();
        test.add(1);
        test.add(3);
        test.add(4);
        int [] one = {1, 2};
        int [] two = {1, 3};
        int [] three = {3, 2};
        int [] four = {2, 4};
        int [] five = {3, 5};
        test2.add(one);
        test2.add(two);
        test2.add(three);
        test2.add(four);
        test2.add(five);
        while (edge.hasNext()) {
            int[] val = edge.next();
            edg.add(val);
        }
        assertArrayEquals(test2.get(0), edg.get(0));
        assertArrayEquals(test2.get(1), edg.get(1));
        assertEquals(test2.size(), edg.size());
    }
    @Test
    public void testDirectedEdgeID() {
        DirectedGraph d = new DirectedGraph();
        d.add();
        d.add();
        d.add(1, 2);
        assertEquals(0, d.edgeId(2, 1));
        assertEquals(1, d.edgeId(1, 2));
        d.add();
        d.add();
        d.add();
        d.add(1, 3);
        d.add(1, 4);
        assertEquals(2, d.edgeId(1, 3));
        assertEquals(3, d.edgeId(1, 4));
        assertEquals(0, d.edgeId(3, 4));
        assertEquals(0, d.edgeId(4, 1));
    }
    @Test
    public void testUndirectedEdgeID() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        assertEquals(1, u.edgeId(2, 1));
        assertEquals(1, u.edgeId(1, 2));
        assertEquals(0, u.edgeId(1, 3));
        u.add();
        u.add(1, 3);
        u.add(1, 4);
        assertEquals(2, u.edgeId(1, 3));
        assertEquals(3, u.edgeId(1, 4));
    }
    @Test
    public void testSuccessors() {
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(1, 3);
        u.add(2, 3);
        Iterator<Integer> succ = u.successors(1);
        assertEquals(succ.next(), (Integer) 2);
        assertEquals(succ.next(), (Integer) 3);
    }
    @Test
    public void testUnSuccessors() {
        DirectedGraph u = new DirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add(1, 2);
        u.add(3, 1);
        u.add(2, 3);
        Iterator<Integer> succ = u.successors(1);
        assertEquals(succ.next(), (Integer) 2);
        assertFalse(succ.hasNext());
    }
}
