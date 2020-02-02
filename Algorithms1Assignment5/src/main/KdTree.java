import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

/**
 * An implementation of a data-type that represents a set of points on the unit square using a
 * 2d-tree. Allows efficient range search and nearest-neighbour search operations. For use on
 * Coursera, Algorithms Part I programming assignment.
 */

public class KdTree {

  // See suggested order of steps at bottom of FAQ

  private Node root;
  private int size;

  private static class Node {

    private final Point2D p; // the point
    private final RectHV rect; // the axis-aligned rectangle corresponding to this node
    private Node lb; // the left/bottom subtree
    private Node rt; // the right/top subtree

    public Node(Point2D p, RectHV rect) {
      this.p = p;
      this.rect = rect;
    }

    // Orientation; 0 = vertical; 1 = horizontal
    public int compareTo(Point2D point, boolean orientation) {
      double x = this.p.x();
      double y = this.p.y();
      double xComparison = point.x();
      double yComparison = point.y();

      if (orientation) {
        if (yComparison < y) {
          return -1;
        } else if (yComparison > y) {
          return 1;
        } else {
          return Double.compare(xComparison, x);
        }
      } else {
        if (xComparison < x) {
          return -1;
        } else if (xComparison > x) {
          return 1;
        } else {
          return Double.compare(yComparison, y);
        }
      }
    }
  }

  private static class NearestNode {

    Node node;
    double distance;

    public NearestNode(Node node, double distance) {
      this.node = node;
      this.distance = distance;
    }
  }

  /**
   * Constructs an empty set of points.
   */
  public KdTree() {

  }

  /**
   * Is the set empty?
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the number of points in the set.
   */
  public int size() {
    return size;
  }

  /**
   * Adds a point to the set (if it is not already in the set).
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. Duplicate points are ignored.
   *
   * @param p the point.
   * @throws IllegalArgumentException if any argument is null.
   */
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Null input.");
    }

    root = insert(root, p, false);
  }

  private Node insert(Node node, Point2D p, boolean orientation) {
    RectHV rectangle;

    if (root == null) {
      size++;
      rectangle = new RectHV(0, 0, 1, 1);
      return new Node(p, rectangle);
    }

    int cmp = node.compareTo(p, orientation);
    if (cmp < 0) {
      if (node.lb == null) {
        size++;
        if (orientation) {
          rectangle = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
        } else {
          rectangle = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }

        node.lb = new Node(p, rectangle);
      } else {
        node.lb = insert(node.lb, p, !orientation);
      }
    } else if (cmp > 0) {
      if (node.rt == null) {
        size++;
        if (orientation) {
          rectangle = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
        } else {
          rectangle = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
        }

        node.rt = new Node(p, rectangle);
      } else {
        node.rt = insert(node.rt, p, !orientation);
      }
    }

    return node;
  }

  /**
   * Does the set contain a point?
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1].
   *
   * @param p the point.
   * @throws IllegalArgumentException if any argument is null.
   */
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Null input.");
    }

    return contains(root, p, false);
  }

  private boolean contains(Node node, Point2D p, boolean orientation) {
    if (node == null) {
      return false;
    }

    int cmp = node.compareTo(p, orientation);
    if (cmp < 0) {
      return (contains(node.lb, p, !orientation));
    } else if (cmp > 0) {
      return (contains(node.rt, p, !orientation));
    } else {
      return true;
    }
  }

  /**
   * Draws all points to standard draw.
   */
  public void draw() {
    StdDraw.clear();
    draw(root, false);
  }

  private void draw(Node node, boolean orientation) {
    if (node.lb != null) {
      draw(node.lb, !orientation);
    }

    double x = node.p.x();
    double y = node.p.y();

    if (orientation) {
      // Drawing horizontal lines
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius();
      StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
    } else {
      // Drawing vertical lines
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.setPenRadius();
      StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());
    }

    // Drawing current point
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    StdDraw.point(x, y);

    if (node.rt != null) {
      draw(node.rt, !orientation);
    }
  }


  /**
   * Returns an iterator for all points that are inside a rectangle (or on the boundary).
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1]. If there are no points in the set, returns
   * an Iterable<Point2D> object with zero points.
   *
   * @param rect the rectangle.
   * @throws IllegalArgumentException if any argument is null.
   */
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("Null input.");
    }

    return range(root, new Stack<>(), rect);
  }

  private Stack<Point2D> range(Node node, Stack<Point2D> stack, RectHV rect) {
    if (node == null) {
      return stack;
    }

    if ((node.lb != null) && (rect.intersects(node.lb.rect))) {
      stack = range(node.lb, stack, rect);
    }

    Point2D point = node.p;
    if (rect.contains(point)) {
      stack.push(point);
    }

    if ((node.rt != null) && (rect.intersects(node.rt.rect))) {
      stack = range(node.rt, stack, rect);
    }

    return stack;
  }

  /**
   * Returns a nearest neighbor in the set to point p; null if the set is empty.
   * <p>
   * <p>
   * All points are assumed to belong to the set [0,1].
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
    NearestNode nearestNode = new NearestNode(root, root.p.distanceSquaredTo(p));

    return nearest(root, p, nearestNode, false).node.p;
  }

  private NearestNode nearest(Node node, Point2D p, NearestNode nearestNode, boolean orientation) {
    double distance = node.p.distanceSquaredTo(p);
    if (distance < nearestNode.distance) {
      nearestNode.distance = distance;
      nearestNode.node = node;
    }

    Node firstNode = null;
    Node secondNode = null;
    if ((node.lb != null)
        && (orientation && (p.y() <= node.p.y()) || (!orientation && (p.x() <= node.p.x())))) {
      firstNode = node.lb;
      secondNode = node.rt;
    } else if ((node.rt != null)
        && (orientation && (p.y() >= node.p.y()) || (!orientation && (p.x() >= node.p.x())))) {
      firstNode = node.rt;
      secondNode = node.lb;
    }

    if (firstNode != null) {
      if (firstNode.rect.distanceSquaredTo(p) < nearestNode.distance) {
        nearestNode = nearest(firstNode, p, nearestNode, !orientation);
      }
      if ((secondNode != null) && (secondNode.rect.distanceSquaredTo(p) < nearestNode.distance)) {
        nearestNode = nearest(secondNode, p, nearestNode, !orientation);
      }
    } else if ((node.lb != null) && (node.lb.rect.distanceSquaredTo(p) < nearestNode.distance)) {
      nearestNode = nearest(node.lb, p, nearestNode, !orientation);
    } else if ((node.rt != null) && (node.rt.rect.distanceSquaredTo(p) < nearestNode.distance)) {
      nearestNode = nearest(node.rt, p, nearestNode, !orientation);
    }

    return nearestNode;
  }
}
