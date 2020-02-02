import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

class BoardTest {

  private Board board;

  @Test
  void testConstructorNonSquareException() {
    int blocks[][] = {{1, 2, 3}, {4, 5}, {6, 7, 8}};
    int blocks2[][] = {{1, 2, 3}, {4, 5, 6}};

    assertThrows(IllegalArgumentException.class, () -> {
      board = new Board(blocks);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      board = new Board(blocks2);
    });
  }

  @Test
  void testConstructorMissingNumbersException() {
    int blocks[][] = {{1, 2, 3}, {4, 4, 5}, {6, 7, 8}};
    int blocks2[][] = {{1, 2, 3}, {4, 5, 6}, {7, 9, 0}};

    assertThrows(IllegalArgumentException.class, () -> {
      board = new Board(blocks);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      board = new Board(blocks2);
    });
  }

  @Test
  void test3x3BoardNonGoal() {
    int blocks[][] = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

    board = new Board(blocks);

    assertEquals(3, board.dimension());
    assertEquals(5, board.hamming());
    assertEquals(10, board.manhattan());
    assertFalse(board.isGoal());
  }

  @Test
  void test4x4BoardGoal() {
    int blocks[][] = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};

    board = new Board(blocks);

    assertEquals(4, board.dimension());
    assertEquals(0, board.hamming());
    assertEquals(0, board.manhattan());
    assertTrue(board.isGoal());
  }

  @Test
  void testEquals() {
    int blocks[][] = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
    int blocks2[][] = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
    int blocks3[][] = {{8, 1, 3}, {4, 0, 2}, {7, 5, 6}};

    board = new Board(blocks);
    Board board2 = new Board(blocks2);
    Board board3 = new Board(blocks3);

    assertTrue(board.equals(board));
    assertTrue(board.equals(board2));
    assertFalse(board.equals(board3));
    assertFalse(board.equals(blocks2));
    assertFalse(board.equals(null));
  }

  @Test
  void testToString() {
    int blocks[][] = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};

    board = new Board(blocks);

    String expectedString = "3\n" + " 0  1  3 \n" + " 4  2  5 \n" + " 7  8  6 \n";
    assertEquals(expectedString, board.toString());
  }

  @Test
  void testTwin() {
    int blocks[][] = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
    int blocks2[][] = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    int blocks3[][] = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
    int expectedBlocks[][] = {{1, 8, 3}, {4, 0, 2}, {7, 6, 5}};
    int expectedBlocks2[][] = {{0, 2, 3}, {4, 1, 5}, {7, 8, 6}};
    int expectedBlocks3[][] = {{4, 0, 3}, {1, 2, 5}, {7, 8, 6}};

    board = new Board(blocks);
    Board board2 = new Board(blocks2);
    Board board3 = new Board(blocks3);
    Board expectedTwin = new Board(expectedBlocks);
    Board expectedTwin2 = new Board(expectedBlocks2);
    Board expectedTwin3 = new Board(expectedBlocks3);

    assertEquals(expectedTwin, board.twin());
    assertEquals(expectedTwin2, board2.twin());
    assertEquals(expectedTwin3, board3.twin());
  }

  @Test
  void testNeighbors() {
    int blocks[][] = {{1, 2, 3}, {4, 0, 5}, {6, 7, 8}};
    int blocks2[][] = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    int blocks3[][] = {{1, 2, 3, 4}, {5, 6, 7, 0}, {8, 9, 10, 11}, {12, 13, 14, 15}};
    int blocks4[][] = {{1, 2, 3, 4}, {5, 6, 7, 14}, {8, 9, 10, 11}, {12, 13, 0, 15}};

    Board[] boards =
        {new Board(blocks), new Board(blocks2), new Board(blocks3), new Board(blocks4)};

    Iterable<Board> neighbors = boards[0].neighbors();
    Iterable<Board> neighbors2 = boards[1].neighbors();
    Iterable<Board> neighbors3 = boards[2].neighbors();
    Iterable<Board> neighbors4 = boards[3].neighbors();

    Board[][] neighborsOut = new Board[4][4];
    int i = 0;
    for (Board s : neighbors) {
      neighborsOut[0][i] = s;
      i++;
    }

    i = 0;
    for (Board s : neighbors2) {
      neighborsOut[1][i] = s;
      i++;
    }

    i = 0;
    for (Board s : neighbors3) {
      neighborsOut[2][i] = s;
      i++;
    }

    i = 0;
    for (Board s : neighbors4) {
      neighborsOut[3][i] = s;
      i++;
    }

    int[][][][] expectedOut = {
        {{{1, 0, 3}, {4, 2, 5}, {6, 7, 8}}, {{1, 2, 3}, {0, 4, 5}, {6, 7, 8}},
            {{1, 2, 3}, {4, 7, 5}, {6, 0, 8}}, {{1, 2, 3}, {4, 5, 0}, {6, 7, 8}}},
        {{{4, 1, 3}, {0, 2, 5}, {7, 8, 6}}, {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}}, null, null},
        {{{1, 2, 3, 0}, {5, 6, 7, 4}, {8, 9, 10, 11}, {12, 13, 14, 15}},
            {{1, 2, 3, 4}, {5, 6, 0, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}},
            {{1, 2, 3, 4}, {5, 6, 7, 11}, {8, 9, 10, 0}, {12, 13, 14, 15}}, null},
        {{{1, 2, 3, 4}, {5, 6, 7, 14}, {8, 9, 0, 11}, {12, 13, 10, 15}},
            {{1, 2, 3, 4}, {5, 6, 7, 14}, {8, 9, 10, 11}, {12, 0, 13, 15}},
            {{1, 2, 3, 4}, {5, 6, 7, 14}, {8, 9, 10, 11}, {12, 13, 15, 0}}, null}};

    Board[][] neighborsOutExpected = new Board[4][4];
    for (i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (expectedOut[i][j] != null) {
          neighborsOutExpected[i][j] = new Board(expectedOut[i][j]);
        }
      }
    }

    for (i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        assertEquals(neighborsOutExpected[i][j], neighborsOut[i][j]);
      }
    }

    assertEquals(6, boards[0].manhattan());
    Iterator<Board> iterator = neighbors.iterator();
    assertEquals(7, (iterator.next()).manhattan());
    assertEquals(7, (iterator.next()).manhattan());
    assertEquals(7, (iterator.next()).manhattan());
    assertEquals(5, (iterator.next()).manhattan());
  }
}
