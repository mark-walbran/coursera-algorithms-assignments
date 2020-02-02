import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;

/**
 * A board layout of the n-size generalisation of the "8-puzzle". For use on Coursera, Algorithms
 * Part I programming assignment.
 */

public class Board {

  private final char[] board;
  private final int dimension;
  private final int size;
  private int manhattanDistance;
  private boolean manhattanDistanceFlag = false;

  /**
   * Constructs a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column
   * j).
   * <p>
   *
   * @param blocks an n-by-n array containing the n^2 integers between 0 and n^2-1, where 0
   *               represents the blank square.
   * @throws IllegalArgumentException if the argument to the constructor is not an n-by-n array
   *                                  containing all integers between 0 and n^2-1.
   */
  public Board(int[][] blocks) {
    dimension = blocks.length;
    size = (int) Math.pow(dimension, 2);

    for (int i = 0; i < dimension; i++) {
      if (blocks[i].length != dimension) {
        throw new IllegalArgumentException("Input must be an n-by-n square array.");
      }
    }

    int[] numberCount = new int[size];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if ((blocks[i][j] > (size - 1)) || (blocks[i][j] < 0)) {
          throw new IllegalArgumentException(
              "Input array must not contain any integer outside of the range 0 to n^2 - 1.");
        }

        if (numberCount[blocks[i][j]] > 0) {
          throw new IllegalArgumentException(
              "Input array must contain all integers from 0 to n^2 - 1.");
        }
        numberCount[blocks[i][j]]++;

      }
    }

    board = new char[size];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        board[dimension * i + j] = (char) blocks[i][j];
      }
    }
  }

  /**
   * Returns the board dimension n.
   */
  public int dimension() {
    return dimension;
  }

  /**
   * Returns the number of blocks out of place.
   */
  public int hamming() {
    int hammingDistance = 0;

    for (int i = 0; i < size - 1; i++) {
      if (board[i] != i + 1) {
        hammingDistance++;
      }
    }

    return hammingDistance;
  }

  /**
   * Returns the sum of Manhattan distances between blocks and goal.
   */
  public int manhattan() {
    if (manhattanDistanceFlag) {
      return manhattanDistance;
    }

    for (int i = 0; i < size; i++) {
      if (board[i] != i + 1) {
        int correctPos;
        if (board[i] != 0) {
          correctPos = board[i] - 1;
          manhattanDistance += manhattanDist(i, correctPos);
        }
      }
    }

    manhattanDistanceFlag = true;
    return manhattanDistance;
  }

  /**
   * Is this board the goal board?
   */
  public boolean isGoal() {
    for (int i = 0; i < size - 1; i++) {
      if (board[i] != i + 1) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a "twin" of the board (a board that is obtained by exchanging any pair of blocks).
   */
  public Board twin() {
    char[] twinBoard = board.clone();
    if (twinBoard[0] != 0) {
      if (twinBoard[1] != 0) {
        // If no zeros at 0, 0 or 0, 1 then swap them
        swap(twinBoard, 0, 1);
      } else {
        // If zero at 0, 1 then swap 0, 0 and 1, 0
        swap(twinBoard, 0, dimension);
      }
    } else {
      // If zero at 0, 0 then swap 1, 0 and 1, 1
      swap(twinBoard, 1, dimension + 1);
    }

    return charArrayToBoard(twinBoard);
  }

  /**
   * Does this board equal y?
   */
  @Override
  public boolean equals(Object y) {
    // Copied from Date.java class
    if (y == this) {
      return true;
    }
    if (y == null) {
      return false;
    }
    if (y.getClass() != this.getClass()) {
      return false;
    }

    Board that = (Board) y;
    return Arrays.equals(this.board, that.board);
  }

  /**
   * Returns an iterator for all neighbouring boards.
   */
  public Iterable<Board> neighbors() {
    Stack<Board> neighbours = new Stack<>();

    // Find position of zero
    int zeroPos = 0;
    for (int i = 0; i < size; i++) {
      if (board[i] == 0) {
        zeroPos = i;
      }
    }

    // For all 4 possible linear movement directions, confirm that direction is not an edge. Swap
    // zero and the number in that direction and add that board to the stack if not
    if (zeroPos % dimension != dimension - 1) { // Right
      int numberPos = zeroPos + 1;
      Board neighbourBoard = createNeighbourBoard(zeroPos, numberPos);
      neighbours.push(neighbourBoard);
    }

    if (zeroPos / dimension != dimension - 1) { // Down
      int numberPos = zeroPos + dimension;
      Board neighbourBoard = createNeighbourBoard(zeroPos, numberPos);
      neighbours.push(neighbourBoard);
    }

    if (zeroPos % dimension != 0) { // Left
      int numberPos = zeroPos - 1;
      Board neighbourBoard = createNeighbourBoard(zeroPos, numberPos);
      neighbours.push(neighbourBoard);
    }

    if (zeroPos / dimension != 0) { // Up
      int numberPos = zeroPos - dimension;
      Board neighbourBoard = createNeighbourBoard(zeroPos, numberPos);
      neighbours.push(neighbourBoard);
    }

    return neighbours;
  }

  /**
   * Returns a string representation of this board (in the output format specified in assignment
   * decription).
   */
  @Override
  public String toString() { //
    // Example string builder from assignment description (will vary based on choice of instance
    // variables).

    StringBuilder s = new StringBuilder();
    s.append(dimension).append("\n");
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        s.append(String.format("%2d ", (int) board[dimension * i + j]));
      }
      s.append("\n");
    }
    return s.toString();
  }

  /**
   * Returns the "manhattan distance" (sum of vertical and horizontal distances) between 2 positions
   * on the board.
   *
   * @param posA the first position
   * @param posB the second position
   */
  private int manhattanDist(int posA, int posB) {
    int dist = Math.abs(posA - posB);
    int posMin = Math.min(posA, posB);
    int manhattanDist;

    if (dist % dimension + posMin % dimension < dimension) {
      manhattanDist = dist / dimension + dist % dimension;
    } else {
      manhattanDist = (dist / dimension + 1) + (dimension - dist % dimension);
    }

    return manhattanDist;
  }

  /**
   * Swaps 2 numbers on the board with each other.
   *
   * @param posA the index of the first number.
   * @param posB the index of the second number.
   */
  private char[] swap(char[] swapBoard, int posA, int posB) {
    char temp = swapBoard[posA];
    swapBoard[posA] = swapBoard[posB];
    swapBoard[posB] = temp;

    return swapBoard;
  }


  /**
   * Convert 1D array of chars Board and return.
   */
  private Board charArrayToBoard(char[] charArray) {
    int[][] blocks = new int[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        blocks[i][j] = charArray[dimension * i + j];
      }
    }

    return new Board(blocks);
  }

  private void setManhattan(int dist) {
    this.manhattanDistance = dist;
    manhattanDistanceFlag = true;
  }

  private Board createNeighbourBoard(int zeroPos, int numberPos) {
    char[] neighbourBlocks = swap(board.clone(), zeroPos, numberPos);
    Board neighbourBoard = charArrayToBoard(neighbourBlocks);
    int correctPos = board[numberPos] - 1;
    int neighbourManhattanDist =
        manhattan() - manhattanDist(correctPos, numberPos) + manhattanDist(correctPos, zeroPos);
    neighbourBoard.setManhattan(neighbourManhattanDist);
    return neighbourBoard;
  }
}
