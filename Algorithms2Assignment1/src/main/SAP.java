import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * An immutable data-type that represents the shortest ancestral path between two vertices on a
 * digraph, as well as the shortest common ancestor at the root of this path. For use on Coursera,
 * Algorithms Part II programming assignment.
 */
public class SAP {

  private static final int INFINITY = Integer.MAX_VALUE;
  private final Digraph digraph;

  private class BFSAncestralPath {

    private boolean[] markedA; // marked[v] = is there an s->v path?
    private boolean[] markedB; // marked[v] = is there an s->v path?
    private int[] distToA; // distTo[v] = length of shortest s->v path
    private int[] distToB; // distTo[v] = length of shortest s->v path
    private int shortestAncestralPathLength = INFINITY;
    private int shortestAncestor = -1;

    /**
     * Computes the shortest common ancestral path between {@code sourceA} and {@code sourceB}.
     *
     * @param G       the digraph
     * @param sourceA the first source vertex
     * @param sourceB the second source vertex
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public BFSAncestralPath(Digraph G, int sourceA, int sourceB) {
      markedA = new boolean[G.V()];
      markedB = new boolean[G.V()];
      distToA = new int[G.V()];
      distToB = new int[G.V()];

      validateVertex(sourceA, sourceB);
      bfs(G, sourceA, sourceB);
    }

    /**
     * Computes the shortest common ancestral path from any one of the source vertices in {@code
     * sourcesA} to any one of the source vertices in {@code sourcesB}.
     *
     * @param G        the digraph
     * @param sourcesA the first group of source vertices
     * @param sourcesB the second group of source vertices
     * @throws IllegalArgumentException unless each vertex {@code v} in {@code sources} satisfies
     *                                  {@code 0 <= v < V}
     */
    public BFSAncestralPath(Digraph G, Iterable<Integer> sourcesA, Iterable<Integer> sourcesB) {
      markedA = new boolean[G.V()];
      markedB = new boolean[G.V()];
      distToA = new int[G.V()];
      distToB = new int[G.V()];

      validateVertices(sourcesA, sourcesB);
      bfs(G, sourcesA, sourcesB);
    }

    public int length() {
      return shortestAncestralPathLength;
    }

    public int ancestor() {
      return shortestAncestor;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int a, int b) {

      int vertex = markedA.length;
      if (a < 0 || a >= vertex) {
        throw new IllegalArgumentException(
            "vertex A " + a + " is not between 0 and " + (vertex - 1));
      }
      if (b < 0 || b >= vertex) {
        throw new IllegalArgumentException(
            "vertex B " + b + " is not between 0 and " + (vertex - 1));
      }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> verticesA, Iterable<Integer> verticesB) {
      if (verticesA == null || verticesB == null) {
        throw new IllegalArgumentException("argument is null");
      }

      int vertex = markedA.length;
      for (Integer v : verticesA) {
        if ((v == null) || (v < 0 || v >= vertex)) {
          throw new IllegalArgumentException(
              "vertex A " + v + " is not between 0 and " + (vertex - 1));
        }
      }

      for (Integer v : verticesB) {
        if ((v == null) || (v < 0 || v >= vertex)) {
          throw new IllegalArgumentException(
              "vertex B " + v + " is not between 0 and " + (vertex - 1));
        }
      }

    }

    // BFS from two sources
    private void bfs(Digraph G, int sourceA, int sourceB) {
      // Check if sourceA == sourceB
      if (sourceA == sourceB) {
        shortestAncestralPathLength = 0;
        shortestAncestor = sourceA;
        return;
      }

      Queue<Integer> q = new Queue<>();
      markedA[sourceA] = true;
      markedB[sourceB] = true;
      q.enqueue(sourceA);
      q.enqueue(sourceB);

      calculatedAncestralPath(G, q);
    }

    private void calculatedAncestralPath(Digraph G, Queue<Integer> q) {
      while (!q.isEmpty()) {
        int v = q.dequeue();
        if ((distToA[v] > shortestAncestralPathLength)
            && (distToB[v] > shortestAncestralPathLength)) {
          return;
        }

        for (int w : G.adj(v)) {
          if (markedA[v] && !markedA[w]) {
            distToA[w] = distToA[v] + 1;
            markedA[w] = true;
            q.enqueue(w);
          } else if (markedB[v] && !markedB[w]) {
            distToB[w] = distToB[v] + 1;
            markedB[w] = true;
            q.enqueue(w);
          }

          if (markedA[w] && markedB[w]) {
            int newSAPLength = distToA[w] + distToB[w];
            if (newSAPLength < shortestAncestralPathLength) {
              shortestAncestralPathLength = newSAPLength;
              shortestAncestor = w;
            }
          }
        }
      }
    }

    // BFS from two groups of sources
    private void bfs(Digraph G, Iterable<Integer> sourcesA, Iterable<Integer> sourcesB) {
      // Check if sourceA == sourceB
      for (int s : sourcesA) {
        for (int t : sourcesB) {
          if (s == t) {
            shortestAncestralPathLength = 0;
            shortestAncestor = s;
            return;
          }
        }
      }

      Queue<Integer> q = new Queue<>();
      for (int s : sourcesA) {
        markedA[s] = true;
        q.enqueue(s);
      }

      for (int s : sourcesB) {
        markedB[s] = true;
        q.enqueue(s);
      }

      calculatedAncestralPath(G, q);
    }
  }

  /**
   * Constructs the Shortest Ancestral Path immutable data type by reading in a defensive copy of
   * the digraph input.
   *
   * @param G digraph input.
   * @throws IllegalArgumentException if null argument.
   */
  public SAP(Digraph G) {
    digraph = new Digraph(G);
  }

  /**
   * Returns the length of shortest ancestral path between v and w; -1 if no such path.
   *
   * @param v the first vertex.
   * @param w the second vertex.
   * @throws IllegalArgumentException if null argument or vertex argument outside prescribed range.
   */
  public int length(int v, int w) {
    BFSAncestralPath bfsAncestralPath = new BFSAncestralPath(digraph, v, w);

    int pathLength = bfsAncestralPath.length();

    if (pathLength == INFINITY) {
      return -1;
    } else {
      return pathLength;
    }
  }

  /**
   * Returns a common ancestor of v and w that participates in a shortest ancestral path; -1 if no
   * such path.
   *
   * @param v the first vertex.
   * @param w the second vertex.
   * @throws IllegalArgumentException if null argument or vertex argument outside prescribed range.
   */
  public int ancestor(int v, int w) {
    BFSAncestralPath bfsAncestralPath = new BFSAncestralPath(digraph, v, w);

    return bfsAncestralPath.ancestor();
  }

  /**
   * Returns the length of shortest ancestral path between any vertex in subset v and any vertex in
   * subset w; -1 if no such path.
   *
   * @param v the iterable list of integers containing the first subset.
   * @param w the iterable list of integers containing the second subset.
   * @throws IllegalArgumentException if null argument, iterable argument contains null item or
   *                                  vertex argument outside prescribed range.
   */
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    BFSAncestralPath bfsAncestralPath = new BFSAncestralPath(digraph, v, w);

    int pathLength = bfsAncestralPath.length();

    if (pathLength == INFINITY) {
      return -1;
    } else {
      return pathLength;
    }
  }

  /**
   * Returns a common ancestor that participates in shortest ancestral path between any vertex in
   * subset v and any vertex in subset w; -1 if no such path.
   *
   * @param v the iterable list of integers containing the first subset.
   * @param w the iterable list of integers containing the second subset.
   * @throws IllegalArgumentException if null argument, iterable argument contains null item or
   *                                  vertex argument outside prescribed range.
   */
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    BFSAncestralPath bfsAncestralPath = new BFSAncestralPath(digraph, v, w);

    return bfsAncestralPath.ancestor();
  }


  /**
   * The following test client takes the name of a digraph input file as as a command-line argument,
   * constructs the digraph, reads in vertex pairs from standard input, and prints out the length of
   * the shortest ancestral path between the two vertices and a common ancestor that participates in
   * that path.
   *
   * @param args the name of a diagraph input file.
   */
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
