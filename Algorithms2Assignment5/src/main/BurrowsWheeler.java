import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * The Burrows–Wheeler transform transforms a message into a form that is more amenable for
 * compression. The Burrows–Wheeler transform rearranges the characters in the input so that there
 * are lots of clusters with repeated characters, but in such a way that it is still possible to
 * recover the original input.
 */
public class BurrowsWheeler {

  private static final int ALPHABET_SIZE = 256;

  private static class ReturnVal {

    int[] next;
    char[] tSorted;

    ReturnVal(int[] next, char[] tSorted) {
      this.next = next;
      this.tSorted = tSorted;
    }
  }

  /**
   * Applies Burrows-Wheeler transform, reading from standard input and writing to standard output.
   */
  public static void transform() {
    String string = BinaryStdIn.readString();
    BinaryStdIn.close();

    char[] input = string.toCharArray();
    int length = input.length;
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(string);
    int index;
    int first = -1;
    char[] output = new char[length];

    for (int i = 0; i < length; i++) {
      index = circularSuffixArray.index(i);

      if (index == 0) {
        first = i;
        output[i] = input[length - 1];
      } else {
        output[i] = input[index - 1];
      }
    }

    if (first == -1) {
      throw new RuntimeException("First not found.");
    }

    BinaryStdOut.write(first);
    BinaryStdOut.write(new String(output));
    BinaryStdOut.close();
  }

  /**
   * Applies Burrows-Wheeler inverse transform, reading from standard input and writing to standard
   * output.
   * <p>
   * Assumes that the input is valid (i.e. created by a call to transform())
   */
  public static void inverseTransform() {
    int first = BinaryStdIn.readInt();
    char[] t = BinaryStdIn.readString().toCharArray();
    BinaryStdIn.close();

    ReturnVal returnVal = keyIndexedCountingSort(t);
    char[] output = new char[t.length];

    int i = first;
    for (int a = 0; a < returnVal.next.length; a++) {
      output[a] = returnVal.tSorted[i];
      i = returnVal.next[i];
    }

    BinaryStdOut.write(new String(output));
    BinaryStdOut.close();
  }

  private static ReturnVal keyIndexedCountingSort(char[] array) {
    int length = array.length;
    char[] arraySorted = new char[length];
    int[] next = new int[length];
    int[] count = new int[ALPHABET_SIZE + 1];

    // Count frequencies of each letter using key as index.
    for (char c : array) {
      count[c + 1]++;
    }

    // Compute frequency cumulates which specify destinations.
    for (int r = 0; r < ALPHABET_SIZE; r++) {
      count[r + 1] += count[r];
    }

    // Access cumulates using key as index to move items.
    for (int i = 0; i < length; i++) {
      char c = array[i];
      arraySorted[count[c]] = c;
      next[count[c]++] = i;
    }

    return new ReturnVal(next, arraySorted);
  }

  /**
   * Either applies Burrows-Wheeler transform, or inverse transform, depending on the input args.
   *
   * @param args if args[0] is "-", applies transform, if args[0] is "+", applies reverse
   *             transform.
   */
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      transform();
    } else if (args[0].equals("+")) {
      inverseTransform();
    }
  }
}