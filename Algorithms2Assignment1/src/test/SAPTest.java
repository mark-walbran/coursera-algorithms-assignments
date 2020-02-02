import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

class SAPTest {

  @Test
  void diagraph25Test() {
    In in = new In("test-input/digraph25.txt");

    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    Integer[] v = {13, 23, 24};
    Integer[] w = {6, 16, 17};
    Iterable<Integer> iterableV = Arrays.asList(v);
    Iterable<Integer> iterableW = Arrays.asList(w);
    int length = sap.length(iterableV, iterableW);
    int ancestor = sap.ancestor(iterableV, iterableW);
    assertEquals(4, length);
    assertEquals(3, ancestor);

    Integer[] v1 = {13, 23, 24, 3};
    Integer[] w1 = {6, 16, 17, 3};
    iterableV = Arrays.asList(v1);
    iterableW = Arrays.asList(w1);
    length = sap.length(iterableV, iterableW);
    ancestor = sap.ancestor(iterableV, iterableW);
    assertEquals(0, length);
    assertEquals(3, ancestor);
  }

  @Test
  void diagraph1Test() {
    In in = new In("test-input/digraph1.txt");

    Digraph G = new Digraph(in);
    System.out.println(G.toString());
    SAP sap = new SAP(G);
    int[] v = {3, 9, 7, 1};
    int[] w = {11, 12, 2, 6};

    int[] expectedLength = {4, 3, 4, -1};
    int[] expectedAncestor = {1, 5, 0, -1};

    for (int i = 0; i < v.length; i++) {
      assertEquals(expectedLength[i], sap.length(v[i], w[i]));
      assertEquals(expectedAncestor[i], sap.ancestor(v[i], w[i]));
    }
  }

  @Test
  void diagraph4Test() {
    In in = new In("test-input/digraph4.txt");

    Digraph G = new Digraph(in);
    System.out.println(G.toString());
    SAP sap = new SAP(G);
    int[] v = {3, 4};
    int[] w = {3, 1};

    int[] expectedLength = {0, 3};
    int[] expectedAncestor = {3, 4};

    for (int i = 0; i < v.length; i++) {
      assertEquals(expectedLength[i], sap.length(v[i], w[i]));
      assertEquals(expectedAncestor[i], sap.ancestor(v[i], w[i]));
    }

    Integer[] v1 = {4};
    Integer[] w1 = {1, 7};
    Iterable<Integer> iterableV = Arrays.asList(v1);
    Iterable<Integer> iterableW = Arrays.asList(w1);
    int length = sap.length(iterableV, iterableW);
    int ancestor = sap.ancestor(iterableV, iterableW);
    assertEquals(3, length);
    assertEquals(4, ancestor);
  }

  @Test
  void diagraph6Test() {
    In in = new In("test-input/digraph6.txt");

    Digraph G = new Digraph(in);
    System.out.println(G.toString());
    SAP sap = new SAP(G);
    int[] v = {5};
    int[] w = {0};

    int[] expectedLength = {5};
    int[] expectedAncestor = {4};

    for (int i = 0; i < v.length; i++) {
      assertEquals(expectedLength[i], sap.length(v[i], w[i]));
      assertEquals(expectedAncestor[i], sap.ancestor(v[i], w[i]));
    }
  }

  @Test
  void diagraphMarkTest() {
    In in = new In("test-input/digraphMark.txt");

    Digraph G = new Digraph(in);
    System.out.println(G.toString());
    SAP sap = new SAP(G);
    int[] v = {10};
    int[] w = {2};

    int[] expectedLength = {2};
    int[] expectedAncestor = {10};

    for (int i = 0; i < v.length; i++) {
      assertEquals(expectedLength[i], sap.length(v[i], w[i]));
      assertEquals(expectedAncestor[i], sap.ancestor(v[i], w[i]));
    }
  }
}
