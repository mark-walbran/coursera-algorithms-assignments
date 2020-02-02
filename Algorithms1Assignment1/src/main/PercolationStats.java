

/**
 * Designed to create run a set number of trials regarding percolation threshold and print
 * statistical information about the results. Each trial will involve creating a blocked grid,
 * opening new cells at random until the system first percolates and then recording the percentage
 * of open cells when this happens
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;

  private double mean;
  private double stddev;
  private double confidenceLo;
  private double confidenceHi;

  /**
   * Creates the percolationStats object and calculates the percolation threshold statistics for a
   * specified number of different trials.
   *
   * @param gridSize length dimension of square grid
   * @param trials   number of trials to perform
   * @throws IllegalArgumentException unless {@code n > 0} & {@code trials > 0}
   */
  public PercolationStats(int gridSize, int trials) {
    if (gridSize <= 0) {
      throw new IllegalArgumentException("n " + gridSize + " is not a positive number");
    }

    if (trials <= 0) {
      throw new IllegalArgumentException("trials " + trials + " is not a positive number");
    }

    runTrials(gridSize, trials);
  }


  /**
   * Returns sample mean of percolation threshold.
   */
  public double mean() {
    return mean;
  }

  /**
   * Returns sample standard deviation of percolation threshold (NaN if n = 1).
   */
  public double stddev() {
    return stddev;
  }

  /**
   * Returns low endpoint of 95% confidence interval for percolation threshold.
   */
  public double confidenceLo() {
    return confidenceLo;
  }

  /**
   * Returns high endpoint of 95% confidence interval for percolation threshold.
   */
  public double confidenceHi() {
    return confidenceHi;
  }

  /**
   * Main function of program, returns statistics about estimated percolation threshold for a grid
   * of size {@code n}.
   *
   * <p>Takes input of grid size {@code n} and number of trials {@code trials} and prints out
   * statistical data for percolation threshold based on {@code trials} number of simulations done
   * for a grid of size {@code n}-by-{@code n}.
   *
   * @param args {@code n} dimension of square grid {@code trials} number of trials to perform
   */
  public static void main(String[] args) {
    // Confirm that there are 2 integer arguments and assign then to n and trials respectively
    if (args.length != 2) {
      System.err.println("Must have 2 arguments");
      throw new IllegalArgumentException("number of arguments " + args.length + " is not 2.");
    }

    int gridSize;
    int trialsLocal;
    try {
      gridSize = Integer.parseInt(args[0]);
      trialsLocal = Integer.parseInt(args[1]);
    } catch (NumberFormatException nfe) {
      System.err.println("Both arguments must be integers.");
      throw nfe;
    }

    // Create new percolationStats object with arguments
    PercolationStats percolationStats = new PercolationStats(gridSize, trialsLocal);

    // Print out information to user
    System.out.println("mean                    = " + percolationStats.mean());
    System.out.println("stddev                  = " + percolationStats.stddev());
    System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
        + percolationStats.confidenceHi() + "]");
  }

  /**
   * Perform a set trial number of trials on percolation threshold given a grid of specified n-by-n
   * size.
   *
   * <p>Each trial will involves creating a blocked grid, opening new cells until the system first
   * percolates, and then recording the percentage of open cells when this happens.
   *
   * @param gridSize size of grid to calculate statistics for
   * @param trials   number of trials to run on grid
   */
  private void runTrials(int gridSize, int trials) {
    double[] percolationThreshold = new double[trials];

    // Perform percolation "trials" number of times
    for (int i = 0; i < trials; i++) {
      // Initialize fully closed grid of size n
      Percolation percolation = new Percolation(gridSize);

      // Keep opening new random cells until system percolates
      while (!percolation.percolates()) {
        int row;
        int col;
        // Choose site randomly among all blocked sites
        // Keep choosing new random cell until a closed one is found
        do {
          row = StdRandom.uniform(gridSize) + 1;
          col = StdRandom.uniform(gridSize) + 1;
        } while (percolation.isOpen(row, col));

        percolation.open(row, col);
      }

      // Record number of open cells for trial # "trials"
      percolationThreshold[i] = percolation.numberOfOpenSites();
    }

    // Divide percolation threshold by number of cells to determine percentage open at first point
    // of percolation
    for (int i = 0; i < percolationThreshold.length; i++) {
      percolationThreshold[i] /= (gridSize * gridSize);
    }

    // Calculate statistical values
    mean = StdStats.mean(percolationThreshold);
    if (trials == 1) {
      stddev = Double.NaN;
    } else {
      stddev = StdStats.stddev(percolationThreshold);
    }
    confidenceLo = mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
    confidenceHi = mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
  }
}
