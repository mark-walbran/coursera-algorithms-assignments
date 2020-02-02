import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BurrowsWheelerTest {

  @ParameterizedTest
  @ValueSource(strings = {"abra.txt", "aaaaa.txt", "aesop.txt", "rand10K.bin", "us.gif"})
  void TransformTest(String inputFilename) throws IOException {
    // Read in text & jpg file and assert output is text file in bwt/
    final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(myOut));

    byte[] inputBytes = Files.readAllBytes(Paths.get("test-input/" + inputFilename));
    System.setIn(new ByteArrayInputStream(inputBytes));

    byte[] expectedResult = Files
        .readAllBytes(Paths.get("test-input/bwt/" + inputFilename + ".bwt"));

    BurrowsWheeler.transform();
    byte[] actualResult = myOut.toByteArray();

    assertArrayEquals(expectedResult, actualResult,
        "\nExpected: " + Arrays.toString(expectedResult) +
            "\nBut got: " + Arrays.toString(actualResult) + "\n");
  }

  @ParameterizedTest
  @ValueSource(strings = {"abra.txt", "aaaaa.txt", "aesop.txt", "rand10K.bin", "us.gif"})
  void InverseTransformTest(String inputFilename) throws IOException {
    // Read in text & jpg file from bwt/ and assert output is text file in parent folder
    final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(myOut));
    byte[] inputBytes = Files
        .readAllBytes(Paths.get("test-input/bwt/" + inputFilename + ".bwt"));
    System.setIn(new ByteArrayInputStream(inputBytes));

    byte[] expectedResult = Files.readAllBytes(Paths.get("test-input/" + inputFilename));

    BurrowsWheeler.inverseTransform();
    byte[] actualResult = myOut.toByteArray();

    assertArrayEquals(expectedResult, actualResult,
        "\nExpected: " + new String(expectedResult) +
            "\nBut got: " + new String(actualResult) + "\n");
  }

  @ParameterizedTest
  @ValueSource(strings = {"abra.txt", "aaaaa.txt", "aesop.txt", "rand10K.bin", "us.gif"})
  void TransformThenInverseTransformTest(String inputFilename) throws IOException {
    // Assert that the inverse transform of the transform of any input is equal to the original input

    final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(myOut));

    byte[] inputBytes = Files.readAllBytes(Paths.get("test-input/" + inputFilename));
    System.setIn(new ByteArrayInputStream(inputBytes));

    BurrowsWheeler.transform();
    byte[] transformedBytes = myOut.toByteArray();
    myOut.reset();

    System.setOut(new PrintStream(myOut));
    System.setIn(new ByteArrayInputStream(transformedBytes));
    BurrowsWheeler.inverseTransform();
    byte[] inverseTransformedBytes = myOut.toByteArray();

    assertArrayEquals(inputBytes, inverseTransformedBytes,
        "\nExpected: " + new String(inputBytes) +
            "\nBut got: " + new String(inverseTransformedBytes) + "\n");
  }

  @ParameterizedTest
  @CsvSource({"-, abra.txt", "+, abra.txt", "-, aaaaa.txt", "+, aaaaa.txt", "-, aesop.txt",
      "+, aesop.txt", "-, rand10K.bin", "+, rand10K.bin", "-, us.gif", "+, us.gif"})
  void MainTest(String op, String inputFilename) throws IOException {
    final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(myOut));

    byte[] original = Files.readAllBytes(Paths.get("test-input/" + inputFilename));
    byte[] transformed = Files
        .readAllBytes(Paths.get("test-input/bwt/" + inputFilename + ".bwt"));
    byte[] inputBytes = op.equals("-") ? original : transformed;
    byte[] expectedResult = op.equals("-") ? transformed : original;

    System.setIn(new ByteArrayInputStream(inputBytes));
    String[] args = {op};
    BurrowsWheeler.main(args);
    byte[] actualResult = myOut.toByteArray();

    assertArrayEquals(expectedResult, actualResult,
        "\nExpected: " + Arrays.toString(expectedResult) +
            "\nBut got: " + Arrays.toString(actualResult) + "\n");
  }
}