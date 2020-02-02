
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PercolationTest {
  private Percolation percolation;
  private int gridSize = 4;

  private void openCells(int[][] cellsToOpen) {
    for (int[] cells : cellsToOpen) {
      percolation.open(cells[0], cells[1]);
    }
  }

  private void correctCellsOpen(int[][] cellsToOpen) {
    int counterOpen = 0;
    for (int i = 1; i <= gridSize; i++) {
      for (int j = 1; j <= gridSize; j++) {
        if ((counterOpen < cellsToOpen.length) && (i == cellsToOpen[counterOpen][0])
            && (j == cellsToOpen[counterOpen][1])) {
          assertTrue(percolation.isOpen(i, j));
          counterOpen++;
        } else {
          assertFalse(percolation.isOpen(i, j));
        }
      }
    }
  }

  private void correctCellsFull(int[][] cellsExpectedFull) {
    int counterFull = 0;
    for (int i = 1; i <= gridSize; i++) {
      for (int j = 1; j <= gridSize; j++) {
        if ((counterFull < cellsExpectedFull.length) && (i == cellsExpectedFull[counterFull][0])
            && (j == cellsExpectedFull[counterFull][1])) {
          assertTrue(percolation.isFull(i, j));
          counterFull++;
        } else {
          assertFalse(percolation.isFull(i, j));
        }
      }
    }
  }

  @Test
  void testConstructorOutOfRangeException() {
    assertThrows(IllegalArgumentException.class, () -> {
      percolation = new Percolation(0);
    });
  }

  @Test
  void testConstructor() {
    percolation = new Percolation(1);
  }

  @Test
  void testOpenOutOfRangeException() {
    percolation = new Percolation(4);
    assertThrows(IllegalArgumentException.class, () -> {
      percolation.open(5, 5);
    });
  }

  @Test
  void testOpen() {
    percolation = new Percolation(4);
    percolation.open(4, 4);
  }

  @Test
  void testIsOpenOutOfRangeException() {
    percolation = new Percolation(4);
    assertThrows(IllegalArgumentException.class, () -> {
      percolation.isOpen(5, 4);
    });
  }

  @Test
  void testIsOpen() {
    percolation = new Percolation(4);
    percolation.isOpen(1, 1);

  }

  @Test
  void testIsFullOutOfRangeException() {
    percolation = new Percolation(4);
    assertThrows(IllegalArgumentException.class, () -> {
      percolation.isFull(0, 1);
    });
  }

  @Test
  void testIsFull() {
    percolation = new Percolation(4);
    percolation.isFull(4, 4);

  }

  @Test
  void testGridSizeOne() {
    percolation = new Percolation(1);
    assertFalse(percolation.percolates());

    percolation.open(1, 1);
    assertTrue(percolation.percolates());
  }

  @Test
  void testPercolation() {
    percolation = new Percolation(4);

    for (int i = 1; i <= gridSize; i++) {
      for (int j = 1; j <= gridSize; j++) {
        assertFalse(percolation.isOpen(i, j));
        assertFalse(percolation.isFull(i, j));
      }
    }

    assertEquals(0, percolation.numberOfOpenSites());
    assertFalse(percolation.percolates());

  }

  @Test
  void testOpenDoesntPercolate() {
    percolation = new Percolation(4);

    int[][] cellsToOpen = {{1, 2}, {1, 3}, {2, 3}, {3, 1}, {3, 4}, {4, 4}};
    int[][] cellsExpectedFull = {{1, 2}, {1, 3}, {2, 3}};

    openCells(cellsToOpen);

    correctCellsOpen(cellsToOpen);

    correctCellsFull(cellsExpectedFull);

    assertEquals(cellsToOpen.length, percolation.numberOfOpenSites());
    assertFalse(percolation.percolates());
  }

  @Test
  void testOpenPercolates() {
    percolation = new Percolation(4);

    int[][] cellsToOpen = {{1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 1}, {3, 4}, {4, 4}};
    int[][] cellsExpectedFull = {{1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 4}, {4, 4}};

    openCells(cellsToOpen);

    correctCellsOpen(cellsToOpen);

    correctCellsFull(cellsExpectedFull);

    assertEquals(cellsToOpen.length, percolation.numberOfOpenSites());
    assertTrue(percolation.percolates());
  }

  @Test
  void testOpenPercolatesAndNoBackwash() {
    percolation = new Percolation(4);

    int[][] cellsToOpen = {{1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 1}, {3, 4}, {4, 2}, {4, 4}};
    int[][] cellsExpectedFull = {{1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 4}, {4, 4}};

    openCells(cellsToOpen);

    correctCellsOpen(cellsToOpen);

    correctCellsFull(cellsExpectedFull);

    assertEquals(cellsToOpen.length, percolation.numberOfOpenSites());
    assertTrue(percolation.percolates());
  }
}
