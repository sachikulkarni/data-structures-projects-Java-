package graph;

/* See restrictions in Graph.java. */

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Sachi Kulkarni
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        _set = new TreeSet<Integer>(custom);
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        Iteration<Integer> vert = _G.vertices();
        ArrayList<Integer> marked = new ArrayList<>();
        setWeight(getSource(), 0);
        setPredecessor(getSource(), 0);
        _set.add(getSource());
        while (!_set.isEmpty()) {
            int v = _set.pollFirst();
            marked.add(v);
            if (v == _dest) {
                return;
            }
            for (int success: _G.successors(v)) {
                if (getWeight(v) + getWeight(v, success) < getWeight(success)) {
                    if (!marked.contains(success)) {
                        _set.remove(success);
                        setWeight(success,
                                getWeight(v) + getWeight(v, success));
                        setPredecessor(success, v);
                        _set.add(success);
                    }
                }

            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> temppath = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        temppath.add(v);
        int ver = v;
        while (ver != getSource()) {
            ver = getPredecessor(ver);
            temppath.add(ver);
        }
        for (int i = temppath.size() - 1; i >= 0; i--) {
            path.add(temppath.get(i));
        }
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** New customized comparator to be used in A* search. */
    private class Customized implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (estimatedDistance(o1) + getWeight(o1)
                    > estimatedDistance(o2) + getWeight(o2)) {
                return 1;
            } else if (estimatedDistance(o1) + getWeight(o1)
                    < estimatedDistance(o2) + getWeight(o2)) {
                return -1;
            } else {
                if (o1 < o2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }
    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** The next set of vertices to look at. */
    private TreeSet<Integer> _set;
    /** A new comparator. */
    private Customized custom = new Customized();

}
