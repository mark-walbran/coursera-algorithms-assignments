import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CircularSuffixArrayTest {

  private CircularSuffixArray circularSuffixArray;
  private String testString = "ABRACADABRA!";

  @Test
  void CircularSuffixTest() {
    int[] expectedIndices = {11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2};
    int[] actualIndices = new int[expectedIndices.length];

    circularSuffixArray = new CircularSuffixArray(testString);
    assertEquals(testString.length(), circularSuffixArray.length());

    for (int i = 0; i < testString.length(); i++) {
      actualIndices[i] = circularSuffixArray.index(i);
    }

    assertArrayEquals(expectedIndices, actualIndices,
        "\nExpected: " + Arrays.toString(expectedIndices) +
            "\nBut got: " + Arrays.toString(actualIndices) + "\n");
  }

  @Test
  void CircularCuffixUnaryAlphabetTest() {
    String unaryString = "AAAA";
    int[] expectedIndices = {0, 1, 2, 3};
    int[] actualIndices = new int[expectedIndices.length];

    circularSuffixArray = new CircularSuffixArray(unaryString);
    assertEquals(unaryString.length(), circularSuffixArray.length());

    for (int i = 0; i < unaryString.length(); i++) {
      actualIndices[i] = circularSuffixArray.index(i);
    }

    assertArrayEquals(expectedIndices, actualIndices,
        "\nExpected: " + Arrays.toString(expectedIndices) +
            "\nBut got: " + Arrays.toString(actualIndices) + "\n");

  }

  @Test
  void NullCircularSuffixTest() {
    assertThrows(IllegalArgumentException.class, () -> new CircularSuffixArray(null));
  }

  @Test
  void OutOfRangeIndexTest() {
    circularSuffixArray = new CircularSuffixArray(testString);
    assertThrows(IllegalArgumentException.class,
        () -> circularSuffixArray.index(circularSuffixArray.length()));
    assertThrows(IllegalArgumentException.class, () -> circularSuffixArray.index(-1));
  }
}