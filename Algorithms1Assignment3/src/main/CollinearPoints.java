import java.util.List;

/**
 * A utility for finding all line segments containing 4 or more points from a given set of points.
 */
public interface CollinearPoints {

  /**
   * Finds all of the line segments containing 4 or more points.
   */
  List<LineSegment> findLineSegments(Point[] points);
}
