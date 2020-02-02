
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class PercolationStatsTest {

  private PercolationStats percolationStats;

  @Test
  void testConstructorException() {
    try {
      percolationStats = new PercolationStats(0, 1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  void testConstructor() {
    percolationStats = new PercolationStats(1, 1);
  }


  @Test
  void testMainOneArgumentException() {
    String[] mainArgs = {"200"};
    assertThrows(IllegalArgumentException.class, () -> {
      PercolationStats.main(mainArgs);
    });
  }

  @Test
  void testMainNonIntegerArgumentException() {
    String[] mainArgs = {"200", "c"};
    assertThrows(NumberFormatException.class, () -> {
      PercolationStats.main(mainArgs);
    });
  }

  @Test
  void testMain() {
    String[] mainArgs = {"200", "500"};
    PercolationStats.main(mainArgs);
  }

  @Test
  void testPercolationStatsLargeValues() {
    int testN = 200;
    int testTrials = 100;
    double mean;
    double stddev;
    double confidenceLo;
    double confidenceHi;

    for (int i = 0; i < 5; i++) {
      percolationStats = new PercolationStats(testN, testTrials);
      mean = percolationStats.mean();
      stddev = percolationStats.stddev();
      confidenceLo = percolationStats.confidenceLo();
      confidenceHi = percolationStats.confidenceHi();

      assertEquals(0.5925, mean, 0.003);
      assertEquals(0.0125, stddev, 0.0075);
      assertTrue(confidenceLo > 0.585,
          "confidenceLo was " + confidenceLo + ", below limit of 0.585");
      assertTrue(confidenceLo < mean,
          "confidenceLo was " + confidenceLo + ", above limit (mean) of " + mean);
      assertTrue(confidenceHi < 0.6, "confidenceHi was " + confidenceHi + ", above limit of 0.6");
      assertTrue(confidenceHi > mean,
          "confidenceHi was " + confidenceHi + ", below limit (mean( of " + mean);
    }
  }

  @Test
  void testOneN() {
    int testN = 1;
    int[] testTrials = {10, 100, 1000};
    double mean;
    double stddev;
    double confidenceLo;
    double confidenceHi;

    for (int testTrial : testTrials) {
      percolationStats = new PercolationStats(testN, testTrial);
      mean = percolationStats.mean();
      stddev = percolationStats.stddev();
      confidenceLo = percolationStats.confidenceLo();
      confidenceHi = percolationStats.confidenceHi();

      assertEquals(1.0, mean, 0.000001);
      assertEquals(0.0, stddev, 0.000001);
      assertEquals(1.0, confidenceLo, 0.000001);
      assertEquals(1.0, confidenceHi, 0.000001);
    }
  }

  @Test
  void testOneTrial() {
    int[] testN = {10, 100, 1000};
    int testTrials = 1;
    double mean;
    double stddev;
    double confidenceLo;
    double confidenceHi;

    for (int value : testN) {
      percolationStats = new PercolationStats(value, testTrials);
      mean = percolationStats.mean();
      stddev = percolationStats.stddev();
      confidenceLo = percolationStats.confidenceLo();
      confidenceHi = percolationStats.confidenceHi();

      assertEquals(0.5, mean, 0.5);
      assertTrue(Double.isNaN(stddev));
      assertTrue(Double.isNaN(confidenceLo));
      assertTrue(Double.isNaN(confidenceHi));
    }
  }
}
