/*
 * String permutation program.
 *
 * Creator: Mark Walbran
 *
 * Date: 23/02/2019
 *
 * Coursera - Algorithms 1 course: Assignment 2
 *
 * This program will take an integer k as a command-line argument, read in a sequence of strings
 * from Standard Input and print exactly k of them, uniformly at random.
 *
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Client program which will take an integer k as a command-line argument, read in a sequence of
 * strings from Standard Input and print exactly k of them, uniformly at random.
 */
public class Permutation {

  /**
   * Client program which will take an integer k as a command-line argument, read in a sequence of
   * strings from Standard Input and print exactly k of them, uniformly at random.
   *
   * <p>Assuming that k is non-negative while being no greater than the number of strings contained
   * in standard input.
   *
   * @param args {@code k} number of strings to print out at random
   */
  public static void main(String[] args) {
    // Confirm that there is 1 integer argument and assign it to k.
    if (args.length != 1) {
      System.err.println("Expected exactly 1 argument.");
      return;
    }

    int k;
    try {
      k = Integer.parseInt(args[0]);
    } catch (NumberFormatException nfe) {
      System.err.println("Argument must be integer.");
      return;
    }

    // Read strings from stdin and add them to a RandomizedQueue.
    RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

    while (!StdIn.isEmpty()) {
      randomizedQueue.enqueue(StdIn.readString());
    }

    for (int i = 0; i < k; i++) {
      StdOut.println(randomizedQueue.dequeue());
    }
  }
}
