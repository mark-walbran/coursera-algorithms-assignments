import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * A "naive"/brute-force implementation of a data-type that represents a set of points on the unit
 * square. Allows range search and nearest-neighbour search operations. For use on Coursera,
 * Algorithms Part I programming assignment.
 */

public class PointSET {

  private final TreeSet<Point2D> pointsBST;

  /**
   * Constructs an empty set of points.
   */
  public PointSET() {
    pointsBST = new TreeSet<>();
  }

  /**
   * Is the set empty?
   */
  public boolean isEmpty() {
    return pointsBST.isEmpty();
  }

  /**
   * Returns the number of points in the set.
   */
  public int size() {
    return pointsBST.size();
  }

  /**
   * Adds a point to the set (if it is not already in the set).
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. Duplicate points are ignored. Operation time
   * proportional to logN.
   *
   * @param p the point.
   * @throws IllegalArgumentException if any argument is null.
   */
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Null input.");
    }

    pointsBST.add(p);
  }

  /**
   * Does the set contain a point?
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. Operation time proportional to logN.
   *
   * @param p the point.
   * @throws IllegalArgumentException if any argument is null.
   */
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Null input.");
    }

    return pointsBST.contains(p);
  }

  /**
   * Draws all points to standard draw.
   */
  public void draw() {
    StdDraw.clear();

    // Drawing points
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);

    for (Point2D point : pointsBST) {
      StdDraw.point(point.x(), point.y());
    }
  }

  /**
   * Returns an iterator for all points that are inside a rectangle (or on the boundary).
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. Operation time proportional to N. If there
   * are no points in the set, returns an Iterable<Point2D> object with zero points.
   *
   * @param rect the rectangle.
   * @throws IllegalArgumentException if any argument is null.
   */
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("Null input.");
    }

    Iterator<Point2D> pointsIterator = pointsBST.iterator();
    Stack<Point2D> rangeIterator = new Stack<>();

    while (pointsIterator.hasNext()) {
      Point2D point = pointsIterator.next();

      if (rect.contains(point)) {
        rangeIterator.push(point);
      }
    }

    return rangeIterator;
  }


  /**
   * Returns a nearest neighbor in the set to point p; null if the set is empty.
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. Operation time proportional to N.
   *
   * @param p the point.
   * @throws IllegalArgumentException if any argument is null.
   */
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Null input.");
    }

    if (isEmpty()) {
      return null;
    }

    // Initialise first point as the nearest neighbour to begin
    Iterator<Point2D> pointsIterator = pointsBST.iterator();
    Point2D nearestNeighbor = pointsIterator.next();
    double lowestDistance = nearestNeighbor.distanceSquaredTo(p);

    // Keep searching for nearer neighbours
    while (pointsIterator.hasNext()) {
      Point2D point = pointsIterator.next();
      double distance = point.distanceSquaredTo(p);

      if (distance < lowestDistance) {
        lowestDistance = distance;
        nearestNeighbor = point;
      }
    }

    return nearestNeighbor;
  }
}
