import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class PermutationTest {

  @Test
  void testMain() {
    String filename = "test-input/duplicates.txt";
    String k = "8";
    try {
      System.setIn(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("No file");
    }

    String[] mainArgs = {k};
    Permutation.main(mainArgs);
  }
}
