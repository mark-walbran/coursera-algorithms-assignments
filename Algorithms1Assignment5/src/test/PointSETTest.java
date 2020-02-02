import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.Stack;

import org.junit.jupiter.api.Test;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

class PointSETTest {

  // Based on sample output given for Circle10.txt input file
  @Test
  void Circle10Test() {

    // Construct set and check empty
    PointSET pointSet = new PointSET();
    assertTrue(pointSet.isEmpty());
    assertEquals(0, pointSet.size());

    // Insert circle10.txt points
    pointSet.insert(new Point2D(0.206107, 0.095492));
    pointSet.insert(new Point2D(0.975528, 0.654508));
    pointSet.insert(new Point2D(0.024472, 0.345492));
    pointSet.insert(new Point2D(0.793893, 0.095492));
    pointSet.insert(new Point2D(0.793893, 0.904508));
    pointSet.insert(new Point2D(0.975528, 0.345492));
    pointSet.insert(new Point2D(0.206107, 0.904508));
    pointSet.insert(new Point2D(0.500000, 0.000000));
    pointSet.insert(new Point2D(0.024472, 0.654508));
    pointSet.insert(new Point2D(0.500000, 1.000000));

    // Insert duplicate point
    pointSet.insert(new Point2D(0.024472, 0.654508));

    // Check it not contains 10 points
    assertFalse(pointSet.isEmpty());
    assertEquals(10, pointSet.size());

    // Check it contains correct points
    assertTrue(pointSet.contains(new Point2D(0.206107, 0.095492)));
    assertFalse(pointSet.contains(new Point2D(0.206106, 0.095492)));

    // Test draw
    pointSet.draw();

    // Test range search
    RectHV rectangle = new RectHV(0.5, 0, 1, 0.5);
    Iterator<Point2D> rangeIterator = pointSet.range(rectangle).iterator();
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
    assertEquals(new Point2D(0.975528, 0.345492), pointSet.nearest(new Point2D(0.81, 0.30)));
  }
}
