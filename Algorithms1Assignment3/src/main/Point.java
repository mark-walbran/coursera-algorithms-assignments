/**
 * An immutable data type for points in the plane. For use on Coursera, Algorithms Part I
 * programming assignment.
 */

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

  private final int x; // x-coordinate of this point
  private final int y; // y-coordinate of this point

  /**
   * Initializes a new point.
   * <p>
   * The inputs x and y are stated as assumed to be between 0 and 32,767 by the assignment
   * description, so we do not have to worry about integer overflow or floating-point precision.
   *
   * @param x the <em>x</em>-coordinate of the point
   * @param y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point. Formally, if the two points are
   * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope is
   * defined to be +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if (x0,
   * y0) and (x1, y1) are equal.
   *
   * @param that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    double x0 = this.x;
    double y0 = this.y;
    double x1 = that.x;
    double y1 = that.y;

    // Equal points case
    if ((x0 == x1) && (y0 == y1)) {
      return Double.NEGATIVE_INFINITY;
    }

    // Vertical line case
    if (x0 == x1) {
      return Double.POSITIVE_INFINITY;
    }

    // Horizontal line case
    if (y0 == y1) {
      return +0.0;
    }

    // Otherwise use normal line slope function
    return (y1 - y0) / (x1 - x0);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Point) {
      Point otherPoint = (Point) other;
      return x == otherPoint.x && y == otherPoint.y;
    }
    return false;
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
   * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0
   * = y1 and x0 < x1.
   *
   * @param that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
   * y1); a negative integer if this point is less than the argument point; and a positive integer
   * if this point is greater than the argument point
   */
  @Override
  public int compareTo(Point that) {
    double x0 = this.x;
    double y0 = this.y;
    double x1 = that.x;
    double y1 = that.y;

    // This < that case
    if ((y0 < y1) || ((y0 == y1) && (x0 < x1))) {
      return -1;
    }

    // This == that case
    if ((x0 == x1) && (y0 == y1)) {
      return 0;
    }

    // This > that case
    return +1;
  }

  /**
   * Compares two points by the slope they make with this point. The slope is defined as in the
   * slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return new SlopeOrder();
  }

  private class SlopeOrder implements Comparator<Point> {

    /**
     * Compares two points, q1 and q2, by the slope that they make with this point. Slope is defined
     * as in the slopeTo() method.
     *
     * @param q1 the first point to compare
     * @param q2 the second point to compare
     * @return -1 if slope q1 < q2, 0 if q1 == q2 or +1 if q1 > q2
     */
    @Override
    public int compare(Point q1, Point q2) {
      double q1Slope = slopeTo(q1);
      double q2Slope = slopeTo(q2);

      return Double.compare(q1Slope, q2Slope);
    }
  }

  /**
   * Returns a string representation of this point. This method is provide for debugging; your
   * program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  @Override
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }
}
