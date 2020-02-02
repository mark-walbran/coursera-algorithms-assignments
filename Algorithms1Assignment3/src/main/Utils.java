import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;

/**
 * Shared utility methods.
 */
class Utils {

  // Prevent the class from being instantiated.
  private Utils() {
  }

  public static void checkPoints(Point[] points) {
    // NOTE: All exceptions are as explicitly required in assignment description.
    // DO NOT CHANGE
    if (points == null) {
      throw new IllegalArgumentException("Input must not be null.");
    }

    for (Point point : points) {
      if (point == null) {
        throw new IllegalArgumentException("All of the input points must be non-null.");
      }
    }

    for (int i = 0; i < points.length; i++) {
      for (int j = i + 1; j < points.length; j++) {
        if (points[i].compareTo(points[j]) == 0) {
          throw new IllegalArgumentException("Input must not contain duplicate points.");
        }
      }
    }
  }

  /**
   * Read a series of points from the given file.
   */
  public static Point[] readPoints(String filename) {
    In in = new In(filename);
    int numPoints = in.readInt();
    Point[] points = new Point[numPoints];
    for (int i = 0; i < numPoints; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }
    return points;
  }

  /**
   * Draw the given points on the screen.
   */
  public static void drawPoints(Point[] points) {
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();
  }

  /**
   * Print and draw the given line segments.
   */
  public static void drawSegments(Iterable<LineSegment> segments) {
    for (LineSegment segment : segments) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

  public static void runCollinear(CollinearPoints collinear, String[] args) {
    if (args.length != 1) {
      System.err.println("Usage:");
      System.err.println("  java " + collinear.getClass().getSimpleName() + " <filename>");
      System.exit(1);
    }

    Point[] points = Utils.readPoints(args[0]);

    Utils.drawPoints(points);

    List<LineSegment> segments = collinear.findLineSegments(points);
    Utils.drawSegments(segments);
  }
}
