import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;
import java.util.stream.IntStream;

/**
 * Circular suffix array, which describes the abstraction of a sorted array of the n circular
 * suffixes of a string of length n.
 */
public class CircularSuffixArray {

  private static final int ALPHABET_SIZE = 256;
  private static final int INSERTION_SORT_CUTOFF = 15;

  private final int[] index;
  private final String inputString;
  private final int length;
  private CircularSuffix[] circularSuffixes;

  private class CircularSuffix {

    private final int index;

    CircularSuffix(int index) {
      this.index = index;
    }

    char charAt(int pos) {
      return inputString.charAt((pos + index) % length);
    }
  }

  private static class MSDVals {

    int lo;
    int hi;
    int pos;

    MSDVals(int lo, int hi, int pos) {
      this.lo = lo;
      this.hi = hi;
      this.pos = pos;
    }
  }

  /**
   * Create a circular suffix array from the input string.
   *
   * @param inputString the string to create the circular suffix array from
   * @throws IllegalArgumentException if {@code s} is {@code null}
   */
  public CircularSuffixArray(String inputString) {
    if (inputString == null) {
      throw new IllegalArgumentException("Input must not be null");
    }

    this.inputString = inputString;
    this.length = inputString.length();
    this.index = IntStream.range(0, length).toArray();
    circularSuffixes = new CircularSuffix[length];
    for (int i = 0; i < length; i++) {
      circularSuffixes[i] = new CircularSuffix(i);
    }

    msdSort();
  }

  private void msdSort() {
    CircularSuffix[] aux = new CircularSuffix[length];
    int[] auxIndex = new int[length];

    Stack<MSDVals> stack = new Stack<>();
    stack.push(new MSDVals(0, length - 1, 0));
    int pos = 0;
    int lo;
    int hi;

    while (!stack.isEmpty() && pos < length) {
      MSDVals msdVals = stack.pop();
      lo = msdVals.lo;
      hi = msdVals.hi;
      pos = msdVals.pos;

      if (hi <= (lo + INSERTION_SORT_CUTOFF)) {
        insertionSort(lo, hi, pos);
        continue;
      }

      int[] count = new int[ALPHABET_SIZE + 2];

      // Count frequencies of each letter using key as index.
      for (int i = lo; i <= hi; i++) {
        count[circularSuffixes[i].charAt(pos) + 2]++;
      }

      // Compute frequency cumulates which specify destinations.
      for (int r = 0; r <= ALPHABET_SIZE; r++) {
        count[r + 1] += count[r];
      }

      // Access cumulates using key as index to move items.
      for (int i = lo; i <= hi; i++) {
        int auxPos = count[circularSuffixes[i].charAt(pos) + 1]++;
        aux[auxPos] = circularSuffixes[i];
        auxIndex[auxPos] = index[i];
      }

      // Copy back into original array
      for (int i = lo; i <= hi; i++) {
        int auxPos = i - lo;
        circularSuffixes[i] = aux[auxPos];
        index[i] = auxIndex[auxPos];
      }

      // Sort alphabetSize subarray's recursively
      for (int r = 0; r < ALPHABET_SIZE; r++) {
        stack.push(new MSDVals(lo + count[r], lo + count[r + 1] - 1, pos + 1));
      }
    }
  }

  private void insertionSort(int lo, int hi, int pos) {
      for (int i = lo; i <= hi; i++) {
          for (int j = i; j > lo && less(circularSuffixes[j], circularSuffixes[j - 1], pos); j--) {
              exchange(j, j - 1);
          }
      }
  }

  private boolean less(CircularSuffix v, CircularSuffix w, int pos) {
    for (int i = pos; i < length; i++) {
      if (v.charAt(i) < w.charAt((i))) {
        return true;
      } else if (v.charAt(i) > w.charAt(i)) {
        return false;
      }
    }

    return false;
  }

  private void exchange(int a, int b) {
    CircularSuffix tempCircularSuffix = circularSuffixes[a];
    int tempIndex = index[a];

    circularSuffixes[a] = circularSuffixes[b];
    index[a] = index[b];

    circularSuffixes[b] = tempCircularSuffix;
    index[b] = tempIndex;
  }

  /**
   * Returns the length of the string
   */
  public int length() {
    return length;
  }

  /**
   * Returns index of ith sorted suffix.
   * <p>
   * <p>
   * Index[i] is the index of the original suffix that appears ith in the sorted array.
   *
   * @param i the index
   * @throws IllegalArgumentException if {@code i} is outside its prescribed range (between 0 and n
   *                                  − 1)
   */
  public int index(int i) {
    if (i < 0 || i >= length) {
      throw new IllegalArgumentException("Index i must be between 0 and " + (length - 1));
    }

    return index[i];
  }

  /**
   * As per the assignment specification, the main() method is required to call each public method
   * directly and help verify that they work as prescribed by printing results to standard output.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    String testString = args[0];
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(testString);
    int[] indices = new int[testString.length()];

    for (int i = 0; i < testString.length(); i++) {
      indices[i] = circularSuffixArray.index(i);
    }

    System.out.println("Index array: " + Arrays.toString(indices));
    System.out.println("Length: " + circularSuffixArray.length());
  }
}