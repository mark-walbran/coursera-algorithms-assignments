

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements a more efficient method to find all sets of 4 or more collinear points
 * within the input set.
 */
public class FastCollinearPoints implements CollinearPoints {

  /**
   * Finds all of the line segments containing 4 or more points.
   * <p>
   * Uses faster method than BruteCollinearPoints. For each point p, all of the other points are
   * first sorted by the slope that they make with p. We then examine the array to see if any set of
   * 3 or more adjacent points in the sorted array have equal slopes with the respect to p. If so
   * then they are collinear.
   *
   * @param points the set of points within which to search.
   * @throws IllegalArgumentException if the argument to the constructor is null.
   * @throws IllegalArgumentException if any point in the array is null.
   * @throws IllegalArgumentException if the argument to the constructor contains a repeated point.
   */
  @Override
  public List<LineSegment> findLineSegments(Point[] points) {
    Utils.checkPoints(points);
    points = points.clone();

    List<LineSegment> lineSegmentList = new ArrayList<>();
    Arrays.sort(points);

    for (int p = 0; p < points.length; p++) {
      Point[] pointsTemp = points.clone();

      Arrays.sort(pointsTemp, pointsTemp[p].slopeOrder());
      int collinearCounter = 0;

      // Iterate from p + 2 up until the end of the sorted array
      for (int q = 2; q < pointsTemp.length; q++) {
        // If the current point has the same slope to p as the previous point then increase counter
        if (points[p].slopeTo(pointsTemp[q - 1]) == points[p].slopeTo(pointsTemp[q])) {
          collinearCounter++;
        }

        if (q == pointsTemp.length - 1
            || points[p].slopeTo(pointsTemp[q]) != points[p].slopeTo(pointsTemp[q + 1])) {
          // If end of a segment of 3 or more points with equal slope to p then create lineSegment
          int startPoint = q - collinearCounter;
          if (collinearCounter >= 2 && points[p].compareTo(pointsTemp[startPoint]) < 0) {
            lineSegmentList.add(new LineSegment(points[p], pointsTemp[q]));
          }
          collinearCounter = 0;
        }
      }
    }

    return lineSegmentList;
  }

  /**
   * Client function provided with assignment
   *
   * @param args the file to to be read.
   */
  public static void main(String[] args) {
    Utils.runCollinear(new FastCollinearPoints(), args);
  }
}
