import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Move-to-front encoding will maintain an ordered sequence of the characters in the alphabet by
 * repeatedly reading a character from the input message; printing the position in the sequence in
 * which that character appears; and moving that character to the front of the sequence.
 * <p>
 * A valid input message must be made of 8-bit extended ASCII characters.
 */
public class MoveToFront {

  /**
   * Applies move-to-front encoding, reading from standard input and writing to standard output.
   */
  public static void encode() {
    char[] input = getChars();
    int length = input.length;
    char[] out = new char[length];
    List<Character> moveToFront = IntStream.rangeClosed(0, 255).mapToObj(c -> (char) c)
        .collect(Collectors.toList());

    for (int i = 0; i < length; i++) {
      char inputChar = input[i];
      int pos = moveToFront.indexOf(inputChar);
      out[i] = (char) pos;
      moveToFront.add(0, moveToFront.remove(pos));
    }

    BinaryStdOut.write(new String(out));
    BinaryStdOut.close();
  }

  /**
   * Applies move-to-front decoding, reading from standard input and writing to standard output.
   */
  public static void decode() {
    char[] input = getChars();
    int length = input.length;
    char[] out = new char[length];
    List<Character> moveToFront = IntStream.rangeClosed(0, 255).mapToObj(c -> (char) c)
        .collect(Collectors.toList());

    for (int i = 0; i < length; i++) {
      int pos = input[i];
      char character = moveToFront.remove(pos);
      out[i] = character;
      moveToFront.add(0, character);
    }

    BinaryStdOut.write(new String(out));
    BinaryStdOut.close();
  }

  private static char[] getChars() {
    StringBuilder inputBuilder = new StringBuilder();
    while (!BinaryStdIn.isEmpty()) {
      inputBuilder.append(BinaryStdIn.readChar());
    }

    char[] input = inputBuilder.toString().toCharArray();
    BinaryStdIn.close();
    return input;
  }

  /**
   * Either applies move-to-front encoding, or decoding, depending on the input args.
   *
   * @param args if args[0] is "-", applies encoding, if args[0] is "+", applies decoding.
   */
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      encode();
    } else if (args[0].equals("+")) {
      decode();
    }
  }
}