import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

class KdTreeTest {

  @Test
  void Circle10Test() {
    // Construct tree and test is empty
    KdTree kdTree = new KdTree();
    assertTrue(kdTree.isEmpty());
    assertEquals(0, kdTree.size());

    // Insert circle10.txt points
    kdTree.insert(new Point2D(0.206107, 0.095492));
    kdTree.insert(new Point2D(0.975528, 0.654508));
    kdTree.insert(new Point2D(0.024472, 0.345492));
    kdTree.insert(new Point2D(0.793893, 0.095492));
    kdTree.insert(new Point2D(0.793893, 0.904508));
    kdTree.insert(new Point2D(0.975528, 0.345492));
    kdTree.insert(new Point2D(0.206107, 0.904508));
    kdTree.insert(new Point2D(0.500000, 0.000000));
    kdTree.insert(new Point2D(0.024472, 0.654508));
    kdTree.insert(new Point2D(0.500000, 1.000000));

    // Insert duplicate point
    kdTree.insert(new Point2D(0.024472, 0.654508));

    // Check it not contains 10 points
    assertFalse(kdTree.isEmpty());
    assertEquals(10, kdTree.size());

    // Check it contains correct points
    assertTrue(kdTree.contains(new Point2D(0.206107, 0.095492)));
    assertTrue(kdTree.contains(new Point2D(0.975528, 0.654508)));
    assertTrue(kdTree.contains(new Point2D(0.024472, 0.345492)));
    assertTrue(kdTree.contains(new Point2D(0.793893, 0.095492)));
    assertTrue(kdTree.contains(new Point2D(0.793893, 0.904508)));
    assertTrue(kdTree.contains(new Point2D(0.975528, 0.345492)));
    assertTrue(kdTree.contains(new Point2D(0.206107, 0.904508)));
    assertTrue(kdTree.contains(new Point2D(0.500000, 0.000000)));
    assertTrue(kdTree.contains(new Point2D(0.024472, 0.654508)));
    assertTrue(kdTree.contains(new Point2D(0.500000, 1.000000)));

    assertFalse(kdTree.contains(new Point2D(0.206106, 0.095492)));
    assertFalse(kdTree.contains(new Point2D(0.975527, 0.654508)));
    assertFalse(kdTree.contains(new Point2D(0.024478, 0.345492)));
    assertFalse(kdTree.contains(new Point2D(0.793899, 0.095492)));
    assertFalse(kdTree.contains(new Point2D(0.793890, 0.904508)));
    assertFalse(kdTree.contains(new Point2D(0.975528, 0.345491)));
    assertFalse(kdTree.contains(new Point2D(0.206107, 0.904502)));
    assertFalse(kdTree.contains(new Point2D(0.500000, 0.000003)));
    assertFalse(kdTree.contains(new Point2D(0.024472, 0.654504)));
    assertFalse(kdTree.contains(new Point2D(0.500000, 0.999995)));

    // Test drawing and rectangles
    kdTree.draw();

    // Test range search
    RectHV rectangle = new RectHV(0.5, 0, 1, 0.5);
    Iterator<Point2D> rangeIterator = kdTree.range(rectangle).iterator();
    Stack<Point2D> expectedRangeStack = new Stack<>();
    expectedRangeStack.push(new Point2D(0.975528, 0.345492));
    expectedRangeStack.push(new Point2D(0.793893, 0.095492));
    expectedRangeStack.push(new Point2D(0.500000, 0.000000));

    for (Point2D point2D : expectedRangeStack) {
      assertTrue(rangeIterator.hasNext());
      assertEquals(point2D, rangeIterator.next());
    }
    assertFalse(rangeIterator.hasNext());

    // Test nearest neighbour method
    assertEquals(new Point2D(0.975528, 0.345492), kdTree.nearest(new Point2D(0.81, 0.30)));
  }

  @Test
  void Input10Test() {
    KdTree kdTree = new KdTree();

    kdTree.insert(new Point2D(0.372, 0.497));
    kdTree.insert(new Point2D(0.564, 0.413));
    kdTree.insert(new Point2D(0.226, 0.577));
    kdTree.insert(new Point2D(0.144, 0.179));
    kdTree.insert(new Point2D(0.083, 0.510));
    kdTree.insert(new Point2D(0.320, 0.708));
    kdTree.insert(new Point2D(0.417, 0.362));
    kdTree.insert(new Point2D(0.862, 0.825));
    kdTree.insert(new Point2D(0.785, 0.725));
    kdTree.insert(new Point2D(0.499, 0.208));

    // Check it not contains 10 points
    assertFalse(kdTree.isEmpty());
    assertEquals(10, kdTree.size());

    // Test drawing and rectangles
    kdTree.draw();

    // Test nearest neighbour method
    assertEquals(new Point2D(0.785, 0.725), kdTree.nearest(new Point2D(0.904, 0.664)));
    assertEquals(new Point2D(0.32, 0.708), kdTree.nearest(new Point2D(0.394, 0.855)));
    assertEquals(new Point2D(0.226, 0.577), kdTree.nearest(new Point2D(0.22, 0.61)));
  }
}
