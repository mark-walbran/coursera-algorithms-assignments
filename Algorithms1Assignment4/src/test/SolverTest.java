import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolverTest {

  Solver solver;
  Board initialBoard;

  @Test
  void basicTest() {
    int blocks[][] = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    initialBoard = new Board(blocks);
    solver = new Solver(initialBoard);

    StringBuilder expectedSolutionStringBuilder = new StringBuilder();
    int blocksSol[][] = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    int blocksSol2[][] = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
    int blocksSol3[][] = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
    int blocksSol4[][] = {{1, 2, 3}, {4, 5, 0}, {7, 8, 6}};
    int blocksSol5[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    Board boardSol = new Board(blocksSol);
    Board boardSol2 = new Board(blocksSol2);
    Board boardSol3 = new Board(blocksSol3);
    Board boardSol4 = new Board(blocksSol4);
    Board boardSol5 = new Board(blocksSol5);
    expectedSolutionStringBuilder.append(boardSol);
    expectedSolutionStringBuilder.append(boardSol2);
    expectedSolutionStringBuilder.append(boardSol3);
    expectedSolutionStringBuilder.append(boardSol4);
    expectedSolutionStringBuilder.append(boardSol5);
    String expectedSolution = expectedSolutionStringBuilder.toString();

    Iterable<Board> solution = solver.solution();
    StringBuilder solutionStringBuilder = new StringBuilder();
    for (Board t : solution) {
      solutionStringBuilder.append(t);
    }
    String solutionString = solutionStringBuilder.toString();

    assertTrue(solver.isSolvable());
    assertEquals(4, solver.moves());
    assertEquals(expectedSolution, solutionString);
  }

  @Test
  void unsolveableTest() {
    int blocks[][] = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
    int blocks2[][] = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 15, 14, 0}};
    initialBoard = new Board(blocks);
    Board board2 = new Board(blocks2);
    solver = new Solver(initialBoard);
    Solver solver2 = new Solver(board2);

    assertFalse(solver.isSolvable());
    assertFalse(solver2.isSolvable());
  }

}
