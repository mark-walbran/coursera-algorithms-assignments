import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class FastCollinearPointsTest extends CollinearPointsTest {

  @Override
  CollinearPoints getInstance() {
    return new FastCollinearPoints();
  }

  @Test
  void testLineSizeGreaterThan4() {
    int[][] pointCoords = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {3, 2}, {4, 2}, {5, 2}, {2, 3}, {2, 4},
        {2, 5}, {3, 1}, {3, 0}, {0, 0}, {3, -1}, {5, 5}, {1, 2}, {1, 3}, {1, 4}};

    Point[] points = new Point[pointCoords.length];

    for (int i = 0; i < pointCoords.length; i++) {
      points[i] = new Point(pointCoords[i][0], pointCoords[i][1]);
    }

    List<LineSegment> lineSegments = testInstance.findLineSegments(points);

    List<LineSegment> expectedOutput = Arrays.asList(
        new LineSegment(new Point(3, -1), new Point(3, 3)),
        new LineSegment(new Point(0, 0), new Point(5, 5)),
        new LineSegment(new Point(1, 1), new Point(1, 4)),
        new LineSegment(new Point(1, 2), new Point(5, 2)),
        new LineSegment(new Point(2, 2), new Point(2, 5)));

    assertIterableEquals(expectedOutput, lineSegments);
  }
}
