/**
 * This class implements a brute force method to find all sets of 4 or more collinear points within
 * the input set.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints implements CollinearPoints {

  /**
   * Finds all of the line segments containing 4 or more points.
   * <p>
   * Uses brute force method. Examines 4 points at a time, checks whether they all lie on the same
   * line segment, and then returns all such line segments. To check whether the 4 points p, q, r,
   * and s are collinear, we check whether the three slopes between p and q, between p and r, and
   * between p and s are all equal.
   * <p>
   * Stated in assignment description that for simplicity it does not need to handle inputs
   * containing 5 or more collinear points.
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
      for (int q = p + 1; q < points.length; q++) {
        double slopeQ = points[p].slopeTo(points[q]);
        for (int r = q + 1; r < points.length; r++) {
          double slopeR = points[p].slopeTo(points[r]);
          if (slopeQ == slopeR) {
            for (int s = r + 1; s < points.length; s++) {
              double slopeS = points[p].slopeTo(points[s]);
              if (slopeQ == slopeS) {
                lineSegmentList.add(new LineSegment(points[p], points[s]));
              }
            }
          }
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
    Utils.runCollinear(new BruteCollinearPoints(), args);
  }
}
