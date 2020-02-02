
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Main percolation class that calculates the percolation of the grid.
 */
public class Percolation {

  private final int gridSize;
  private final WeightedQuickUnionUF isFullUnionFind;
  private final WeightedQuickUnionUF percolatesUnionFind;

  private boolean[] openSites;
  private int numberOpenSites = 0;
  private boolean percolates = false;

  /**
   * Creates square grid of size {@code n}, with all sites blocked.
   *
   * <p>Constructor for the this class which will create a 2-D square grid of size
   * {@code n}-by-{@code n}. All elements will be initialized as closed and empty.
   *
   * @param gridSize the dimension of grid to be created
   * @throws IllegalArgumentException unless {@code n > 0}
   */
  public Percolation(int gridSize) {
    if (gridSize <= 0) {
      throw new IllegalArgumentException("Grid size " + gridSize + " is not a positive number");
    }

    // Initialize 2 Union Find objects with n^2 + 1 & n^2 + 2 size (Extra cells to collate top and
    // bottom rows)
    int unionFindSize = gridSize * gridSize + 1;
    isFullUnionFind = new WeightedQuickUnionUF(unionFindSize);
    percolatesUnionFind = new WeightedQuickUnionUF(unionFindSize + 1);

    // Set grid size and create arrays of size n^2+1 for openSites
    this.gridSize = gridSize;
    openSites = new boolean[unionFindSize];

    // Initialize all cells to closed
    for (int i = 0; i < unionFindSize; i++) {
      openSites[i] = false;
    }

    // Connect top row to top "virtual" cell for both WQUF structures
    for (int i = 1; i < gridSize + 1; i++) {
      isFullUnionFind.union(0, i);
      percolatesUnionFind.union(0, i);
    }

    // Connect bottom row to bottom "virtual" cell for percolatesWQUF
    for (int i = unionFindSize - gridSize; i < unionFindSize; i++) {
      percolatesUnionFind.union(unionFindSize, i);
    }
  }

  /**
   * Open site at {@code row}, {@code col} if it is not open already.
   *
   * @param row row number of cell to open
   * @param col column number of cell to open
   * @throws IllegalArgumentException unless {@code (0 < row < gridSize) && (0 < col < gridSize)}
   */
  public void open(int row, int col) {
    validateIndex(row, col);

    // Convert cell to 1D co-ordinates and mark cell as open
    int cell = xyto1D(row, col);

    if (openSites[cell]) {
      return;
    }
    openSites[cell] = true;
    numberOpenSites++;

    // Check all adjacent cells (when they exist) if they are open, and perform
    // union operation if so
    mergeSurrounding(row, col);

    // Check if the top and bottom rows are now connected and set a boolean variable tracking this
    // to true if so
    if (percolatesUnionFind.connected(0, cell)
        && percolatesUnionFind.connected((gridSize * gridSize) + 1, cell)) {
      percolates = true;
    }
  }

  /**
   * Finds if site {@code row}, {@code col} is open.
   *
   * @param row row index of cell to check
   * @param col column index of cell to check
   * @return {@code true} if the cell at {@code row}, {@code col} is open; {@code} false otherwise
   * @throws IllegalArgumentException unless {@code (0 < row < gridSize) && (0 < col < gridSize)}
   */
  public boolean isOpen(int row, int col) {
    validateIndex(row, col);

    int cell = xyto1D(row, col);
    return openSites[cell];
  }

  /**
   * Finds if site {@code row}, {@code col} is full.
   *
   * @param row row index of cell to check
   * @param col column index of cell to check
   * @return {@code true} if the cell at {@code row}, {@code col} is full; {@code} false otherwise
   * @throws IllegalArgumentException unless {@code (0 < row < gridSize) && (0 < col < gridSize)}
   */
  public boolean isFull(int row, int col) {
    validateIndex(row, col);

    // Convert to 1D co-ordinates
    int cell = xyto1D(row, col);

    if (isOpen(row, col)) {
      // If open, check if it is connected to the top cell
      return isFullUnionFind.connected(0, cell);
    } else {
      // If it is closed return false
      return false;
    }
  }

  /**
   * Returns the number of open sites in the grid.
   */
  public int numberOfOpenSites() {
    return numberOpenSites;
  }

  /**
   * Checks if the system percolates.
   *
   * @return {@code true} if the system percolates; {@code false} otherwise
   */
  public boolean percolates() {
    return percolates;
  }

  /**
   * Convert a given x and y co-ordinate to a 1D co-ordinate Cell 0 = Top cell Cells 1 to n are
   * cells of the grid going up like you would read a book (left to right, then top to bottom) Cell
   * n^2+1 = Bottom Cell.
   */
  private int xyto1D(int x, int y) {
    return gridSize * (x - 1) + y;
  }

  /**
   * Validate that row and col are valid indices (between 1 and gridSize).
   */
  private void validateIndex(int row, int col) {
    if (row < 1 || row > gridSize) {
      throw new IllegalArgumentException(
          "row " + row + " is not between valid range of 1 and " + gridSize);
    }

    if (col < 1 || col > gridSize) {
      throw new IllegalArgumentException(
          "column " + col + " is not between valid range of 1 and " + gridSize);
    }
  }

  /**
   * Merge a cell to it's surrounding neighbors if they exist and are open.
   *
   * @param row row index of cell to merge
   * @param col column index of cell to merge
   */
  private void mergeSurrounding(int row, int col) {
    int cell = xyto1D(row, col);

    // Check left cell (if it exists) and perform union operation with it if open
    if (col > 1 && isOpen(row, col - 1)) {
      int surroundingCell = xyto1D(row, col - 1);
      isFullUnionFind.union(cell, surroundingCell);
      percolatesUnionFind.union(cell, surroundingCell);
    }

    // Check above cell (if it exists) and perform union operation with it if open
    if (row > 1 && isOpen(row - 1, col)) {
      int surroundingCell = xyto1D(row - 1, col);
      isFullUnionFind.union(cell, surroundingCell);
      percolatesUnionFind.union(cell, surroundingCell);
    }

    // Check right cell (if it exists) and perform union operation with it if open
    if (col < gridSize && isOpen(row, col + 1)) {
      int surroundingCell = xyto1D(row, col + 1);
      isFullUnionFind.union(cell, surroundingCell);
      percolatesUnionFind.union(cell, surroundingCell);
    }

    // Check below cell (if it exists) and perform union operation with it if open
    if (row < gridSize && isOpen(row + 1, col)) {
      int surroundingCell = xyto1D(row + 1, col);
      isFullUnionFind.union(cell, surroundingCell);
      percolatesUnionFind.union(cell, surroundingCell);
    }
  }
}
