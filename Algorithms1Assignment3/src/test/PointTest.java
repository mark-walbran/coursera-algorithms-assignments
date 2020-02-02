import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

// Only testing SlopeTo, compareTo and the comparator slopeOrder because the other methods were
// provided by the Coursera team

class PointTest {

  private int xPoint = 1;
  private int yPoint = 2;

  @Test
  void testSlopeTo() {
    Point point = new Point(xPoint, yPoint);

    Point pointPositiveSlope = new Point(3, 5);
    Point pointNegativeSlope = new Point(2, -1);
    Point pointVerticalSlope = new Point(1, 1);
    Point pointHorizontalSlope = new Point(3, 2);
    Point pointDuplicate = new Point(1, 2);

    assertEquals(1.5, point.slopeTo(pointPositiveSlope));
    assertEquals(-3.0, point.slopeTo(pointNegativeSlope));
    assertEquals(Double.POSITIVE_INFINITY, point.slopeTo(pointVerticalSlope));
    assertEquals(+0.0, point.slopeTo(pointHorizontalSlope));
    assertEquals(Double.NEGATIVE_INFINITY, point.slopeTo(pointDuplicate));
  }

  @Test
  void testCompareTo() {
    Point point = new Point(xPoint, yPoint);

    Point pointGreaterYGreaterX = new Point(2, 3);
    Point pointGreaterYEqualX = new Point(1, 3);
    Point pointGreaterYLesserX = new Point(0, 3);
    Point pointEqualYGreaterX = new Point(2, 2);
    Point pointEqualYEqualX = new Point(1, 2);
    Point pointEqualYLesserX = new Point(0, 2);
    Point pointLesserYGreaterX = new Point(2, 1);
    Point pointLesserYEqualX = new Point(1, 1);
    Point pointLesserYLesserX = new Point(0, 1);

    assertEquals(-1, point.compareTo(pointGreaterYGreaterX));
    assertEquals(-1, point.compareTo(pointGreaterYEqualX));
    assertEquals(-1, point.compareTo(pointGreaterYLesserX));
    assertEquals(-1, point.compareTo(pointEqualYGreaterX));
    assertEquals(0, point.compareTo(pointEqualYEqualX));
    assertEquals(1, point.compareTo(pointEqualYLesserX));
    assertEquals(1, point.compareTo(pointLesserYGreaterX));
    assertEquals(1, point.compareTo(pointLesserYEqualX));
    assertEquals(1, point.compareTo(pointLesserYLesserX));
  }

  @Test
  void testSlopeOrder() {
    Point origin = new Point(xPoint, yPoint);
    Comparator<Point> slopeOrder = origin.slopeOrder();

    // Point less than and greater than
    Point point = new Point(2, 3);
    Point negativePoint = new Point(2, -1);
    Point lesserSlopeThanPoint = new Point(3, 3);
    Point greaterSlopeThanPoint = new Point(2, 4);
    Point horizontalSlope = new Point(3, 2);
    Point verticalSlope = new Point(1, 5);
    Point collinear = new Point(4, 5);
    Point equal = new Point(2, 3);
    Point degeneratae = new Point(1, 2);

    assertEquals(-1, slopeOrder.compare(lesserSlopeThanPoint, point));
    assertEquals(1, slopeOrder.compare(lesserSlopeThanPoint, negativePoint));
    assertEquals(1, slopeOrder.compare(greaterSlopeThanPoint, point));
    assertEquals(1, slopeOrder.compare(greaterSlopeThanPoint, negativePoint));
    assertEquals(-1, slopeOrder.compare(horizontalSlope, point));
    assertEquals(1, slopeOrder.compare(horizontalSlope, negativePoint));
    assertEquals(1, slopeOrder.compare(verticalSlope, point));
    assertEquals(1, slopeOrder.compare(verticalSlope, negativePoint));
    assertEquals(0, slopeOrder.compare(collinear, point));
    assertEquals(0, slopeOrder.compare(equal, point));
    assertEquals(-1, slopeOrder.compare(degeneratae, point));
    assertEquals(-1, slopeOrder.compare(degeneratae, negativePoint));
  }
}
