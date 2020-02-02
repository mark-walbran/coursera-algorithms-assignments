import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * A solver to find a "least-moves" solution of the n-size generalisation of the "8-puzzle", using
 * the A* algorithm. For use on Coursera, Algorithms Part I programming assignment.
 */

public class Solver {

  private static final boolean PRIORITY_FUNCTION = true; // false = Hamming, true = Manhattan

  private boolean solvable;
  private SearchNode solutionNode;

  /**
   * Search node helper class that will be used to define the objects linked by the MinPQ
   *
   * @author Mark
   */
  private class SearchNode implements Comparable<SearchNode> {

    private final Board board;
    private final int numberOfMoves;
    private final SearchNode predecessorNode;
    private final int manhattanDistance;
    private final int hammingDistance;
    private final boolean twinFlag;

    /**
     * Creates a new search node, initialises it's variables and calculatates it's manhattan and
     * hamming distances in advance.
     *
     * @param board the board
     * @param numberOfMoves the number of moves
     * @param predecessorNode the previous node
     * @param twinFlag a flag representing whether it is a twin or not
     */
    public SearchNode(Board board, int numberOfMoves, SearchNode predecessorNode,
                      boolean twinFlag) {
      this.board = board;
      this.numberOfMoves = numberOfMoves;
      this.predecessorNode = predecessorNode;
      this.twinFlag = twinFlag;

      manhattanDistance = board.manhattan();
      hammingDistance = board.hamming();
    }

    /**
     * Compares to search nodes by either Hamming function (if PriorityFunction parameter set to
     * false) or Manhattan function (if PriorityFunction set to true).
     * <p>
     * Breaks ties by the alternate priority function value.
     */
    @Override
    public int compareTo(SearchNode other) {
      int priority;
      int priorityOther;
      int priorityDiff;

      if (PRIORITY_FUNCTION) {
        priority = numberOfMoves + manhattanDistance;
        priorityOther = other.numberOfMoves() + other.manhattanDistance();
        priorityDiff = priority - priorityOther;

        if (priorityDiff == 0) {
          return manhattanDistance - other.manhattanDistance();
        }

        return priorityDiff;
      } else {
        priority = numberOfMoves + hammingDistance;
        priorityOther = other.numberOfMoves() + other.hammingDistance();
        priorityDiff = priority - priorityOther;

        if (priorityDiff == 0) {
          return manhattanDistance - other.manhattanDistance();
        }

        return priorityDiff;
      }

    }

    public int numberOfMoves() {
      return numberOfMoves;
    }

    public int manhattanDistance() {
      return manhattanDistance;
    }

    public int hammingDistance() {
      return hammingDistance;
    }

    public boolean isTwin() {
      return twinFlag;
    }
  }

  /**
   * Constructs the solver to find a "least-moves" solution of the n-size generalisation of the
   * "8-puzzle", using the A* algorithm.
   *
   * @param initial the initial board.
   * @throws IllegalArgumentException if passed a null argument.
   */
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("Input must be non null.");
    }

    // Insert initial board and twin of initial board
    MinPQ<SearchNode> pq = new MinPQ<>();
    SearchNode initialNode = new SearchNode(initial, 0, null, false);
    SearchNode twinNode = new SearchNode(initial.twin(), 0, null, true);
    pq.insert(initialNode);
    pq.insert(twinNode);

    // While not on solution board
    while (true) {
      SearchNode dequeue = pq.delMin();
      if (dequeue.board.isGoal()) {
        solutionNode = dequeue;
        break;
      }

      // Enqueue all neighbours, except predecessor board
      for (Board neighbour : dequeue.board.neighbors()) {
        if (!((dequeue.predecessorNode != null)
            && (neighbour.equals(dequeue.predecessorNode.board)))) {
          pq.insert(
              new SearchNode(neighbour, dequeue.numberOfMoves() + 1, dequeue, dequeue.isTwin()));
        }
      }
    }

    solvable = !solutionNode.isTwin();
  }

  /**
   * Is the initial board solvable?
   */
  public boolean isSolvable() {
    return solvable;
  }

  /**
   * Returns the minimum number of moves to solve the initial board; -1 if unsolvable.
   */
  public int moves() {
    if (!isSolvable()) {
      return -1;
    }

    return solutionNode.numberOfMoves();
  }

  /**
   * Returns the iterator for the sequence of boards in a shortest solution; null if unsolvable.
   */
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }

    Stack<Board> solution = new Stack<>();

    SearchNode currentNode = solutionNode;

    while (currentNode != null) {
      solution.push(currentNode.board);
      currentNode = currentNode.predecessorNode;
    }

    return solution;
  }

  /**
   * Solves a slider puzzle.
   *
   * @param args the slider puzzle to solve.
   */
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        blocks[i][j] = in.readInt();
      }
    }
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable()) {
      StdOut.println("No solution possible");
    } else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) {
        StdOut.println(board);
      }
    }
  }
}
