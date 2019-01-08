package graph;

/* See restrictions in Graph.java. */
import java.util.ArrayList;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Sachi Kulkarni
 */
abstract class GraphObj extends Graph {
    /** An ArrayList of the vertices in a graph. */
    private ArrayList<Integer> vertices;
    /** An ArrayList of the edges in a graph where each edge
     * is an array of the x and y coordinates. */
    private ArrayList<int[]> edges;
    /** An ArrayList of ArrayLists to hold successors. */
    private ArrayList<ArrayList<Integer>> succ;
    /** An ArrayList of ArrayLists to hold predecessors. */
    private ArrayList<ArrayList<Integer>> pred;
    /** A new, empty Graph. */
    GraphObj() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        succ = new ArrayList<>();
        pred = new ArrayList<>();
        succ.add(null);
        pred.add(null);
    }

    @Override
    public int vertexSize() {
        return vertices.size();
    }

    @Override
    public int maxVertex() {
        int maxVal = vertices.get(vertexSize() - 1);
        for (int i = 0; i < vertexSize(); i++) {
            if (vertices.get(i) > maxVal) {
                maxVal = vertices.get(i);
            }
        }
        return maxVal;
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        int out = 0;
        for (int i = 0; i < edgeSize(); i++) {
            if (isDirected()) {
                if (edges.get(i)[0] == v) {
                    out += 1;
                }
            } else {
                if (edges.get(i)[0] == v || edges.get(i)[1] == v) {
                    out += 1;
                }
            }
        }
        return out;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        for (int i = 0; i < vertexSize(); i++) {
            if (vertices.get(i) == u) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && contains(v)) {
            for (int i = 0; i < edgeSize(); i++) {
                if (isDirected()) {
                    if (edges.get(i)[0] == u && edges.get(i)[1] == v) {
                        return true;
                    }
                } else {
                    if ((edges.get(i)[0] == u && edges.get(i)[1] == v)
                            || (edges.get(i)[1] == u && edges.get(i)[0] == v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int add() {
        int next = 1;
        while (contains(next)) {
            next += 1;
        }
        vertices.add(next - 1, next);
        boolean set = false;
        for (int i = 1; i < succ.size(); i++) {
            if (succ.get(i) == null) {
                succ.set(i, new ArrayList<>());
                pred.set(i, new ArrayList<>());
                set = true;
            }
        }
        if (!set) {
            succ.add(new ArrayList<>());
            pred.add(new ArrayList<>());
        }
        return next;
    }

    @Override
    public int add(int u, int v) {
        if (contains(u) && contains(v)) {
            if (!contains(u, v)) {
                int[] arr = new int[2];
                arr[0] = u;
                arr[1] = v;
                edges.add(arr);
                if (!succ.get(u).contains(v)) {
                    succ.get(u).add(v);
                }
                if (!succ.get(v).contains(u)) {
                    if (!isDirected()) {
                        succ.get(v).add(u);
                    }
                }
                if (!pred.get(v).contains(u)) {
                    pred.get(v).add(u);
                }
                if (!pred.get(u).contains(v)) {
                    if (!isDirected()) {
                        pred.get(u).add(v);
                    }
                }
            }
            return edgeId(u, v);
        } else {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    @Override
    public void remove(int v) {
        ArrayList<int[]> removing = new ArrayList<>();
        if (contains(v)) {
            for (int i = 0; i < vertexSize(); i++) {
                if (vertices.get(i) == v) {
                    vertices.remove(vertices.get(i));
                    ArrayList<Integer> wasIn = succ.get(v);
                    succ.set(v, null);
                    if (wasIn != null) {
                        for (int ver : wasIn) {
                            pred.get(ver).remove((Integer) v);
                        }
                    }
                    pred.set(v, null);
                    break;
                }
            }
            for (int i = 0; i < edgeSize(); i++) {
                if (edges.get(i)[0] == v || edges.get(i)[1] == v) {
                    removing.add(edges.get(i));
                }
            }
            for (int i = 0; i < removing.size(); i++) {
                for (int k = 0; k < edgeSize(); k++) {
                    if (edges.get(k) == removing.get(i)) {
                        edges.remove(removing.get(i));
                        break;
                    }
                }
            }
            for (int i = 1; i < succ.size(); i++) {
                ArrayList<Integer> in = succ.get(i);
                ArrayList<Integer> n = pred.get(i);
                if (in != null) {
                    for (int j = 0; j < in.size(); j++) {
                        int val = in.get(j);
                        if (val == v) {
                            in.remove((Integer) val);
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("vertex not in Graph");
        }
    }

    @Override
    public void remove(int u, int v) {
        ArrayList<int[]> removing = new ArrayList<>();
        if (contains(u, v)) {
            for (int i = 0; i < edgeSize(); i++) {
                if (edges.get(i)[0] == u && edges.get(i)[1] == v) {
                    removing.add(edges.get(i));
                }
            }
            for (int j = 0; j < removing.size(); j++) {
                for (int k = 0; k < edgeSize(); k++) {
                    if (edges.get(k) == removing.get(j)) {
                        edges.remove(removing.get(j));
                        break;
                    }
                }
            }
            if (isDirected()) {
                succ.get(u).remove((Integer) v);
                pred.get(v).remove((Integer) u);
            } else {
                succ.get(u).remove((Integer) v);
                succ.get(v).remove((Integer) u);
                pred.get(v).remove((Integer) u);
                pred.get(u).remove((Integer) u);
            }
        } else {
            throw new IllegalArgumentException("edge not in Graph");
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        return Iteration.iteration(vertices.iterator());
    }

    @Override
    public Iteration<Integer> successors(int v) {
        if (succ.get(v) == null) {
            return Iteration.iteration(new ArrayList<>());
        }
        return Iteration.iteration(succ.get(v).iterator());
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        return Iteration.iteration(edges.iterator());
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        if (contains(u) && contains(v) && contains(u, v)) {
            if (isDirected()) {
                for (int i = 0; i < edgeSize(); i++) {
                    if (edges.get(i)[0] == u && edges.get(i)[1] == v) {
                        return i + 1;
                    }
                }
            } else if (!isDirected()) {
                for (int i = 0; i < edgeSize(); i++) {
                    if ((edges.get(i)[0] == u && edges.get(i)[1] == v)
                            || (edges.get(i)[0] == v
                            && edges.get(i)[1] == u)) {
                        return i + 1;
                    }
                }
            }
        }
        return 0;
    }
    /** The # of incoming edges incident to V, or 0 if V is not
     *  one of my vertices.
     *  @return int */
    int inDeg(int v) {
        int in = 0;
        for (int i = 0; i < edgeSize(); i++) {
            if (edges.get(i)[1] == v) {
                in += 1;
            }
        }
        return in;
    }
    /** Returns an iteration over all predecessors of V.
     *  Empty if V is not my vertex. */
    Iteration<Integer> preds(int v) {
        if (pred.get(v) == null) {
            return Iteration.iteration(new ArrayList<>());
        }
        return Iteration.iteration(pred.get(v).iterator());
    }

}
