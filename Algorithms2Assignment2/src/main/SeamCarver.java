import edu.princeton.cs.algs4.Picture;

/**
 * Seam-carving is a content-aware image resizing technique where the image is reduced in size by
 * one pixel of height (or width) at a time. For use on Coursera, Algorithms Part II programming
 * assignment.
 */
public class SeamCarver {

  private int width;
  private int height;
  private int[][] pixelColor;
  private boolean horizontalMode = false;
  private boolean horizontalCall = false;

  /**
   * Creates a seam carver object based on the given picture.
   *
   * @param picture input picture.
   * @throws IllegalArgumentException if null argument.
   */
  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException("Cannot have null argument.");
    }

    width = picture.width();
    height = picture.height();
    pixelColor = new int[width][height];

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        pixelColor[col][row] = picture.getRGB(col, row);
      }
    }
  }

  /**
   * Returns current picture.
   */
  public Picture picture() {
    // Transpose pixel matrix back if was previously in horizontal mode
    transposeHandler();

    Picture picture = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        picture.setRGB(col, row, pixelColor[col][row]);
      }
    }

    return new Picture(picture);
  }

  /**
   * Returns width of current picture.
   */
  public int width() {
    if (horizontalMode) {
      return height;
    } else {
      return width;
    }
  }

  /**
   * Returns height of current picture.
   */
  public int height() {
    if (horizontalMode) {
      return width;
    } else {
      return height;
    }
  }

  /**
   * Returns the energy of the pixel at column {@code x} and row {@code y}.
   *
   * @throws IllegalArgumentException if {@code x} is not within range [0, width - 1] or {@code y}
   *                                  is not within range [0, height - 1].
   */
  public double energy(int x, int y) {
    if (horizontalMode) {
      int temp = x;
      x = y;
      y = temp;
    }

    return energyCalculator(x, y);
  }

  private double energyCalculator(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y > height - 1) {
      throw new IllegalArgumentException(
          "Input must pixel must be in range ([0, width - 1], [0, height - 1]).");
    }

    // If border pixel then automatically return 1000
    if ((x == 0) || (x == width - 1) || (y == 0) || (y == height - 1)) {
      return 1000;
    }

    int rgbLeft = pixelColor[x - 1][y];
    int rgbRight = pixelColor[x + 1][y];
    int rgbUp = pixelColor[x][y - 1];
    int rgbDown = pixelColor[x][y + 1];

    double squareXGradient = centralDiffHelper(rgbLeft, rgbRight);
    double squareYGradient = centralDiffHelper(rgbUp, rgbDown);
    return Math.sqrt(squareXGradient + squareYGradient);
  }

  private static double centralDiffHelper(int rgbA, int rgbB) {
    int redA = (rgbA >> 16) & 0xFF;
    int greenA = (rgbA >> 8) & 0xFF;
    int blueA = rgbA & 0xFF;

    int redB = (rgbB >> 16) & 0xFF;
    int greenB = (rgbB >> 8) & 0xFF;
    int blueB = rgbB & 0xFF;

    return (redA - redB) * (redA - redB) + (greenA - greenB) * (greenA - greenB)
        + (blueA - blueB) * (blueA - blueB);
  }

  /**
   * Returns the sequence of indices for the vertical seam to remove.
   */
  public int[] findVerticalSeam() {
    // Transpose input matrix if changing from horizontal to vertical call or vice versa
    transposeHandler();

    int[] seam = new int[height];
    double[][] distTo = new double[width][height];
    int[][] colTo = new int[width][height];

    // Initialise all of top row distTo as 0 and all others as infinity
    for (int row = 1; row < height; row++) {
      for (int col = 0; col < width; col++) {
        distTo[col][row] = Double.POSITIVE_INFINITY;
      }
    }

    // For each row (up to the 2nd to last row). Go through each column in that row and relax the
    // 2-3 pixels in the next row that it is linked to.
    for (int row = 0; row < height - 1; row++) {
      for (int colStart = 0; colStart < width; colStart++) {
        edges:
        for (int colEnd = colStart - 1; colEnd <= colStart + 1; colEnd++) {
          // Ignore columns outside of valid range (border of image)
          if ((colEnd < 0) || (colEnd >= width)) {
            continue edges;
          }

          // Relax edge
          double newDist = distTo[colStart][row] + energyCalculator(colStart, row);
          if (distTo[colEnd][row + 1] > newDist) {
            distTo[colEnd][row + 1] = newDist;
            colTo[colEnd][row + 1] = colStart;
          }
        }
      }
    }

    // Find pixel in final row with lowest distTo value
    double minDistTo = Double.POSITIVE_INFINITY;
    int minCol = 0;
    for (int col = 0; col < width; col++) {
      double currentDistTo = distTo[col][height - 1];
      if (currentDistTo < minDistTo) {
        minDistTo = currentDistTo;
        minCol = col;
      }
    }

    // Work backwards through colTo starting from pixel and add their column numbers to seam
    for (int row = height - 1; row >= 0; row--) {
      seam[row] = minCol;
      minCol = colTo[minCol][row];
    }

    return seam;
  }

  /**
   * Returns the sequence of indices for the horizontal seam to remove.
   */
  public int[] findHorizontalSeam() {
    horizontalCall = true;
    int[] seam = findVerticalSeam();
    horizontalCall = false;

    return seam;
  }

  /**
   * Remove specified vertical seam from current picture.
   *
   * @param seam the vertical seam to be removed.
   * @throws IllegalArgumentException if null argument given.
   * @throws IllegalArgumentException if seam array is of incorrect length.
   * @throws IllegalArgumentException if seam array is not a valid seam (i.e., either an entry is
   *                                  outside its prescribed range or two adjacent entries differ by
   *                                  more than 1).
   * @throws IllegalArgumentException if width of the picture is less than or equal to 1.
   */
  public void removeVerticalSeam(int[] seam) {
    // Transpose input matrix if changing from horizontal to vertical call or vice versa
    transposeHandler();

    if (seam == null) {
      throw new IllegalArgumentException("Null seam not allowed.");
    }

    if (seam.length != height) {
      throw new IllegalArgumentException("Seam length must be the same as height of picture.");
    }

    for (int seamValue : seam) {
      if ((seamValue < 0) || (seamValue >= width)) {
        throw new IllegalArgumentException("Seam values must within range of [0, width - 1].");
      }
    }

    for (int i = 0; i < seam.length - 1; i++) {
      if (Math.abs(seam[i] - seam[i + 1]) > 1) {
        throw new IllegalArgumentException("Adjacent seam values may not differ by more than 1.");
      }
    }

    if (width <= 1) {
      throw new IllegalArgumentException(
          "Cannot remove vertical seam for a picture of width less than 2.");
    }

    // Go through each row of pixelColor array. Starting from each seam column value, move every
    // pixel one space to the left
    for (int row = 0; row < height; row++) {
      for (int col = seam[row]; col < width - 1; col++) {
        pixelColor[col][row] = pixelColor[col + 1][row];
      }
    }

    width--;
  }

  /**
   * Remove specified horizontal seam from current picture.
   *
   * @param seam the horizontal seam to be removed.
   * @throws IllegalArgumentException if null argument given.
   * @throws IllegalArgumentException if seam array is of incorrect length.
   * @throws IllegalArgumentException if seam array is not a valid seam (i.e., either an entry is
   *                                  outside its prescribed range or two adjacent entries differ by
   *                                  more than 1).
   * @throws IllegalArgumentException if height of the picture is less than or equal to 1.
   */
  public void removeHorizontalSeam(int[] seam) {
    horizontalCall = true;
    removeVerticalSeam(seam);
    horizontalCall = false;
  }

  private void transposeHandler() {
    if (horizontalMode ^ horizontalCall) {
      pixelColor = transposeMatrix(pixelColor);
      int temp = width;
      width = height;
      height = temp;

      horizontalMode = !horizontalMode;
    }
  }

  private int[][] transposeMatrix(int[][] matrix) {
    int m = matrix.length;
    int n = matrix[0].length;

    int[][] transposedMatrix = new int[n][m];

    for (int x = 0; x < n; x++) {
      for (int y = 0; y < m; y++) {
        transposedMatrix[x][y] = matrix[y][x];
      }
    }

    return transposedMatrix;
  }
}
