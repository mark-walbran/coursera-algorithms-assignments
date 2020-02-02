import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class DequeTest {

  private int testSize = 10;

  @Test
  void testForwardQueue() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = new int[testSize];
    int[] numbersOut = new int[testSize];

    for (int i = 0; i < testSize; i++) {
      deque.addFirst(i);
      numbersExpected[i] = i;
    }

    assertEquals(testSize, deque.size());

    for (int i = 0; i < testSize; i++) {
      numbersOut[i] = deque.removeLast();
    }

    assertArrayEquals(numbersExpected, numbersOut);
    assertTrue(deque.isEmpty());
  }

  @Test
  void testBackQueue() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = new int[testSize];
    int[] numbersOut = new int[testSize];

    for (int i = 0; i < testSize; i++) {
      deque.addLast(i);
      numbersExpected[i] = i;
    }

    assertEquals(testSize, deque.size());

    for (int i = 0; i < testSize; i++) {
      numbersOut[i] = deque.removeFirst();
    }

    assertArrayEquals(numbersExpected, numbersOut);
    assertTrue(deque.isEmpty());
  }

  @Test
  void testForwardStack() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = new int[testSize];
    int[] numbersOut = new int[testSize];

    for (int i = 0; i < testSize; i++) {
      deque.addFirst(i);
      numbersExpected[i] = testSize - i - 1;
    }

    assertEquals(testSize, deque.size());

    for (int i = 0; i < testSize; i++) {
      numbersOut[i] = deque.removeFirst();
    }

    assertArrayEquals(numbersExpected, numbersOut);
    assertTrue(deque.isEmpty());
  }

  @Test
  void testBackStack() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = new int[testSize];
    int[] numbersOut = new int[testSize];

    for (int i = 0; i < testSize; i++) {
      deque.addLast(i);
      numbersExpected[i] = testSize - i - 1;
    }

    assertEquals(testSize, deque.size());

    for (int i = 0; i < testSize; i++) {
      numbersOut[i] = deque.removeLast();
    }

    assertArrayEquals(numbersExpected, numbersOut);
    assertTrue(deque.isEmpty());
  }

  @Test
  void testComplexFunction() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = {4, 2, 6, 5, 8, 7, 3, 1};
    int[] numbersOut = new int[8];

    deque.addFirst(1);
    deque.addLast(2);
    deque.addFirst(3);
    deque.addLast(4);
    deque.addFirst(5);
    deque.addFirst(6);

    numbersOut[0] = deque.removeLast();
    numbersOut[1] = deque.removeLast();
    numbersOut[2] = deque.removeFirst();
    numbersOut[3] = deque.removeFirst();

    deque.addLast(7);
    deque.addLast(8);

    assertEquals(4, deque.size());

    numbersOut[4] = deque.removeLast();
    numbersOut[5] = deque.removeLast();
    numbersOut[6] = deque.removeFirst();
    numbersOut[7] = deque.removeFirst();

    assertArrayEquals(numbersExpected, numbersOut);
    assertTrue(deque.isEmpty());
  }

  @Test
  void testIterator() {
    Deque<Integer> deque = new Deque<>();

    int[] numbersExpected = {5, 3, 1, 2, 4, 6};
    int[] numbersOut = new int[6];

    deque.addFirst(1);
    deque.addLast(2);
    deque.addFirst(3);
    deque.addLast(4);
    deque.addFirst(5);
    deque.addLast(6);

    int i = 0;

    for (Integer s : deque) {
      numbersOut[i] = s;
      i++;
    }

    assertArrayEquals(numbersExpected, numbersOut);
    assertEquals(6, deque.size());
  }

  @Test
  void testAddFirstException() {
    Deque<Integer> deque = new Deque<>();

    assertThrows(IllegalArgumentException.class, () -> deque.addFirst(null));
  }

  @Test
  void testAddLastException() {
    Deque<Integer> deque = new Deque<>();

    assertThrows(IllegalArgumentException.class, () -> deque.addLast(null));
  }

  @Test
  void testRemoveFirstException() {
    Deque<Integer> deque = new Deque<>();

    assertThrows(NoSuchElementException.class, deque::removeFirst);
  }

  @Test
  void testRemoveLastException() {
    Deque<Integer> deque = new Deque<>();

    assertThrows(NoSuchElementException.class, deque::removeLast);
  }

  @Test
  void testIteratorNextException() {
    Deque<Integer> deque = new Deque<>();
    Iterator<Integer> i = deque.iterator();

    assertThrows(NoSuchElementException.class, i::next);
  }

  @Test
  void testIteratorRemoveException() {
    Deque<Integer> deque = new Deque<>();
    Iterator<Integer> i = deque.iterator();

    assertThrows(UnsupportedOperationException.class, i::remove);
  }

  @Test
  void testNonEmptyEmptyNonEmpty() {
    Deque<Integer> deque = new Deque<>();

    deque.addFirst(1);
    assertEquals((Integer) 1, deque.removeLast());

    deque.addLast(2);
    assertEquals((Integer) 2, deque.removeFirst());
  }
}
