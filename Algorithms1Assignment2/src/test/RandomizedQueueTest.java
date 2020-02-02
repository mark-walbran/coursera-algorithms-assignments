import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

// NOTE: not testing randomness of iterator/dequeuing as hard to do

class RandomizedQueueTest {

  private int testSize = 10;

  @Test
  void testRandomizedQueue() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

    for (int i = 0; i < testSize; i++) {
      randomizedQueue.enqueue(i);
    }
    assertEquals(testSize, randomizedQueue.size());
    assertFalse(randomizedQueue.isEmpty());

    for (int i = 0; i < testSize; i++) {
      randomizedQueue.sample();
    }
    assertEquals(testSize, randomizedQueue.size());
    assertFalse(randomizedQueue.isEmpty());

    for (int i = 0; i < testSize; i++) {
      randomizedQueue.dequeue();
    }
    assertEquals(0, randomizedQueue.size());
    assertTrue(randomizedQueue.isEmpty());

    for (int i = 0; i < testSize; i++) {
      randomizedQueue.enqueue(i);
    }
    assertEquals(testSize, randomizedQueue.size());
    assertFalse(randomizedQueue.isEmpty());
  }

  @Test
  void testIterator() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

    for (int i = 0; i < testSize; i++) {
      randomizedQueue.enqueue(i + 1);
    }

    assertEquals(testSize, randomizedQueue.size());
    assertFalse(randomizedQueue.isEmpty());
  }

  @Test
  void testEnqueueException() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

    assertThrows(IllegalArgumentException.class, () -> randomizedQueue.enqueue(null));
  }

  @Test
  void testSampleException() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

    assertThrows(NoSuchElementException.class, randomizedQueue::sample);
  }

  @Test
  void testDequeueException() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

    assertThrows(NoSuchElementException.class, randomizedQueue::dequeue);
  }

  @Test
  void testIteratorNextException() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
    Iterator<Integer> i = randomizedQueue.iterator();

    assertThrows(NoSuchElementException.class, i::next);
  }

  @Test
  void testIteratorRemoveException() {
    RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
    Iterator<Integer> i = randomizedQueue.iterator();

    assertThrows(UnsupportedOperationException.class, i::remove);
  }
}
