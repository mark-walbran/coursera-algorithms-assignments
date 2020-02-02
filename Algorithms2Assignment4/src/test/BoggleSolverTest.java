import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Test;


public class BoggleSolverTest {

  /**
   * From assignment description: To stress test the performance of your implementation, create one
   * markw.BoggleSolver object (from a given dictionary); then, repeatedly generate and solve random
   * Hasbro boards. How many random Hasbro boards can you solve per second? For full credit, your
   * program must be able to solve thousands of random Hasbro boards per second. The goal on this
   * assignment is raw speed for example, it's fine to use 10x more memory if the program is 10x
   * faster.
   */
  @Test
  public void stressTest() {
    In in = new In("test-input/dictionary-yawl.txt");
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);

    for (int i = 0; i < 100; i++) {
      BoggleBoard board = new BoggleBoard();
      solver.getAllValidWords(board);
    }
  }

  @Test
  public void test() {
    In in = new In("test-input/dictionary-algs4.txt");
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board1 = new BoggleBoard("test-input/board4x4.txt");
    BoggleBoard board2 = new BoggleBoard("test-input/board-q.txt");

    int score1 = 0;
    for (String word : solver.getAllValidWords(board1)) {
      score1 += solver.scoreOf(word);
    }

    int score2 = 0;
    for (String word : solver.getAllValidWords(board2)) {
      score2 += solver.scoreOf(word);
    }

    assertEquals(33, score1);
    assertEquals(84, score2);
  }
}
