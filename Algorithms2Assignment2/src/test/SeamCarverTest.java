import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

class SeamCarverTest {

  @Test
  void nullConstructorTest() {
    assertThrows(IllegalArgumentException.class, () -> {
      new SeamCarver(null);
    });
  }

  @Test
  void energyOutOfRangeTest() {
    Picture picture = new Picture("test-input/HJOceanSmall.png");
    SeamCarver sc = new SeamCarver(picture);

    assertThrows(IllegalArgumentException.class, () -> {
      sc.energy(sc.width(), 0);
    });
  }

  @Test
  void energyTest() {
    Picture picture = new Picture("test-input/HJOceanSmall.png");
    SeamCarver sc = new SeamCarver(picture);

    assertEquals(1000, sc.energy(10, sc.height() - 1));

    Color colorLeft = picture.get(1, 3);
    Color colorRight = picture.get(3, 3);
    Color colorUp = picture.get(2, 2);
    Color colorDown = picture.get(2, 4);

    double expectedEnergy = Math.sqrt(Math.pow((colorLeft.getRed() - colorRight.getRed()), 2)
        + Math.pow((colorLeft.getGreen() - colorRight.getGreen()), 2)
        + Math.pow((colorLeft.getBlue() - colorRight.getBlue()), 2)
        + Math.pow((colorUp.getRed() - colorDown.getRed()), 2)
        + Math.pow((colorUp.getGreen() - colorDown.getGreen()), 2)
        + Math.pow((colorUp.getBlue() - colorDown.getBlue()), 2));

    assertEquals(expectedEnergy, sc.energy(2, 3));
  }

  /**
   * The following commented out tests were used during the development process. They do have no
   * assert statements, but were used to manually visually check if the image processing results are
   * as expected.
   */
//  @Test
//  void verticalHorizontalSeamRemovalMixingTest() {
//    Picture picture = new Picture("test-input/HJOceanSmall.png");
//    int removeColumnsOrRows = 200;
//
//    StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
//    SeamCarver sc = new SeamCarver(picture);
//
//    Stopwatch sw = new Stopwatch();
//
//    for (int i = 0; i < removeColumnsOrRows; i++) {
//      if (Math.random() < 0.6) {
//        int[] verticalSeam = sc.findVerticalSeam();
//        sc.removeVerticalSeam(verticalSeam);
//      } else {
//        int[] horizontalSeam = sc.findHorizontalSeam();
//        sc.removeHorizontalSeam(horizontalSeam);
//      }
//    }
//
//    StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
//
//    StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
//    picture.show();
//    sc.picture().show();
//
//    System.out.println("Press Enter to continue");
//    try {
//      System.in.read();
//    } catch (Exception e) {
//    }
//  }
//
//  @Test
//  void showEnergyTest() {
//    Picture picture = new Picture("test-input/HJOceanSmall.png");
//    StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
//    picture.show();
//    SeamCarver sc = new SeamCarver(picture);
//
//    StdOut.printf("Displaying energy calculated for each pixel.\n");
//    SCUtility.showEnergy(sc);
//
//    System.out.println("Press Enter to continue");
//    try {
//      System.in.read();
//    } catch (Exception e) {
//    }
//  }
//
//  @Test
//  void showVerticalSeamTest() {
//    Picture picture = new Picture("test-input/HJOceanSmall.png");
//    StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
//    picture.show();
//    SeamCarver sc = new SeamCarver(picture);
//
//    StdOut.printf("Displaying vertical seam calculated.\n");
//    showVerticalSeam(sc);
//
//    System.out.println("Press Enter to continue");
//    try {
//      System.in.read();
//    } catch (Exception e) {
//    }
//  }
//
//  @Test
//  void showHorizontalSeamTest() {
//    Picture picture = new Picture("test-input/HJOceanSmall.png");
//    StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
//    picture.show();
//    SeamCarver sc = new SeamCarver(picture);
//
//    StdOut.printf("Displaying vertical seam calculated.\n");
//    showHorizontalSeam(sc);
//
//    System.out.println("Press Enter to continue");
//    try {
//      System.in.read();
//    } catch (Exception e) {
//    }
//  }
//
//  @Test
//  void verticalHorizontalSeamRemovalTest() {
//    Picture picture = new Picture("test-input/HJOceanSmall.png");
//    int removeColumns = 150;
//    int removeRows = 100;
//
//    StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
//    SeamCarver sc = new SeamCarver(picture);
//
//    Stopwatch sw = new Stopwatch();
//
//    for (int i = 0; i < removeRows; i++) {
//      int[] horizontalSeam = sc.findHorizontalSeam();
//      sc.removeHorizontalSeam(horizontalSeam);
//    }
//
//    for (int i = 0; i < removeColumns; i++) {
//      int[] verticalSeam = sc.findVerticalSeam();
//      sc.removeVerticalSeam(verticalSeam);
//    }
//
//    StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
//
//    StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
//    picture.show();
//    sc.picture().show();
//
//    System.out.println("Press Enter to continue");
//    try {
//      System.in.read();
//    } catch (Exception e) {
//    }
//  }
//
//  private void showHorizontalSeam(SeamCarver sc) {
//    Picture ep = SCUtility.toEnergyPicture(sc);
//    int[] horizontalSeam = sc.findHorizontalSeam();
//    Picture epOverlay = SCUtility.seamOverlay(ep, true, horizontalSeam);
//    epOverlay.show();
//  }
//
//  private void showVerticalSeam(SeamCarver sc) {
//    Picture ep = SCUtility.toEnergyPicture(sc);
//    int[] verticalSeam = sc.findVerticalSeam();
//    Picture epOverlay = SCUtility.seamOverlay(ep, false, verticalSeam);
//    epOverlay.show();
//  }
}
