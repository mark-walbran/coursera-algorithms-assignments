import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Common unit tests for implementations of {@link CollinearPoints}.
 */
abstract class CollinearPointsTest {

  CollinearPoints testInstance;

  abstract CollinearPoints getInstance();

  @BeforeEach
  private void setUp() {
    testInstance = getInstance();
  }

  @Test
  void testNullArgument() {
    assertThrows(IllegalArgumentException.class, () -> {
      testInstance.findLineSegments(null);
    });
  }

  @Test
  void testNullPoint() {
    Point[] points = new Point[3];
    points[0] = new Point(1, 2);
    points[1] = null;
    points[2] = new Point(3, 4);

    assertThrows(IllegalArgumentException.class, () -> {
      testInstance.findLineSegments(points);
    });
  }

  @Test
  void testDuplicatePoint() {
    int[][] pointCoords = {{1, 2}, {3, 4}, {3, 4}, {5, 6}};

    Point[] points = new Point[pointCoords.length];

    for (int i = 0; i < pointCoords.length; i++) {
      points[i] = new Point(pointCoords[i][0], pointCoords[i][1]);
    }

    assertThrows(IllegalArgumentException.class, () -> {
      testInstance.findLineSegments(points);
    });
  }

  @Test
  void testCollinearPointsMaxLength4() {
    int[][] pointCoords = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {3, 2}, {4, 2}, {5, 2}, {2, 3}, {2, 4},
        {2, 5}, {3, 1}, {3, 0}};

    Point[] points = new Point[pointCoords.length];

    for (int i = 0; i < pointCoords.length; i++) {
      points[i] = new Point(pointCoords[i][0], pointCoords[i][1]);
    }

    List<LineSegment> lineSegments = testInstance.findLineSegments(points);

    List<LineSegment> expectedOutput = Arrays.asList(
        new LineSegment(new Point(3, 0), new Point(3, 3)),
        new LineSegment(new Point(1, 1), new Point(4, 4)),
        new LineSegment(new Point(2, 2), new Point(5, 2)),
        new LineSegment(new Point(2, 2), new Point(2, 5)));

    assertIterableEquals(expectedOutput, lineSegments);
  }
}
